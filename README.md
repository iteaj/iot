#### iot
基于netty, spring boot, redis等开源项目开发来的物联网框架, 支持udp, tcp, http底层协议和mqtt, modbus等上层协议. 支持对设备同步和异步的调用操作
#### 1. 主要功能
1. 支持监听多个端口, 对接多种设备类型
2. 拥有一个设备库, 此库已经对接好多种设备
3. 支持多种协议, tcp, udp, http等
4. 包含一套基于tcp的应用客户端和tcp的服务端进行通讯
5. 支持设备协议的解析和其业务进行分离
6. 支持redis, 可以方便的将设备上报的协议进行缓存到redis和从redis获取数据
7. 支持对数据的生产采集和数据的消费进行分离
8. 支持同步和异步调用设备, 支持应用程序客户端和设备服务端和设备三端之间的同步和异步调用
#### 2. 已对接的设备(具体看设备仓库模块(devices))
1. 福州瑞邦断路器
2. 福州瑞邦智慧融合控制台
3. 道路运输车辆主动安全智能防控系统(GPS定位)
4. 7合一环境监测设备
5. 十七合一环境监测设备
6. 温湿度监测和空调红外控制设备
7. 中控智慧门禁BS版
8. 中央空调控制面板
...
