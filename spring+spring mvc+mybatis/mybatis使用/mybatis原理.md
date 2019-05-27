###1、MyBatis简介
MyBatis避免了几乎所有的JDBC代码和手动设置参数以及获取结果集。MyBatis可以使用简单的XML或注解来配置和映射原生信息。将接口和Java的POJOS(普通的Java对象)映射成数据库的记录。
###2、框架图
![20170607091428631](/assets/3552355082-5b0fa771ad34f_articlex.png)
* 通过configuration解析config.xml配置文件和mapper.xml映射文件，映射文件可以使用xml方式或者注解方式。
* 然后由configuration获得sqlsessionfactory对象。
* 再由sqlsessionfactory获得sqlsession数据库访问会话对象，通过会话对象获得DAO层的mapper对象。
* 通过调用mapper对象相应方法，框架会自动执行sql语句从而获得结果。
###3、细节
####xml解析和配置解析
```java
String resource = "org/mybatis/example/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```
```java
public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    SqlSessionFactory var5;
    try {
        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
        var5 = this.build(parser.parse());
    } catch (Exception var14) {
        throw ExceptionFactory.wrapException("Error building SqlSession.", var14);
    } finally {
        ErrorContext.instance().reset();

        try {
            inputStream.close();
        } catch (IOException var13) {
            ;
        }
    }
    return var5;
}

public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
}
```
通过XMLConfigBuilder初始化并解析了配置文件，最后返回了一个DefaultSqlSessionFactory对象并返回。

####sqlSession对象
我们通过配置文件或者配置对象与MyBatis交互。使用sqlSession对象来操作数据库。
SqlSession提供select/insert/update/delete方法。
通过动态代理技术，让接口跑起来，之后采用命令模式，最后还是采用了SqlSession的接口方法执行sql查询（也就是说Mapper接口方法的实现底层还是采用SqlSession接口方法实现的）。

####Mapper对象
调用mapper对象方法就能发出sql操作数据库，mapper对象是一个一个mapperProxy的代理类，所以这个代理类必然实现了InvocationHandler接口。所以调用永久层接口的方法时就会调用到这个MapperProxy对象的invoke方法。
最后还是调用sqlsession与数据库交互。
```java
param = this.method.convertArgsToSqlCommandParam(args);
result = sqlSession.selectOne(this.command.getName(), param);
```
