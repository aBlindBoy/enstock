package com.xc.utils.task.stock;


import com.xc.service.IUserPositionService;

import com.xc.utils.DateTimeUtil;

import java.util.Date;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;


@Component
public class ClosingStayTask {

    private static final Logger log = LoggerFactory.getLogger(ClosingStayTask.class);


    @Autowired
    IUserPositionService iUserPositionService;

    /*遞延費週一到週五，每天七点定時计算*/
//    @Scheduled(cron = "0 0 7 * * ?")
    //cron=秒 分 時 0 0 10,14,16 * * ? 每天上午10点，下午2点，4点
    @Scheduled(cron = "0 15 9 ? * MON-FRI")
    //@Scheduled(cron = "0 46 12 ? * *")
    public void closingStayV1() {

        log.info("=======================收盘收取留倉费任务开始 ===========================");

        log.info("收盘收取留倉费任务 开始時间 = {}", DateTimeUtil.dateToStr(new Date()));

        log.info("");

        dotask();

        log.info("");

        log.info("收盘收取留倉费任务 结束時间 = {}", DateTimeUtil.dateToStr(new Date()));

        log.info("=======================收盘收取留倉费任务结束 ===========================");

    }


    public void dotask() {
        this.iUserPositionService.doClosingStayTask();
    }

    /*留倉到期強制平倉，每天15点执行*/
//    @Scheduled(cron = "0 0 15 ? * MON-FRI")
//    public void expireStayUnwind() {
//
//        log.info("=======================留倉到期強制平倉任务开始 ===========================");
//        log.info("留倉到期強制平倉 开始時间 = {}", DateTimeUtil.dateToStr(new Date()));
//
//        this.iUserPositionService.expireStayUnwindTask();
//
//        log.info("留倉到期強制平倉 结束時间 = {}", DateTimeUtil.dateToStr(new Date()));
//        log.info("=======================留倉到期強制平倉任务结束 ===========================");
//
//    }

}
