# BaaS服务说明

## 1.技术栈
+ Spring Boot
+ Nacos
  + Config Center
  + Service Discovery

## 2.版本说明

| 依赖           | 版本                |
|--------------|-------------------|
| Spring Boot  | 2.3.1.RELEASE     |
| 1            | 1                 |

## 3.版本兼容说明
### 3.1 Nacos
Nacos 2.0.0版本与Nacos 1.X版本使用完全一致。
+ 服务端
配置中心兼容支持Nacos1.0起所有版本的客户端，服务发现兼容Nacos1.2起所有版本客户端。

QOS配置
Qos=Quality of Service，qos是Dubbo的在线运维命令，可以对服务进行动态的配置、控制及查询，Dubboo2.5.8新版本重构了telnet（telnet是从Dubbo2.0.5开始支持的）模块，提供了新的telnet命令支持，新版本的telnet端口与dubbo协议的端口是不同的端口，默认为22222，可以通过配置文件dubbo.properties修改。telnet 模块现在同时支持 http 协议和 telnet 协议，方便各种情况的使用。
QoS提供了一些启动参数，来对启动进行配置，他们主要包括：
https://blog.csdn.net/u012988901/article/details/84503672