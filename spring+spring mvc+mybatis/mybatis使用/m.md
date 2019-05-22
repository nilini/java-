####1、动态SQL
#####if
只会使用一个if。
```xml
<select id="findBlog" resultType="Blog">
    SELECT * FROM BLOG WHERE state = 'ACTIVE'
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="name != null">
        AND name like #{name}
    </if>
</select>
```

#####choose, when, otherwise
可以使用多个。
```xml
<select id="findBlog" resultType="Blog">
    SELECT * FROM BLOG WHERE state = 'ACTIVE'
    <choose>
        <when test="title != null">
            AND title like #{title}
        </when>
        <when test="name != null">
            AND name like #{name}
        </when>
        <otherwise>
            AND featured = 1
        </otherwise>
    </choose>
</select>
```
#####trim, where, set
只使用if，会出现select * from blog where;的情况。
加上where即可解决，如果有if满足才会插入“where”。并且，where后面有"and"或"or",会被清除。
```xml
<select id="findBlog" resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```
还可以自定义清除的元素。
```xml
<trim prefix="WHERE" prefixOverrides="AND | OR">
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
</trims>
```
用于更新语句的解决方案是set。set元素会设置前置“SET”关键字，同事删除无关的逗号。
```xml
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```
set元素等价的自定义trim元素。添加前缀“set”, 删除后缀逗号。
```xml
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
```
#####foreach
 你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象传递给 foreach 作为集合参数。
 当使用可迭代对象或者数组时，index 是当前迭代的次数，item 的值是本次迭代获取的元素。
 当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。
```xml
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *  FROM POST P  WHERE ID in
  <foreach item="item" index="index" collection="list" open="(" separator="," close=")">
        #{item}
  </foreach>
</select>
```
#####bind
bind 元素可以从 OGNL 表达式中创建一个变量并将其绑定到上下文。比如：
```xml
<select id="selectBlogsLike" resultType="Blog">
    <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
    SELECT * FROM BLOG WHERE title LIKE #{pattern}
</select>
```
#####多数据库支持
一个配置了“_databaseId”变量的 databaseIdProvider 可用于动态代码中，这样就可以根据不同的数据库厂商构建特定的语句.
```xml
<insert id="insert">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    <if test="_databaseId == 'oracle'">
      select seq_users.nextval from dual
    </if>
    <if test="_databaseId == 'db2'">
      select nextval for seq_users from sysibm.sysdummy1"
    </if>
  </selectKey>
  insert into users values (#{id}, #{name})
</insert>
```

####2、XML映射文件
####select
```xml
<select id="selectPerson" paramterType="int" resultType="hashmap">
    SELECT * FROM PERSON WHERE ID = #{id}
</select>
```
```xml
* id 在命名空间中唯一的标识符，可以被用来引用这条语句。
* paramterType 将会传入这条语句的参数类的完全限定名或别名。这个属性是可选的，因为 MyBatis 可以通过类型处理器（TypeHandler） 推断出具体传入语句的参数，默认值为未设置（unset）。
* resultType 返回期望类型的类的完全限定名或别名。如果返回的是集合，应该设置为集合包含的类型，而不是集合本身。resultType和resultMap不能同时使用。
* resultMap 外部resultMap的命名引用。
* flushCache 设置为true后，只要语句被调用，都会导致本地缓存和二级缓存被清空。默认false。
* useCache 设置为true后，将会导致本条语句的结果被二级缓存缓存起来。默认：对select元素为true。
* timeout 设个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的描述。默认值为未设置（unset）(依赖驱动)。
* fetchSize 尝试让驱动程序每次批量返回的结果行数和这个设置值相等。默认为设置（unset）(依赖驱动)。
* statementType	STATEMENT，PREPARED 或 CALLABLE 中的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。
* resultSetType	FORWARD_ONLY，SCROLL_SENSITIVE, SCROLL_INSENSITIVE 或 DEFAULT（等价于 unset） 中的一个，默认值为 unset （依赖驱动）。
* databaseId	如果配置了数据库厂商标识（databaseIdProvider），MyBatis 会加载所有的不带 databaseId 或匹配当前 databaseId 的语句；如果带或者不带的语句都有，则不带的会被忽略。
* resultOrdered	这个设置仅针对嵌套结果 select 语句适用：如果为 true，就是假设包含了嵌套结果集或是分组，这样的话当返回一个主结果行的时候，就不会发生有对前面结果集的引用的情况。 这就使得在获取嵌套的结果集的时候不至于导致内存不够用。默认值：false。
* resultSets	这个设置仅对多结果集的情况适用。它将列出语句执行后返回的结果集并给每个结果集一个名称，名称是逗号分隔的。
```

