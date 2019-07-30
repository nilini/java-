*JAVA 基础扎实，对 JVM 有一定了解，能对 JVM 做性能分析及调优。
*熟悉Spring、SpringMVC、SpringBoot、SpringClound等spring家族框架，熟悉ORM框架：MyBatis、Hibernate。
*熟悉Redis的使用场景及原理，熟练搭建redis集群。
*熟悉sql语句，熟悉mysql索引优化、事务、锁，了解mysql的主从搭建、读写分离、分表等业务。
*熟练搭建Zookeeper，ActiveMq队列集群实现高可用。
*熟悉Nginx、Tomcat 部署和配置，熟悉 Nginx 负载均衡，Tomcat 调优。
*了解HTML/CSS、JavaScript、jQuery、Ajax等前端技术。
*熟练使用版本控制工具：svn、git。
*了解ElasticSearch。
*了解sql注入、XSS、JSONP注入等web安全漏洞。
*具备良好的代码编程习惯及文档编写能力。

项目
标广网络科技有限公司
1、中国标识网（www.sign-in-china.com）
    项目描述：
        中国标识网是一个面向国外用户的网上商城。前台主要包括商城首页、商品详情页、类目页面、营销页、购物车、个人中心、支付页面等。
    后台主要包括商品模块、订单模块、类目模块、物流模块、营销模块、用户模块、数据统计模块等。使用ssh开发。
    
    技术描述：
        1、采用maven构建项目，方便管理和部署。前后和后台分开部署，前台部署在两台服务器，后台和数据库部署在一台服务器上，
        2、为减轻服务器压力，使用nginx负载均衡将访问请求分发到两台tomcat应用服务器。
        3、使用redis缓存应对秒杀类促销活动，主要关键点，缓存促销活动商品信息到redis缓存，请求缓存到redis队列，异步处理请求队列中的秒杀请求。
        4、静态化了商城首页及促销活动页，以应对高并发请求。
        5、使用定时任务更新静态化页面，处理超时未支付、自动收货、短信余额预警等，并提供后台管理页面。
        6、对接了UPS、DHL、EMS、FedEx快递api，后台自动快递下单，追踪物流信息。
        7、对接了wish、ebay、amazon网购平台api，后台自动同步平台的订单。
皓之睿数字科技优先公司
1、云饰衣（pgy.yuns1.ne）

