####一、常用接口
* Driver接口
    Driver接口由数据库厂家提供，开发人员只需要使用Driver接口就可以了，使用前先装载驱动。
    * 装载MySql驱动：Class.forName("com.mysql.jdbc.Driver")
    * 装载Oracle驱动：Class.forName("oracle.jdbc.driver.OracleDriver")
* Connection接口
    Connection与特定数据库的连接（会话），在连接上下文中执行sql语句并返回结果。
    * 连接MySql数据库：Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/database", "user", "password");
    * 连接Oracle数据库：Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@host:port:database", "user", "password");
    * 连接SqlServer数据库：Connection conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://host:port; DatabaseName=database", "user", "password");
    常用方法：
    * createStatement()：创建向数据库发送sql的statement对象。
    * prepareStatement(sql) ：创建向数据库发送预编译sql的PrepareSatement对象。
    * prepareCall(sql)：创建执行存储过程的callableStatement对象。
    * setAutoCommit(boolean autoCommit)：设置事务是否自动提交。
    * commit() ：在链接上提交事务。
    * rollback() ：在此链接上回滚事务。
* Statement接口
    用于执行静态sql语句并返回它所生成结果的对象。
    三种Statement类：
    * Statement: 用于发送简单的sql语句（不带参数）。
    * PreparedStatement：继承Statement接口，用于发送含有一个或多个参数的sql语句。即预编译，可防止sql注入。
    常用Statement方法：
    * execute(String sql)：运行语句，返回是否有结果集。
    * executeQuery(String sql)：运行select语句，返回ResultSet结果集。
    * executeUpdate(String sql)：运行insert/update/delete操作，返回更新的次数。
    * addBatch(String sql)：把多条sql语句放到一个批处理中。
    * executeBatch()：向数据库发送一批sql语句执行。
* ResultSet接口
    ResultSet提供检索不同类型字符的方法：
    * getString(int index)、getString(String columnName)：获得在数据库里是varchar、char等类型的数据对象。
    * getFloat(int index)、getFloat(String columnName)：获得在数据库里是Float类型的数据对象。
    * getDate(int index)、getDate(String columnName)：获得在数据库里是Date类型的数据。
    * getBoolean(int index)、getBoolean(String columnName)：获得在数据库里是Boolean类型的数据。
    * getObject(int index)、getObject(String columnName)：获取在数据库里任意类型的数据。
    ResultSet还提供了对结果集进行滚动的方法：
    * next()：移动到下一行。
    * Previous()：移动到前一行。
    * absolute(int row)：移动到指定行。
    * beforeFirst()：移动resultSet的最前面。
    * afterLast() ：移动到resultSet的最后面。
#### 二、使用JDBC的步骤
加载JDBC驱动程序 -> 建立数据库连接Connection -> 创建执行sql的语句Statement -> 处理执行结果ResultSet -> 释放资源
* 注册驱动（只做一次）
  * 方式一：Class.forName(“com.MySQL.jdbc.Driver”);
        推荐这种方式，不会对具体的驱动类产生依赖（就是不用import package）。
    ```JAVA
    //其实这个只是把com.mysql.jdbc.Driver.class这个类装载进去，但是关键就在于，在 
    //这个类中，有个静态块，如下： 
    static{ 
        try{ 
            java.sql.DriverManager.registerDriver(new Driver()); 
        }catch(SQLException e){ 
            throw new RuntimeException("can't register driver!"); 
        } 
    } 
    //就是因为这个代码块，让类在加载的时候就把驱动注册进去了！
    ```
  * 方式二：DriverManager.registerDriver(com.mysql.jdbc.Driver);
    ```
    会造成DriverManager中产生两个一样的驱动，并会对具体的驱动类产生依赖。 
    具体来说就是： 
    1，加载的时候注册一次驱动（原因请看第三中注册方式），实例化的时候又注册一次。所以两次。 
    2，由于实例化了com.mysql.jdbc.Driver.class，导致必须导入该类(就是要把这个类import进去)，从而具体驱动产生了依赖。不方便扩展代码。
    反射如果没有库的话，编译通过，运行出错。
    包依赖没有库的话，编译不过。
    我理解的是，如果想尽可能的降低依赖，那用反射。
    如果想尽可能在编译期解决问题，那包依赖挺合适的。 
    ```
  * 方式三：System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver");
    ```
    通过系统的属性设置注册驱动。
    如果要注册多个驱动，则System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver:com.oracle.jdbc.Driver");
    虽然不会对具体的驱动类产生依赖；但注册不太方便，所以很少使用。
    ```
* 建立连接
    ```
    　Connection conn = DriverManager.getConnection(url, user, password);
    ```
* 创建执行sql语句的statememt
    ```java
    //Statement  
    String id = "5";
    String sql = "delete from table where id=" +  id;
    Statement st = conn.createStatement();  
    st.executeQuery(sql);  
    //存在sql注入的危险
    //如果用户传入的id为“5 or 1=1”，那么将删除表中的所有记录
    ```
    ```JAVA
    //PreparedStatement 有效的防止sql注入(SQL语句在程序运行前已经进行了预编译,当运行时动态地把参数传给PreprareStatement时，即使参数里有敏感字符如 or '1=1'也数据库会作为一个参数一个字段的属性值来处理而不会作为一个SQL指令)
    String sql = “insert into user (name,pwd) values(?,?)”;  
    PreparedStatement ps = conn.preparedStatement(sql);  
    ps.setString(1, “col_value”);  //占位符顺序从1开始
    ps.setString(2, “123456”); //也可以使用setObject
    ps.executeQuery(); 
    ```
* 处理执行结果（ResultSet）
    ```java
    ResultSet rs = ps.executeQuery();  
    While(rs.next()){  
         rs.getString(“col_name”);  
         rs.getInt(1);  
         //…
    }
    ```
* 释放资源
    ```java
    //数据库连接（Connection）非常耗资源，尽量晚创建，尽量早的释放
    //都要加try catch 以防前面关闭出错，后面的就不执行了
    try {
        if (rs != null) {
            rs.close();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    ```
####三、事务
* 下面情况事务会结束
    * 执行COMMIT或ROLLBACK语句
    * 执行一条DDL语句，例create table。这种情况下，会自动执行commit语句。
    * 执行一条DCL语句，例如grant。这种情况下，会自动执行commit语句。
    * 断开与数据库的连接。会rollback
    * 执行一条DML(insert/update/delete)语句，该语句却失败了。这种情况下，会为这个无效的DML语句执行rollback。
* 批处理Batch
    ```java
    Class.forName("com.mysql.jdbc.Driver");
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc","root","mysql");
    conn.setAutoCommit(false); //设为手动提交
    long start = System.currentTimeMillis();
    stmt = conn.createStatement();
    for (int i = 0; i < 20000; i++) {
        stmt.addBatch("insert into t_user (userName,pwd,regTime) values ('hao" + i + "',666666,now())");
    }
    stmt.executeBatch();
    conn.commit();  //提交事务
    ```

http://www.cnblogs.com/erbing/p/5805727.html