# mytool
工具箱，封装或者集成一些常用的工具，最大程度降低使用门槛。

## 碰到的问题
1. 限流组件--SentinelWebAutoConfiguration循环依赖问题 <br/>
异常信息:
```text
***************************
APPLICATION FAILED TO START
***************************

Description:

The dependencies of some of the beans in the application context form a cycle:

   org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration
┌─────┐
|  com.alibaba.cloud.sentinel.SentinelWebAutoConfiguration (field private java.util.Optional com.alibaba.cloud.sentinel.SentinelWebAutoConfiguration.sentinelWebInterceptorOptional)
└─────┘


Action:

Relying upon circular references is discouraged and they are prohibited by default. Update your application to remove the dependency cycle between beans. As a last resort, it may be possible to break the cycle automatically by setting spring.main.allow-circular-references to true.


Process finished with exit code 1
```
由于springboot的版本和spring-cloud-alibaba-dependencie不匹配造成的。<br/>
```text
https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E
```