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
* useColumnLable 使用列标签代替列名，默认true
  ```xml
  select name as alise_name from user;
  开启后mybatis会自动把表中大的name的值赋到对应实体的alise_name属性中。
  ```

* useGeneratedKeys 允许JDBC自动生成主键,需要驱动支持.
* autoMappingBehavior 指定MyBatis应如何自动映射列到字段或属性。
  * NONE 表示取消自动映射；
  * PARTIAL只会自动映射没有定义嵌套结果集映射的结果集;
  * FULL会自动映射任意复杂的结果集（无论是否嵌套）。默认 PARTIAL。
  ```xml
  <resultMap id="FeelingCommentResult" type="Feeling">
    <id property="feeling_id" column="feeling_id" />
    <!-- <result property="content" column="content" /> --><!--注释1-->
    <collection property="feelingComments" ofType="FeelingComment">
      <id property="feeling_comment_id" column="feeling_comment_id" />
      <!-- <result property="commentContent" column="commentContent" /> --> <!--注释2-->
    </collection>
  </resultMap>
  <select id="selectFeelingComment" parameterType="map" resultMap="FeelingCommentResult">
    select * from feeling left outer join feeling_comment on feeling.feeling_id=feeling_comment.feeling_id where feeling.id =#{id}
  </select>
  ```
  自动映射注释2，则要设置为FULL；自动映射注释1，则要设置PARTIAL；NONE则只会映射两个\<id\>标签。
* autoMappingUnknownColumnBehavior	指定发现自动映射目标未知列（或者未知属性类型）的行为。
  * NONE: 不做任何反应
  * WARNING: 输出提醒日志 ('org.apache.ibatis.session.AutoMappingUnknownColumnBehavior' 的日志等级必须设置为 WARN)
  * FAILING: 映射失败 (抛出 SqlSessionException)
  ```
  <!-- 实体类属性和数据表字符字段有一个有不存在的情况。 -->
  <id property="feeling_comment_id" column="feeling_comment_id" />
  ```
* defaultExecutorType SIMPLE就是普通的执行器；REUSE执行器会重用预处理语句（prepared statements）; BATCH 执行器将重用语句并执行此批量更新。
  ```xml
  设为"SIMPLE", 在执行bookDao.save(book)时，就相当于JDBC的stmt.execute(sql)；
  设为"REUSE", 在执行bookDao.save(book)时，相当于JDBC重用一条sql，再通过stmt传入多项参数值，然后执行stmt.executeUpdate()或stmt.executeBatch()；
  设为"BATCH", 在执行bookDao.save(book)时，相当于JDBC语句的 stmt.addBatch(sql)，即仅仅是将执行SQL加入到批量计划。 所以此时不会抛出主键冲突等运行时异常，而只有临近commit前执行stmt.execteBatch()后才会抛出异常。
  Mybatis有三种基本的Executor执行器:

  SimpleExecutor、ReuseExecutor、BatchExecutor。
  SimpleExecutor：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。
  ReuseExecutor：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map内，供下一次使用。简言之，就是重复使用Statement对象。
  BatchExecutor：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。
  ```

