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
一级缓存是SqlSession级别的缓存, 一个SqlSession结束后该SqlSession中的一级缓存也就不存在了, 不同的SqlSession之间的缓存数据区域是互不影响的.
二级缓存是Mapper级别的缓存, 跨SqlSession, 共享域是mapper的同一个namespace, 默认没开启,需要在setting全局配置中开启.

databaseId:
<select id="qryAllUserInfo" databaseId="oracle" parameterType="****" >
    select * from sys_user
</select>