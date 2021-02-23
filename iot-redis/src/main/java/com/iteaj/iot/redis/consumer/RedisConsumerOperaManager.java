package com.iteaj.iot.redis.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class RedisConsumerOperaManager implements InitializingBean, DisposableBean {

    private RedisTemplate redisTemplate;

    /**
     * 所有消费对象列表
     */
    private List<RedisConsumerOpera> operas;

    private ThreadPoolTaskExecutor executor;

    /**
     * 用来处理需要阻塞的消费对线
     */
    private ExecutorService blockExecutorService;

    List<RedisConsumerWrapper> consumers = new ArrayList<>();
    /**
     * 需要阻塞消费的消费列表
     */
    List<RedisConsumerWrapper> blockConsumers = new ArrayList<>();
    /**
     * 正在执行消费的非阻塞消费列表
     */
    List<RedisConsumerWrapper> execConsumers = Collections.synchronizedList(new ArrayList<>());
    /**
     * 用来处理消费对象
     */
    private ExecutorService consumerExecutorService = Executors.newFixedThreadPool(1);

    private Logger logger = LoggerFactory.getLogger(getClass());

    public RedisConsumerOperaManager(List<RedisConsumerOpera> operas, RedisTemplate redisTemplate, ThreadPoolTaskExecutor executor) {
        this.operas = operas;
        this.executor = executor;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        operas.forEach(opera -> {
            if(opera instanceof BlockConsumerOpera) {
                List<RedisConsumer> blocks = ((BlockConsumerOpera) opera).blocks();
                if(!CollectionUtils.isEmpty(blocks)) {
                    List<RedisConsumerWrapper> wrappers = blocks.stream().map(item ->
                            new RedisConsumerWrapper(item, opera)).collect(Collectors.toList());
                    blockConsumers.addAll(wrappers);
                } else {
                    List<RedisConsumer> consumers = opera.consumers();
                    if(!CollectionUtils.isEmpty(consumers)) {
                        List<RedisConsumerWrapper> wrappers = consumers.stream().map(item ->
                                new RedisConsumerWrapper(item, opera)).collect(Collectors.toList());

                        this.consumers.addAll(wrappers);
                    }
                }
            } else {
                List<RedisConsumer> consumers = opera.consumers();
                if(!CollectionUtils.isEmpty(consumers)) {
                    List<RedisConsumerWrapper> wrappers = consumers.stream().map(item ->
                            new RedisConsumerWrapper(item, opera)).collect(Collectors.toList());

                    this.consumers.addAll(wrappers);
                }
            }
        });

        if(!CollectionUtils.isEmpty(this.consumers)) {
            this.execConsumers.addAll(this.consumers);

            // 执行消费处理算法任务
            consumerExecutorService.execute(new ConsumerHandleTask());
        }

        if(!CollectionUtils.isEmpty(blockConsumers)) {
            blockExecutorService = Executors.newFixedThreadPool(blockConsumers.size());
            blockConsumers.forEach(item -> {
                blockExecutorService.execute(new BlockHandleTask(item));
            });
        }


    }

    @Override
    public void destroy() throws Exception {
        if(blockExecutorService != null) {
            blockExecutorService.shutdownNow();
            logger.error("关闭Redis阻塞队列线程池成功");
        }

        if(!consumerExecutorService.isShutdown()) {
            consumerExecutorService.shutdownNow();
            logger.error("关闭Redis消费任务算法执行器成功");
        }

        final ThreadPoolExecutor poolExecutor = executor.getThreadPoolExecutor();
        if(!poolExecutor.isShutdown()) {
            executor.destroy();
        }
    }

    /**
     * 阻塞处理任务
     */
    class BlockHandleTask implements Runnable {

        private RedisConsumerWrapper consumerWrapper;

        public BlockHandleTask(RedisConsumerWrapper consumerWrapper) {
            this.consumerWrapper = consumerWrapper;
        }

        @Override
        public void run() {
            RedisConsumer consumer = consumerWrapper.consumer;
            BlockConsumerOpera consumerOpera = (BlockConsumerOpera)consumerWrapper.consumerOpera;
            while (true) {
                try {
                    List invoker = consumerOpera.invoker(consumer.getKey(), (long) 30 * 60);
                    // 如果已经生产队列里面已经有数据
                    if(!CollectionUtils.isEmpty(invoker)) {

                        // 先消费刚阻塞返回的数据
                        consumerWrapper.consumer(invoker);

                        // 继续读取数据列表
                        List list = consumerOpera.invoker(consumer.getKey(), consumer.maxSize());
                        Integer consumerNum = consumerWrapper.consumer(list); // 消费数据的回调, 并且返回消费成功的条数

                        // 如果没有返回消费成功的数据, 那么将读取的长度作为消费成功的长度进行移除
                        consumerNum = consumerNum == null ? list.size() : consumerNum;
                        if(consumerNum != 0) {
                            consumerOpera.remove(consumer.getKey(), consumerNum);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Redis消费管理异常({}) - key: {} - 消费对象: {}", e.getMessage(), consumer.getKey(), consumer.getClass(), e);
                    continue;
                }
            }
        }
    }

    /**
     * 此任务会一直循环执行任务<br>
     *     1. 如果执行列表还有任务则将任务加入任务队列执行, 并且将任务从当前可执行列表里面移除, 等到此任务执行完之后再将任务加入到可执行列表
     *     2. 如果执行列表暂无任务, 将暂时休眠等待10秒
     */
    class ConsumerHandleTask implements Runnable {

        @Override
        public void run() {
            for (;;) {
                try {
                    Iterator<RedisConsumerWrapper> iterator = execConsumers.iterator();

                    while (iterator.hasNext()) {
                        RedisConsumerWrapper next = iterator.next();
                        iterator.remove(); // 从执行列表里面移除

                        // 加入消费任务
                        executor.execute(new ConsumerTask(next));
                    }

                    // 执行列表没有可执行消费对象, 暂时休眠10秒
                    if(execConsumers.size() == 0) {
                        Thread.sleep(10 * 1000);
                    }
                } catch (Exception e) {
                    logger.error("Redis消费任务算法异常({})", e.getMessage(), e);
                } finally {
                    continue;
                }
            }
        }
    }

    class ConsumerTask implements Runnable {

        private RedisConsumerWrapper consumerWrapper;

        public ConsumerTask(RedisConsumerWrapper consumerWrapper) {
            this.consumerWrapper = consumerWrapper;
        }

        @Override
        public void run() {
            RedisConsumer consumer = consumerWrapper.consumer;

            try {
                // 开始消费
                List invoker = consumerWrapper.consumerOpera.invoker(consumer.getKey(), consumer.maxSize());

                // 执行消费回调
                if(!CollectionUtils.isEmpty(invoker)) {
                    Integer consumerNum = consumerWrapper.consumer(invoker);

                    // 消费完之后移除消费的数据
                    consumerNum = consumerNum == null ? invoker.size() : consumerNum;
                    if(consumerNum != 0) {
                        consumerWrapper.consumerOpera.remove(consumer.getKey(), consumerNum);
                    }
                }
            } catch (Exception e) {
                logger.error("Redis消费管理异常({}) - key: {} - 消费对象: {}", e.getMessage(), consumer.getKey(), consumer.getClass(), e);
            } finally {

                // 此消费如果执行完重新加入执行列表
                execConsumers.add(consumerWrapper);
            }
        }
    }

    class RedisConsumerWrapper implements RedisConsumer {

        // 值类型
        private Class<?> valueClazz;
        private RedisConsumer consumer;
        private RedisConsumerOpera consumerOpera;

        public RedisConsumerWrapper(RedisConsumer consumer, RedisConsumerOpera consumerOpera) {
            this.consumer = consumer;
            this.consumerOpera = consumerOpera;

            Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(consumer.getClass(), RedisConsumer.class);
            valueClazz = typeArguments != null && typeArguments.length != 0 ? typeArguments[0] : null;
        }

        @Override
        public String getKey() {
            return consumer.getKey();
        }

        @Override
        public Integer consumer(List v) {
            if(valueClazz != null) {
                v = consumerOpera.deserialize(v, valueClazz);
            }

            return consumer.consumer(v);
        }

        public RedisConsumer getConsumer() {
            return consumer;
        }

        public RedisConsumerOpera getConsumerOpera() {
            return consumerOpera;
        }

        public Class<?> getValueClazz() {
            return valueClazz;
        }
    }

}