*  defaultStatementTimeout	设置超时时间，它决定驱动等待数据库响应的秒数。
    * socket timeout
    JDBC通过socket对字节流进行处理。
    http://www.importnew.com/2466.html
    ```java
    // mysql设置socket连接和读写时的timeout
    jdbc:mysql://xxx.xx.xxx.xxx:3306/database?connectTimeout=60000&socketTimeout=60000
    connectTimeout和socketTimeout的默认值为0时，timeout不生效。操作系统可以设置socket timeout。
    通常应用会在调用Socket.read()时由于网络问题被阻塞住，很少在调用Socket.write()时进入waitting状态。
    当Socket.write()被调用时，数据被写入到操作系统内核的缓冲区，控制权立即回到应用手上。但是，如果系统内核缓冲区由于某种网络错误而满了的话，Socket.write()也会进入到waitting状态。
    在这种情况下，操作系统会尝试重新发包，当达到重试的时间限制时，将产生系统错误。
    ```
    * transaction timeout
      transaction timeou 一般存在与框架（Spring，EJB）或应用级别。
      就是 statement timeout * N(需要执行的statement数量) + 垃圾回收等其他时间。
      transaction timeout用来限制执行statement的总时长。
      spring中可以使用xml或在源码中使用@Transaction注解来进行设置。
      ```xml
      <tx:attributes>  
        <tx:method name=“…” timeout=“3″/>  
      </tx:attributes>  
      ```
      假设某个事务中包含5个statement，每个statement的执行时间是200ms，其他业务逻辑的执行时间是100ms，那么transaction timeout至少应该设置为1,100ms（200 * 5 + 100）。 
    * statement timeout
      statement timeout是用来限制statement的执行时长。
      ```
      Oracle JDBC Statement的QueryTimeout处理过程 
      1. 通过调用Connection的createStatement()方法创建statement 
      2. 调用Statement的executeQuery()方法 
      3. statement通过自身connection将query发送给Oracle数据库 
      4. statement在OracleTimeoutPollingThread（每个classloader一个）上进行注册 
      5. 达到超时时间 
      6. OracleTimeoutPollingThread调用OracleStatement的cancel()方法 
      7. 通过connection向正在执行的query发送cancel消息 
      
      MySQL JDBC Statement的QueryTimeout处理过程 
      1. 通过调用Connection的createStatement()方法创建statement 
      2. 调用Statement的executeQuery()方法 
      3. statement通过自身connection将query发送给MySQL数据库 
      4. statement创建一个新的timeout-execution线程用于超时处理 
      5. 5.1版本后改为每个connection分配一个timeout-execution线程 
      6. 向timeout-execution线程进行注册 
      7. 达到超时时间 
      6. TimerThread调用JtdsStatement实例中的TsdCore.cancel()方法 
      7. timeout-execution线程创建一个和statement配置相同的connection 
      8. 使用新创建的connection向超时query发送cancel query（KILL QUERY “connectionId”） 
      ```
* defaultFetchSize 为驱动的结果集获取数量（fetchSize）设置一个提示值。此参数可以在查询设置中被覆盖。
      ```xml
      mysql不支持fethsize, 会一次取出所有数据到客户端内存中。数据量较大时可能会出现JVM OOM。
      解决办法：
      1、当statement设置以下属性时，采用的时流数据接收方式，每次只从服务器接收部分数据，直到所有数据处理完。
        setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
        setFetchSize(Integer.MIN_VALUE);
      2、调用statement的enableStreamingResults方法，实际上enableStreamingResults方法内部封装的就是第一种方式。
      3、设置连接属性useCursorFetch = true（5.0版驱动开始支持），statement以TYPE_FORWARD_ONLY打开，再设置fetch size参数，表示采用服务器端游标，每次从服务器取fetch_size条数据。 
      ```
      ```
      Oracle/DB2都有方便的游标机制。
      Mysql的Stream方式有2种，Client Side Cursor 和 Server Side Cursor，JDBC默认的方式是Client Side Cursor，
      即全部读取到Client Side后再处理。
      1、Client Side
      public void enableStreamingResults() throws SQLException {
          synchronized (checkClosed().getConnectionMutex()) {
              this.originalResultSetType = this.resultSetType;
              this.originalFetchSize = this.fetchSize;
              setFetchSize(Integer.MIN_VALUE);
              setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
          }
      }
      mysql本身并没有FetchSize方法，它是通过使用CS阻塞方式的网络流控制实现服务端不会一下发送大量数据到客户端。
      会造成问题：如果没有全部读取完ResultSet的结果再执行其他sql，那么将会影响连接的缓存，所以这种方式要求要么读取完
      ResultSet中的全部数据，要么需要自己调用ResultSet.close()方法。

      MyBatis中需要如下设置：
      <select id="findAllRoles" fetchSize="-2147483648" resultType="chenlong.mybatislearn.db.struct.Role">
        SELECT * FROM role
      </select>

      2、Server Side Cursor
        <property name="url" value="jdbc:mysql://localhost:3008/mybatislearn?autoReconnect=true&amp;useCursorFetch=true"/>
      ```
