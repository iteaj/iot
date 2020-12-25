package com.iteaj.iot.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.iteaj.iot.redis.consumer.ListConsumer;
import com.iteaj.iot.redis.consumer.ListConsumerOpera;
import com.iteaj.iot.redis.consumer.RedisConsumerOpera;
import com.iteaj.iot.redis.consumer.RedisConsumerOperaManager;
import com.iteaj.iot.redis.producer.RedisProducer;
import com.iteaj.iot.redis.proxy.ProtocolHandleProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

@EnableConfigurationProperties(IotRedisProperties.class)
@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration")
public class IotRedisConfiguration implements BeanPostProcessor {

    @Autowired
    private IotRedisProperties properties;
    @Autowired
    private ObjectProvider<ProtocolHandleProxy> protocolHandleProxy;

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建RedisTemplate<String, Object>对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 配置连接工厂
        template.setConnectionFactory(factory);

        // FastJsonRedisSerializer
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会报异常
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jacksonSeial.setObjectMapper(om);
        StringRedisSerializer stringSerial = new StringRedisSerializer();

        // redis key 序列化方式使用stringSerial
        template.setKeySerializer(stringSerial);
        // redis value 序列化方式使用jackson
        template.setValueSerializer(fastJsonRedisSerializer);
        // redis hash key 序列化方式使用stringSerial
        template.setHashKeySerializer(stringSerial);
        // redis hash value 序列化方式使用jackson
        template.setHashValueSerializer(fastJsonRedisSerializer);

        return IotRedis.wrapper.template = template;
    }

    /**
     * 协议处理器代理
     * 如果需要使生产者生效, 必须对生产者对象进行代理
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "iot.redis", value = "producer", havingValue = "true")
    public ProtocolHandleProxy protocolHandleProxy(RedisTemplate redisTemplate) {
        return new ProtocolHandleProxy(redisTemplate);
    }

    /**
     * 开启消费者任务执行管理器
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "iot.redis", value = "consumer", havingValue = "true")
    public RedisConsumerOperaManager redisConsumerOperaManager(List<RedisConsumerOpera> operas
            , RedisTemplate redisTemplate, ThreadPoolTaskExecutor executor) {
        return new RedisConsumerOperaManager(operas, redisTemplate, executor);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = {ListConsumer.class, RedisConsumerOperaManager.class})
    public ListConsumerOpera listConsumerOpera(List<ListConsumer> consumers) {
        return new ListConsumerOpera(consumers);
    }

    /**
     *
     * @see com.iteaj.network.ProtocolHandle
     * @see RedisProducer 对所有的RedisProducer进行代理
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(properties.isProducer() && bean instanceof RedisProducer) {
            ProtocolHandleProxy ifAvailable = protocolHandleProxy.getIfAvailable();
            return ifAvailable.createProxy((RedisProducer) bean);
        }

        return bean;
    }
}
