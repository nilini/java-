1.你如何理解Spring?

具体来说Spring是一个轻量级的容器，用于管理业务相关对象的。核心功能主要为：IOC,AOP,MVC。

IOD：控制反转，将对象的创建过程交给容器，让容器管理对象的生命周期如创建，初始化，销毁等。

AOP：面向切面编程，对关注点进行模块化，通过对某一功能点进行编程，比如记录日志，有很多个类都需要记录日志的方法，则创建记录日志的代理方法，需要调用该功能是只需要调用代理方法，这就是AOP。

MVC:SpringMvc,Spring提供的基于MVC模式设计的Web框架，如今比较流行的框架之一。

2.spring配置bean实例化有哪些方式？

    1）使用类构造器实例化(默认无参数)

<bean id="bean1" class="cn.itcast.spring.b_instance.Bean1"></bean>
    2）使用静态工厂方法实例化(简单工厂模式)
//下面这段配置的含义：调用Bean2Factory的getBean2方法得到bean2
<bean id="bean2" class="cn.itcast.spring.b_instance.Bean2Factory" factory-method="getBean2"></bean>
    3）使用实例工厂方法实例化(工厂方法模式)
//先创建工厂实例bean3Facory，再通过工厂实例创建目标bean实例
<bean id="bean3Factory" class="cn.itcast.spring.b_instance.Bean3Factory"></bean>
<bean id="bean3" factory-bean="bean3Factory" factory-method="getBean3"></bean>


3.介绍一下Spring的事物管理
    事务就是对一系列的数据库操作（比如插入多条数据）进行统一的提交或回滚操作，如果插入成功，那么一起成功，如果中间有一条出现异常，那么回滚之前的所有操作。这样可以防止出现脏数据，防止数据库数据出现问题。

开发中为了避免这种情况一般都会进行事务管理。Spring中也有自己的事务管理机制，一般是使用TransactionMananger进行管 理，可以通过Spring的注入来完成此功能。


Spring支持如下两种方式的事务管理：

编程式事务管理：这意味着你可以通过编程的方式管理事务，这种方式带来了很大的灵活性，但很难维护。

声明式事务管理：这种方式意味着你可以将事务管理和业务代码分离。你只需要通过注解或者XML配置管理事务。

一般选择声明式事务管理，因为这种方式和应用程序的关联较少。

4.Bean注入属性有哪几种方式？

![dd](./assets/20170607091428631.png)

5.讲述SpringMVC工作流程



流程 
1、用户发送请求至前端控制器DispatcherServlet 
2、DispatcherServlet收到请求调用HandlerMapping处理器映射器。 
3、处理器映射器找到具体的处理器，生成处理器对象及处理器拦截器(如果有则生成)一并返回给DispatcherServlet。 
4、DispatcherServlet调用HandlerAdapter处理器适配器 
5、HandlerAdapter经过适配调用具体的处理器(Controller，也叫后端控制器)。 
6、Controller执行完成返回ModelAndView 
7、HandlerAdapter将controller执行结果ModelAndView返回给DispatcherServlet 
8、DispatcherServlet将ModelAndView传给ViewReslover视图解析器 
9、ViewReslover解析后返回具体View 
10、DispatcherServlet根据View进行渲染视图（即将模型数据填充至视图中）。 
11、DispatcherServlet响应用户




6.SpringMVC与Struts2的主要区别？
①springmvc的入口是一个servlet即前端控制器，而struts2入口是一个filter过虑器。
②springmvc是基于方法开发，传递参数是通过方法形参，可以设计为单例或多例(建议单例)，struts2是基于类开发，传递参数是通过类的属性，只能设计为多例。 
③Struts采用值栈存储请求和响应的数据，通过OGNL存取数据， springmvc通过参数解析器是将request对象内容进行解析成方法形参，将响应数据和页面封装成ModelAndView对象，最后又将模型数据通过request对象传输到页面。 Jsp视图解析器默认使用jstl。

