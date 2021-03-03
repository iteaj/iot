#### iot物联网框架
基于netty, spring boot, redis等开源项目开发来的物联网框架, 支持udp, tcp底层协议和http, mqtt, modbus等上层协议. 支持对设备同步和异步的调用操作. 主要向开发人员开放了一套统一、简洁的用于操作设备的Api接口. 该框架只提供和设备对接部分（通过spring的bean注入解耦特性实现业务和协议实现分离）, 使用此框架的客户必须遵循此框架设计的Api规范;接入一台设备只需创建两三个对象(协议的实现不算在内).并提供丰富的日志输出用于支持调试以及详细的代码注释
##### 版本管理
正式版: 功能较少
```
 <dependency>
      <groupId>com.iteaj</groupId>
      <artifactId>iot-xxx</artifactId>
      <version>1.0.0</version>
  </dependency>
```
快照版：相比于1.0.0版本新增了很多实用功能并精简和优化了很多代码，所以不建议使用1.0.0版本，由于快照版本的api已经固定后期将不会大改，已经可以使用
```
 <dependency>
      <groupId>com.iteaj</groupId>
      <artifactId>iot-xxx</artifactId>
      <version>1.1.0-SNAPSHOT</version>
  </dependency>
```
#### 1. 主要功能
1. 支持监听多个端口, 对接多种设备类型
2. 拥有一个设备库, 此库已经对接好多种设备
3. 支持多种协议, tcp, udp, http等
4. 包含一套基于tcp的应用客户端和tcp的服务端进行通讯
5. 支持设备协议对象和其业务对象进行分离(支持默认业务处理器【spring单例注入】和自定义业务处理器)
6. 支持redis, 可以方便的将设备上报的数据进行缓存到redis和从redis获取数据
7. 支持对数据的生产采集和数据的消费进行分离
8. 支持同步和异步调用设备, 支持应用程序客户端和设备服务端和设备三端之间的同步和异步调用
9. 服务端支持设备上线/下线/异常的事件通知, 支持自定义心跳事件， 客户端支持断线重连
10. 丰富的日志打印功能，包括设备上线，下线提示， 一个协议的生命周期(请求或者请求+响应)等

#### 2. 架构图
![iot架构图](https://images.gitee.com/uploads/images/2021/0303/210010_da7cfaa4_1230742.png "iot.png")
1. 设备端和设备管理端同步和异步如线3、4所示
    * 平台主动向设备发起请求4->3：如果是同步调用则线程将等待设备返回在往下执行业务， 如果设备由于某些原因没有返回则将在指定的超时时间内解锁线程继续向下执行业务。如果是异步调用则在设备响应后会执行业务，并不会锁住当前调用的线程
    * 设备主动向平台发起请求3->4：如线3设备主动发起请求给平台， 平台将先解析设备请求的报文然后调用执行业务，执行完业务之后如线4平台将生成需要响应给设备的报文并推送给设备
2.  应用管理端和设备管理端同步和异步(1、2所示)和1相同的流程
3.  应用管理端和设备进行同步和异步调用如：1、2、3、4
    * 如果是同步：由应用客户端发起调用如线1所示，发起调用的线程将阻塞， 然后如线4所示设备管理端向设备发起异步调用，接着等待设备响应如线3，再然后设备管理端响应应用客户端如线2，最后将释放应用客户端的线程锁继续向下执行
    * 如果是异步：和同步的区别是应用客户端不加锁
#### 3. 已对接的设备(具体看设备仓库模块(devices))
1. 福州瑞邦断路器
2. 福州瑞邦智慧融合控制台
3. 道路运输车辆主动安全智能防控系统(GPS定位)
4. 7合一环境监测设备
5. 十七合一环境监测设备
6. 温湿度监测和空调红外控制设备
7. 中控智慧门禁BS版
8. 中央空调控制面板
...