>>safeResultHandlerEnabled 	允许在嵌套语句中使用分页（ResultHandler）。如果允许使用则设置为 false。 
>>safeResultHandlerEnabled 	允许在嵌套语句中使用分页（ResultHandler）。如果允许使用则设置为 false。
* mapUnderscoreToCamelCase 是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。默认false。
* localCacheScope MyBatis 利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。 默认值为 SESSION，这种情况下会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不会共享数据。
* jdbcTypeForNull 当没有为参数提供特定的JDBC类型时，为空值指定JDBC类型。某些驱动需要指定列的JDBC类型，多数情况下直接用一般类型即可，比如NULL、VARCHAR或OTHER。
* lazyLoadTriggerMethods 指定哪个对象的方法触发一次延迟加载。 值：用逗号分割的方法列表。 默认：equals、clone、hashCode、toString
      ```xml
      <!-- 调用doLazyLoadingNow()触发懒加载。 -->
      <setting  name="lazyLoadTriggerMethods" value="doLazyLoadingNow,equals,clone,hashCode,toString" />
      ```
* defaultScriptingLanguage 指定动态sql生成的默认语言。 值：一个类型别名或完全限定名。 默认值：org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
  ```xml
  <!-- 自定义sql解析 -->
  <typeAliases>
    <typeAlias type="org.sample.MyLanguageDriver" alias="myLanguage"/>
  </typeAliases>
  <settings>
    <setting name="defaultScriptingLanguage" value="myLanguage"/>
  </settings>
  ```
* defaultEnumTypeHandler 指定Enum使用的默认 TypeHandler。值：一个类型别名或完全限定名。 默认值：org.apache.ibatis.type.EnumTypeHandler
  * 无论是Mybatis在预处理语句中设置一个参数时，还是从结果集中取出一个值时，都会用类型处理器将获取的值以合适的方式转换成java类型。MyBatis默认为我们实现了许多TypeHandler,当没有配置指定TypeHandler时，MyBatis会根据参数或返回结果的不同，默认为我们选择合适的TypeHandler处理。
  * 对于enum而言，在MyBatis中已经存在EnumTypeHandler和EnumOrdinalTypeHandler两大处理器，他们都继承自BaseTypeHandler处理器。
  * EnumTypeHandler存入数据库的是枚举的name，EnumOrdinalTypeHandler存入数据库的是枚举的位置。
    ```java
    //例如下方的枚举，当我们有一个枚举值是EStatus.init时，这时我们使用mybatis EnumTypeHandler存入数据库的是"init"字符串；而EnumOrdinalTypeHandler存入的是3,因为init是第四个值，第一个值disable的index是0。
    public enum EStatus {
      disable("0"), enable("1"), deleted("2"),
      init("10"), start("11"), wait("12"), end("13");
    }
    ```
    ```xml
    <typeHandlers>
      <typeHandler handler="org.apache.ibatis.type.EnumOrdinalTypeHandler" javaType="com.foo.ProcessStatus"/>
    </typeHandlers>
    ```
    混合使用
    ```xml
    <insert id="insert" parameterType="com.foo.Process">
      insert into process (id, name, status)
      values (#{id}, #{name}, #{status, typeHandler=org.apache.ibatis.type.EnumTypeHandler})
    </insert>
    <!-- 或 -->
    <resultMap id="processMap" type="com.foo.Process">
        <id column="id" property="id"/>
        <id column="name" property="name"/>
        <id column="status" property="status" typeHandler="org.apache.ibatis.type.EnumOrdinalTypeHandler"/>
    </resultMap>
        
    <select id="findById" resultMap="processMap">
        select * FROM process WHERE id=#{id}
    </select>
    ```
    自定义枚举handler:https://segmentfault.com/a/1190000012564028

* callSettersOnNulls
  * mybatis会忽略掉值为null的字段。这样就造成取参数时报空指针异常的情况。如果设置了这条属性之后，mybatis就不会忽略这些字段，你依然能get到这些key，只不过value为null，这样也方便。
  * 问题：1、如果整个查询的所有字段都没有值，也就是查询到0条记录时，则接受到的map是一个所有key都是null的map，而不是一个为null的map。
  * 问题：2、如果只查询一个字段，而用map接收，而此时为null，则会接收到一个为null的map。默认false。
* returnInstanceForEmptyRow
  当返回行的所有列都是空时，MyBatis默认返回null。开启后，MyBatis会返回一个空实例。
* logPrefix 指定MyBatis增加到日志名称的前缀。
* logImpl 指定MyBatis所用日志的具体实现，未指定指定时将自动查找。
