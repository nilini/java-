####dubbo服务调试管理命令
项目是丰项目组开发的，各个项目组调用个项目组的接口，有时候需要在联调环境调试对方的接口，可以直接telnet到dubbo的服务，
通过命令查看已经发布的接口和方法，并能直接invoke具体的方法。

* 1、连接服务
    测试对应IP和端口下的dubbo服务是否连通。
    ```java
    telnet localhost 80801
    ```
    进入telnet窗口
    ```shell
    dubbo>
    ```
* 2、查看服务列表
    查看服务
    ```java
    dubbo>ls
    com.test.DemoService
    ```
    查看服务中的接口
    ```java
    dubbo>ls com.test.DemoService
    queryDemoPageList
    insertDemolist
    uploadDemoList
    deleteDemolist
    ```
* 3、调用服务接口
    调用服务接口时，以JSON格式传入参数，然后打印返回值和返回时间。
    ```JSON
    dubbo>invoke com.test.DemoService.queryDemoPageList({"id":"100"},1,2)
    {"totalCount":1,"data":[{date":"2011-03-2314:10:32","name":"张三","keyword":null}]}
    elapsed: 10 ms.
    ```
* 4、查看服务状态
    查看服务调用次数
    ```java
    dubbo>count  com.test.DemoService
    dubbo>
    +-------------------------+-------+--------+--------+---------+-----+
    | method                | total | failed | active | average | max |
    +-------------------------+-------+--------+--------+---------+-----+
    | queryDemoPageList | 0     | 0    | 0      | 0ms     | 0ms |
    | insertDemolist    | 0     | 0    | 0      | 0ms     | 0ms |
    | uploadDemoList    | 0     | 0    | 0      | 0ms     | 0ms |
    | deleteDemolist    | 0     | 0    | 0      | 0ms     | 0ms |
    +-------------------------+-------+--------+--------+---------+-----+
    ```
    countXxxService //统计1次服务任意方法的调用情况。
    countXxxService 10 //统计10次服务任意方法的调用情况。
    countXxxService xxxMethod //统计1次服务方法的调用情况。
    countXxxService xxxMethod 10 //统计10次服务方法的调用情况。

    status 显示汇总状态，该状态将汇总所有资源的状态，当全部OK时则显示OK，只要有一个ERROR则显示ERROR，只要有一个WARN则显示WARN。
    status -l 显示状态列表。