7.Spring中用到哪些设计模式？
1.工厂模式（BeanFactory中）
2.单例模式（Spring中默认bean为单例）
3.适配器模式（HandlerAdater）
4.装饰者模式
5.代理模式（AOP中用到JDK动态代理）
6.观察者模式（listener的实现，例如ApplicationListener）
7.策略模式（定义一系列的算法，把它们一个个的封装起来，并且使它们可以相互替换。在实例化对象时用到）
8.模板模式（jdbcTemplate）
)



(1) Spring在SSM起什么作用

Spring是一个轻量级框架，也是一个容器，Spring实质上讲就是一个Bean工厂，主要用来管理Bean的生命周期和框架集成。有IOC控制反转，DI依赖注入，控制反转是把dao依赖注入到servic层，然后service层反转给action层，Spring的顶层容器为BeanFactory，常用的ApplicationContext为它的子接口，实现了工厂模式，Spring还提供了AOP的支持，方便在切面级开发，
(2) 怎么样理解IOC和DI
在使用Spring框架的过程中、一定会用到控制反转、但是往往所需要的资源还需要其他资源的支持、个过程就需要依赖注入的支持
(3)Spring的事务，事务的作用。
• 编程式事务管理：这意味你通过编程的方式管理事务，给你带来极大的灵活性，但是难维护。
• 声明式事务管理：这意味着你可以将业务代码和事务管理分离，你只需用注解和XML配置来管理事务。
(3) Spring的IOC你在项目中是怎么使用的？
• IOC主要来解决对象之间的依赖问题,把所有的bean的依赖关系通过配置文件或者注解关联起来,降低了耦合度
(5)Spring的配置文件有哪些内容？
• 开启事务注解驱动
• 事务管理器
• 开启注解功能，并配置扫描包
• 配置数据源
• 配置SQL会话工厂、别名、映射文件
• 不用编写DAO层的实现类（代理模式）
(6)说下Spring的注解
• @Controller
• @Service
• @Component
• @RequestMapping
• @Resource、@Autowired
• @ResponseBody
• @Transactional

(7)为什么要用Spring
• 降低对象耦合度,让代码更加清晰,提供一些常见的模版
(8)Spring DI的几种方式
• (1)构造器注入：通过构造方法初始化
• <constructor-arg name="dao"</constructor-arg>
• (2)setter注入：通过setter方法初始化注入
• <property name="dao" ref="dao2"></property>

• 注意：在实际开发中常用setter注入。

