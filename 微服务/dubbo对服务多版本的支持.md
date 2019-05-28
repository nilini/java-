####dubbo中对服务多版本的支持
当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。
在低压力时间段，先升级一半提供者为新版本，再将所有消费者，然后将剩下的一半提供者升级为新版本。
```xml
<dubbo:service interface="com.foo.BarService" version="1.0.0" />
<dubbo:service interface="com.foo.BarService" version="2.0.0" />
<dubbo:reference id="barService" interface="com.foo.BarService" version="1.0.0" />
<dubbo:reference id="barService" interface="com.foo.BarService" version="2.0.0" />

不区分版本：(2.2.0以上版本支持)

<dubbo:reference id="barService" interface="com.foo.BarService" version="*" />
```

#####示例
######服务提供方
```java
// 服务接口定义
package com.dubbosample.service;
public interface IDubbodoSomethingService {  
    public String doSomething();
}
```

```java
// 服务接口实现
package com.dubbosample.service.impl;
import com.dubbosample.service.IDubbodoSomethingService;
public class DubbodoSomething1ServiceImpl implements IDubbodoSomethingService {
    @Override
    public String doSomething() {
        logger.info( "v1 版本");
        return "----";
    }
}
```

```java
// 服务接口实现
package com.dubbosample.service.impl; 
import com.dubbosample.service.IDubbodoSomethingService;
public class DubbodoSomething2ServiceImpl implements IDubbodoSomethingService {
    @Override
    public String doSomething() { 
        logger.info( "v2 版本");
        return "*****";
    }
}
```

######spring配置
applicationContext.xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://code.alibabatech.com/schema/dubbo
           http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 配置Bean -->
    <bean id="serviceVersion1" class="com.dubbosample.service.impl.DubbodoSomething1ServiceImpl"/>
    <bean id="serviceVersion2" class="com.dubbosample.service.impl.DubbodoSomething2ServiceImpl"/>
    <!-- 引入配置文件 -->
    <import resource="classpath:dubbo.xml"/>
 </beans>
```

dubbo.xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans        
    http://www.springframework.org/schema/beans/spring-beans.xsd        
    http://code.alibabatech.com/schema/dubbo        
    http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 指定web服务名字 -->
    <dubbo:application name="DubbodoSomething"/>
    <!-- 声明服务注册中心 -->
    <dubbo:registry  protocol="zookeeper" address="127.0.0.1:2181"/>
    <!-- 指定传输层通信协议 -->
    <dubbo:protocol name="dubbo" port="20880"/>
    <dubbo:protocol name="rmi" port="1099"/>

    <!-- 暴露你的服务地址 -->
    <!-- 这里使用了版本配置 version -->
    <dubbo:service 
        ref="serviceVersion1" 
        interface="com.dubbosample.service.IDubbodoSomethingService"
        protocol="dubbo,rmi"
        version="1.0.0"
    />
     <dubbo:service 
        ref="serviceVersion2" 
        interface="com.dubbosample.service.IDubbodoSomethingService"
        protocol="dubbo,rmi"
        version="1.0.1"
    />
 </beans>
```
######客户端
```java
// 接口声明
package com.dubbosample.service;
public interface IDubbodoSomethingService {
    public String doSomething();
}
```

applicationContext.xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://code.alibabatech.com/schema/dubbo
            http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 引入配置文件 -->
    <import resource="classpath:dubbo.xml"/>
 </beans>
```

dubbo.xml配置
这里配置了服务的多版本实现引用，使用version
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans        
    http://www.springframework.org/schema/beans/spring-beans.xsd        
    http://code.alibabatech.com/schema/dubbo        
    http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 指定web服务名字 -->
    <dubbo:application name="DubbodoSomething"/>
    <!-- 声明服务注册中心 -->
    <dubbo:registry protocol="zookeeper" address="127.0.0.1:2181"/>

    <!-- 指定传输层通信协议 -->
    <dubbo:protocol name="dubbo" port="20881"/>
    <dubbo:protocol name="rmi" port="1010"/>

    <!-- 暴露你的服务地址 -->
    <dubbo:reference 
        id="serviceVersion1" 
        interface="com.dubbosample.service.IDubbodoSomethingService"
        protocol="dubbo"
        version="1.0.0"
    />
     <dubbo:reference 
        id="serviceVersion2" 
        interface="com.dubbosample.service.IDubbodoSomethingService"
        protocol="dubbo"
        version="1.0.1"
    />
    <dubbo:reference 
        id="serviceVersion3" 
        interface="com.dubbosample.service.IDubbodoSomethingService"
        protocol="dubbo"
        version="*"
    />
 </beans>

```

客户端代码
```java
package com.dubbosample.service;
import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext; 
public class DubboStart{
    public static void main(String[] args) throws IOException {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
        //访问 v1 版本
        IDubbodoSomethingService serviceVersion1=(IDubbodoSomethingService) ctx.getBean("serviceVersion1");
        System.out.println(serviceVersion1.doSomething());
        //访问 v2 版本
        IDubbodoSomethingService serviceVersion2=(IDubbodoSomethingService) ctx.getBean("serviceVersion2");
        System.out.println(serviceVersion2.doSomething());

        //访问随机版本
        IDubbodoSomethingService serviceVersion3=(IDubbodoSomethingService) ctx.getBean("serviceVersion3");
        System.out.println(serviceVersion3.doSomething());
    }
}
```