#####id：
* xml（mapper）文件的namespace关联接口名。
  ```xml
  <mapper namespace="com.pgy.dao.UserDao">
    <select id="select" resultType="User">
        SELECT id,ch_name,name,group_id from sys_user
    </select>
  </mapper>
  ```
* 接口
```java
public interface UserDao {

    /**
     * 根据查询条件查询用户列表
     * @param user 查询条件
     * @return 用户列表
     */
    List<User> select(User user);
}

```
使用
```java
List<User> list = userDao.select(user);
```
######paramterType:
```xml
<delete id="deleteId" parameterType="Integer">
<insert id="addEmp" parameterType="com.entity.Employee">
<!-- 下面是paremeterType是Map（map.put("id", 3)），resultType返回值是List<Teacher> -->
<select id="selectTeacher" parameterType="Map" resultType="com.entity.Teacher">
  select * from Teacher where id = #{id} 
</select>
```
######resultMap:
```xml
<resultMap type="org.apache.ibatis.submitted.rounding.User" id="usermap">
		<id column="id" property="id"/>
		<result column="name" property="name"/>
		<result column="funkyNumber" property="funkyNumber"/>
		<result column="roundingMode" property="roundingMode"/>
</resultMap>
<select id="getUser" resultMap="usermap">
		select * from users
</select>
```
######flushCache:
*  一级缓存是SqlSession级别的缓存, 一个SqlSession结束后该SqlSession中的一级缓存也就不存在了, 不同的SqlSession之间的缓存数据区域是互不影响的.
* 二级缓存是Mapper级别的缓存, 跨SqlSession, 共享域是mapper的同一个namespace, 默认没开启,需要在setting全局配置中开启.
######useCache:
* 当为select语句时：
  flushCache默认为false，表示任何时候语句被调用，都不会去清空本地缓存和二级缓存。
  useCache默认为true，表示会将本条语句的结果进行二级缓存。

  当为insert、update、delete语句时：
  flushCache默认为true，表示任何时候语句被调用，都会导致本地缓存和二级缓存被清空。
  useCache属性在该情况下没有。
######timeout:
```xml 
  <!-- 配置文件中全局设置超时。sql执行时间超过300秒时，报错（Statement cancelled due to timeout or client request） -->
  <configuration>
    <settings>
        <setting name="defaultStatementTimeout" value="300" />
    </settings>
  </configuration>
  <!-- 映射文件中设置，有些sql需要长时间执行。 -->
  <select id="queryList" parameterType="hashmap" timeout="10000">
```
######fetchSize
* 尝试让驱动每次批量返回的结果行数和这个设置值相等。
* Mysql不支持fetchsize，默认为一次性取出所有数据，所以容易OOM。
* Oracle支持。

######statementType
设置使用什么对象操作sql语句。
* STATEMENT：直接操作sql，不进行预编译，获取数据：Statement
* PREPARED（默认）：预处理，先预编译。获取数据：PreparedStatement
* CALLABLE：执行存储过程，CallableStatement
```xml
<update id="update4" statementType="STATEMENT">
    update tb_car set price=${price} where id=${id}
</update>

<update id="update5" statementType="PREPARED">
    update tb_car set xh=#{xh} where id=#{id}
</update>

<!--存储过程-->
<select id="getUserCount" parameterMap="getUserCountMap" statementType="CALLABLE">
    CALL mybatis.ges_user_count(?,?)
</select>
```
######resultSetType
* jdbc中resultSetType的可选值有：ResultSet.TYPE_FORWARD_ONLY、ResultType.TYPE_SCROLL_INSENSITIVE、ResultSet.TYPE_SCROLL_SENSITIVE
* ResultSet.TYPE_FORWORD_ONLY
  默认的cursor类型，仅仅支持结果集forword，不支持backforward、random、last、first等操作。
* ResultSet.TYPE_SCROLL_INSENSITIVE
  支持结果集backforward，random，last，first等操作。对其它session对数据库中数据做出的更改是不敏感的。
  实现方法：从数据库取出数据后，会把全部数据缓存到cache中，对结果集的后续操作，是操作的cache中的数据，数据库中记录发生变化后，不影响cache中的数据，