(7)为什么要用Spring
• 降低对象耦合度,让代码更加清晰,提供一些常见的模版
(8)Spring DI的几种方式
• (1)构造器注入：通过构造方法初始化
• <constructor-arg name="dao"</constructor-arg>
• (2)setter注入：通过setter方法初始化注入
• <property name="dao" ref="dao2"></property>
• 注意：在实际开发中常用setter注入。
(1)SpringMvc的控制器是不是单例模式,如果是,有什么问题,怎么解决
是单例模式,所以在多线程访问的时候有线程安全问题,不要用同步,会影响性能的,解决方案是在控制器里面不能写字段
(2)SpingMvc中的控制器的注解
@Controller 注解：该注解表明该类扮演控制器的角色，Spring不需要你继承任何其他控制器基类或引用Servlet API。
(3)@RequestMapping注解用在类上面有什么作用
该注解是用来映射一个URL到一个类或一个特定的方处理法上。
(4) 我想在拦截的方法里面得到从前台传入的参数,怎么得到
直接在形参里面声明这个参数就可以,但必须名字和传过来的参数一样
(5)如果前台有很多个参数传入,并且这些参数都是一个对象的,那么怎么样快速得到这个对象
直接在方法中声明这个对象,SpringMvc就自动会把属性赋值到这个对象里面
(6)SpringMvc中函数的返回值是什么
返回值可以有很多类型,有String, ModelAndView,List,Set等,一般用String比较好,如果是AJAX请求,返回的可以是一个集合
(7)SpringMvc怎么处理返回值的
SpringMvc根据配置文件中InternalResourceViewResolver(内部资源视图解析器)的前缀和后缀,用前缀+返回值+后缀组成完整的返回值
(8)SpringMVC怎么样设定重定向和转发的
在返回值前面加"forward:"就可以让结果转发,譬如"forward:user.do?name=method4" 在返回值前面加"redirect:"就可以让返回值重定向,譬如"redirect:http://www.uu456.com"
(9)SpringMvc中有个类把视图和数据都合并的一起的,叫什么
ModelAndView
(10)怎么样把数据放入Session里面
可以声明一个request,或者session先拿到session,然后就可以放入数据,或者可以在类上面加上@SessionAttributes注解,里面包含的字符串就是要放入session里面的key
(11)SpringMvc怎么和AJAX相互调用的
通过Jackson框架就可以把Java里面的对象直接转化成js可以识别的Json对象 具体步骤如下 :
1.加入Jackson.jar
2.在配置文件中配置json的映射
3.在接受Ajax方法里面可以直接返回Object,List等,但方法前面要加上@ResponseBody注解
(12)当一个方法向AJAX返回对象,譬如Object,List等,需要做什么处理
要加上@ResponseBody注解
(13)讲下SpringMvc的执行流程
系统启动的时候根据配置文件创建spring的容器, 首先是发送http请求到核心控制器DispatcherServlet，spring容器通过映射器去寻找业务控制器，
使用适配器找到相应的业务类，在进业务类时进行数据封装，在封装前可能会涉及到类型转换，执行完业务类后使用ModelAndView进行视图转发，
数据放在model中，用map传递数据进行页面显示。
(1)什么是MyBatis的接口绑定,有什么好处
接口映射就是在IBatis中任意定义接口,然后把接口里面的方法和SQL语句绑定,我们直接调用接口方法就可以,这样比起原来了SqlSession提供的方法我们可以有更加灵活的选择和设置.
(2)什么情况下用注解绑定,什么情况下用xml绑定
当Sql语句比较简单时候,用注解绑定,当SQL语句比较复杂时候,用xml绑定,一般用xml绑定的比较多
(3)如果要查询的表名和返回的实体Bean对象不一致,那你是怎么处理的?
在MyBatis里面最主要最灵活的的一个映射对象的ResultMap,在它里面可以映射键值对, 默认里面有id节点,result节点,它可以映射表里面的列名和对象里面的字段名. 并且在一对一,一对多的情况下结果集也一定要用ResultMap
(4)MyBatis在核心处理类叫什么
MyBatis里面的核心处理类叫做SqlSession
(5)讲下MyBatis的缓存
MyBatis的缓存分为一级缓存和二级缓存,一级缓存放在session里面,默认就有,二级缓存放在它的命名空间里,
默认是打开的,使用二级缓存属性类需要实现Serializable序列化接口(可用来保存对象的状态),可在它的映射文件中配置<cache/>
(6)MyBatis(IBatis)的好处是什么
ibatis把sql语句从Java源程序中独立出来，放在单独的XML文件中编写，给程序的
维护带来了很大便利。
ibatis封装了底层JDBC API的调用细节，并能自动将结果集转换成JavaBean对象，大大简化了Java数据库编程的重复工作。
因为Ibatis需要程序员自己去编写sql语句，程序员可以结合数据库自身的特点灵活控制sql语句，
因此能够实现比hibernate等全自动orm框架更高的查询效率，能够完成复杂查询。
(7)MyBatis怎么配置一对多？
一对多的关系 ：property: 指的是集合属性的值, ofType：指的是集合中元素的类型
(8)MyBatis怎样配置多对一？
多对一的关系：property: 指的是属性的值, javaType：指的是属性的类型

--------------------- 
作者：猿人类丿 
来源：CSDN 
原文：https://blog.csdn.net/weixin_39801925/article/details/80585758 
版权声明：本文为博主原创文章，转载请附上博文链接！