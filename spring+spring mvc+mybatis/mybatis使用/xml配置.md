#属性
* 典型的Java属性文件(condif.properties)中配置。
* 通过properties元素的子元素来传递。
```xml
<properties resource="org/mybatis/example/config.properties"><!--java属性文件-->
  <property name="username" value="dev_user"/>
  <property name="password" value="F2Fa3!33TYyg"/>
</properties>

<dataSource type="POOLED">
  <property name="driver" value="${driver}"/> <!-- java属性文件 -->
  <property name="url" value="${url}"/>  <!-- java属性文件 -->
  <property name="username" value="${username}"/>  <!-- properties元素的子元素 -->
  <property name="password" value="${password}"/>  <!-- properties元素的子元素 -->
</dataSource>
```
* 属性传递到SqlSessionFactoryBuilder.build()方法中。
    ```
    @Before
    public void init() throws IOException {
        // 定义mabatis全局配置文件
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(inputStream);
        // 根据sqlSessionFactory产生会话sqlsession
        session = factory.openSession();
    }

    ```
    通过方法参数传递的属性具有最高优先级，resource/url属性中指定的配置文件次之，最低优先级的是properties属性中指定的属性。
* 设置默认值
```
<properties resource="org/mybatis/example/config.properties">
  <!-- ... -->
  <property name="org.apache.ibatis.parsing.PropertyParser.default-value-separator" value="?:"/> <!-- 启动默认值特性，并修改默认值的分隔符 -->
</properties>
<dataSource type="POOLED">
  <!-- ... -->
  <property name="username" value="${db:username?:ut_user}"/>
</dataSource>
```

#设置
可以改变MyBatis的运行时行为。
```xml
<settings>
  <setting name="cacheEnabled" value="true"/>
  <setting name="lazyLoadingEnabled" value="true"/>
  <setting name="multipleResultSetsEnabled" value="true"/>
  <setting name="useColumnLabel" value="true"/>
  <setting name="useGeneratedKeys" value="false"/>
  <setting name="autoMappingBehavior" value="PARTIAL"/>
  <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
  <setting name="defaultExecutorType" value="SIMPLE"/>
  <setting name="defaultStatementTimeout" value="25"/>
  <setting name="defaultFetchSize" value="100"/>
  <setting name="safeRowBoundsEnabled" value="false"/>
  <setting name="mapUnderscoreToCamelCase" value="false"/>
  <setting name="localCacheScope" value="SESSION"/>
  <setting name="jdbcTypeForNull" value="OTHER"/>
  <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
</settings>
```
* cacheEnabled  全局开启或关闭配置文件中的所有映射器已经配置的任何缓存。
* lazyLoadingEnabled 延迟加载的全局开关。特定关联关系中可通过设置fetchType属性来覆盖该项的开关状态。
    resultMap可以实现高级映射（使用association、collection实现一对一及一对多映射），association、collection具备延迟加载功能。
    延迟加载：先从单表查询、需要时再从关联表去关联查询。
* aggressiveLazyLoading 当开启时，任何方法的调用都会加载该对象的所有属性，否则，每个属性会按需加载。
    1、直接加载
        执行完主对象之后，直接执行关联对象。
    2、侵入式加载（）
        在执行主对象详情的时候，执行关联对象。
        lazyLoadingEnabled=true
        aggressiveLazyLoading=true
    3、深度延迟加载
        执行完主对象或主对象详情不会执行关联对象，只有用到关联对象数据的时候才走深度延迟加载。
        lazyLoadingEnabled=true
        aggressiveLazyLoading=false
* lazyLoadTriggerMethods 指定哪个对象的方法触发一次延迟加载。用逗号分隔的方法列表。
    默认equals, clone, hashCode, toString。
    这些方法调用后，所有延迟加载属性的关联对象被执行加载。
* multipleResultSetsEnabled 是否允许单一语句返回多结果集 (需要驱动支持). 默认true.
* useGeneratedKeys 允许JDBC自动生成主键,需要驱动支持.