* ResultSet.TYPE_SCROLL_SENSITIVE
  支持结果集backforward、random、last、first等操作，对其它session对数据库中数据做出的更改是敏感的，即其他session修改了数据库中的数据，会反应到本结果集中。
  实现方法：从数据库取出数据后，不是把全部数据缓存到cache中，而是把每条数据逇rowid缓存到cache中，对结果集后续操作时，是根据rowid再去数据库中去数据。所以数据库
  中记录发生变化后，通过ResultSet取出的记录是最新的，即ResultSet是SENSITIVE的。但insert和delete操作不会影响到ResultSet，因为insert数据的rowid不在ResultSet
  取出的rowid中，所以insert的数据对ReultSet是不可见的，而delete数据的rowid依旧在ResultSet中，所以ResultSet仍可以取出被删除的记录（因为一般数据库删除是标记删除，不是真正在数据库文件中删除）。
* mysql不支持。(https://blog.csdn.net/iteye_9083/article/details/82612479)
  ```java
  DatabaseMetaData dbMeta = connection.getMetaData();
  System.out.println(dbMeta.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE)); //false
  ```
######resultSets
某些数据库（mysql可以）允许存储过程返回多个结果集，或一次性执行多个语句，每个语句返回一个结果集。这样就可以在不使用连接的情况下，只访问数据库一次就能获得相关数据。
```xml
SELECT * FROM BLOG WHERE ID = #{id}
SELECT * FROM AUTHOR WHERE ID = #{id}

<select id="selectBlog" resultSets="blogs,authors" resultMap="blogResult" statementType="CALLABLE">
  {call getBlogsAndAuthors(#{id,jdbcType=INTEGER,mode=IN})}
</select>

<resultMap id="blogResult" type="Blog">
  <id property="id" column="id" />
  <result property="title" column="title"/>
  <association property="author" javaType="Author" resultSet="authors" column="author_id" foreignColumn="id">
    <id property="id" column="id"/>
    <result property="username" column="username"/>
    <result property="password" column="password"/>
    <result property="email" column="email"/>
    <result property="bio" column="bio"/>
  </association>
</resultMap>
```
databaseId:
<select id="qryAllUserInfo" databaseId="oracle" parameterType="****" >
    select * from sys_user
</select>

####insert
```xml
<insert id="insertAuthor" parameterType="domain.blog.Author" flushCache="true" statementType="PREPARED" keyProperty="" keyColumn="" useGeneratedKeys="" timeout="20">
<update id="updateAuthor" parameterType="domain.blog.Author" flushCache="true" statementType="PREPARED" timeout="20">
<delete id="deleteAuthor" parameterType="domain.blog.Author" flushCache="true" statementType="PREPARED" timeout="20">
```
######useGeneratedKeys和keyProperty
useGeneratedKeys仅对insert和update有用，这会令MyBatis使用JDBC的getGeneratedKeys方法取出由数据库内部生成的主键。默认false。
keyProperty仅对insert和update有用。
```xml
<!-- 自动生成主键id -->
<insert id="insertAuthor" useGeneratedKeys="true"
    keyProperty="id">
  insert into Author (username, password, email, bio) values
  <foreach item="item" collection="list" separator=",">
    (#{item.username}, #{item.password}, #{item.email}, #{item.bio})
  </foreach>
</insert>

<!-- 对于不支持自动生成类型的数据库或可能不支持自动生成键的JDBC驱动，mybatis还可以使用<selectKey>。将主键放到一个表中，然后使用<selectKey>取出一个记录。但引入更多问题，比如主键表空了怎么办。 -->
<insert id="insertAuthor">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    select CAST(RANDOM()*1000000 as INTEGER) a from SYSIBM.SYSDUMMY1
  </selectKey>
  insert into Author
    (id, username, password, email,bio, favourite_section)
  values
    (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
</insert>
```
######order:
这可以被设置为 BEFORE 或 AFTER。如果设置为 BEFORE，那么它会首先生成主键，设置 keyProperty 然后执行插入语句。如果设置为 AFTER，那么先执行插入语句，然后是 selectKey 中的语句 - 这和 Oracle 数据库的行为相似，在插入语句内部可能有嵌入索引调用。

####sql片段
定义可重用的SQL代码段，这些SQL代码可以被包含在其他语句中。
```xml
<!-- sql片段 -->
<sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>
<!-- 使用 -->
<select id="selectUsers" resultType="map">
  select
    <include refid="userColumns"><property name="alias" value="t1"/></include>,
    <include refid="userColumns"><property name="alias" value="t2"/></include>
  from some_table t1
    cross join some_table t2
</select>
```
sql中也可嵌套include。