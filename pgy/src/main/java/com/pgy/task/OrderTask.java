package com.pgy.task;



import org.springframework.stereotype.Component;

/**
 * 订单相关的定时任务
 */
@Component("OrderTask")
public class OrderTask {

    /**
     * 关闭超时未付款订单
     */
    public void OrderShut(){
        System.out.println("OrderTask.OrderShut");
    }

    /**
    * 订单统计
    */
    public void OrderSum(){
        System.out.println("OrderTask.OrderSum");
    }
}
