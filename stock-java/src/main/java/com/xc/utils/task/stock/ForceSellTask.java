package com.xc.utils.task.stock;


import com.xc.service.IUserService;

import com.xc.utils.DateTimeUtil;

import com.xc.utils.stock.BuyAndSellUtils;

import java.util.Date;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;


@Component
public class ForceSellTask {

    private static final Logger log = LoggerFactory.getLogger(ForceSellTask.class);


    @Autowired
    IUserService iUserService;


    private static final String am_begin = "9:30";


    private static final String am_end = "11:30";


    private static final String pm_begin = "13:00";


    private static final String pm_end = "15:00";


    /*用戶持倉單-用戶总金额-強平定時*/
   /* @Scheduled(cron = "0 0/3 9-15 ? * MON-FRI")
    public void banlanceUserPositionTaskV1() {

        boolean am = false;

        boolean pm = false;

        try {

            am = BuyAndSellUtils.isTransTime("9:30", "11:30");

            pm = BuyAndSellUtils.isTransTime("13:00", "15:00");

        } catch (Exception e) {

            log.error("执行定時任务出错，e = {}", e);

        }


        log.info("當前 am = {}  pm = {}", Boolean.valueOf(am), Boolean.valueOf(pm));

        if (am || pm) {

            log.info("=====扫描用戶持倉执行，當前時间 {} =====", DateTimeUtil.dateToStr(new Date()));

            dotask();

            log.info("=====扫描用戶持倉结束，當前時间 {} =====", DateTimeUtil.dateToStr(new Date()));

        } else {

            log.info("當前時间不為週一至週五，或者不在交易時间内，不执行（強平）定時任务");

        }

    }*/


//    public void dotask() {
//        this.iUserService.ForceSellTask();
//    }

    /*用戶持倉單-單支股票盈虧-強平定時*/
    /*@Scheduled(cron = "0 0/1 9-15 ? * MON-FRI")
    public void stockProfitLossOneTask() {
        boolean am = false;
        boolean pm = false;
        try {
            am = BuyAndSellUtils.isTransTime("9:30", "11:30");
            pm = BuyAndSellUtils.isTransTime("13:00", "15:00");
        } catch (Exception e) {
            log.error("执行定時任务出错，e = {}", e);
        }

        log.info("當前 am = {}  pm = {}", Boolean.valueOf(am), Boolean.valueOf(pm));
        if (am || pm) {
            log.info("=====扫描單支股票盈虧执行，當前時间 {} =====", DateTimeUtil.dateToStr(new Date()));
            this.iUserService.ForceSellOneStockTask();
            log.info("=====扫描單支股票盈虧结束，當前時间 {} =====", DateTimeUtil.dateToStr(new Date()));

        } else {
            log.info("當前時间不為週一至週五，或者不在交易時间内，不执行（強平）單支股票盈虧定時任务");
        }

    }*/

    /*用戶股票持倉單-強平提醒推送消息定時*/
    @Scheduled(cron = "0 0/3 9-15 ? * MON-FRI")
    public void banlanceUserPositionMessage() {

        boolean am = false;

        boolean pm = false;

        try {

            am = BuyAndSellUtils.isTransTime("9:30", "11:30");

            pm = BuyAndSellUtils.isTransTime("13:00", "15:00");

        } catch (Exception e) {

            log.error("执行定時任务出错，e = {}", e);

        }


        log.info("當前 am = {}  pm = {}", Boolean.valueOf(am), Boolean.valueOf(pm));

        if (am || pm) {

            log.info("=====扫描用戶持倉执行，當前時间 {} =====", DateTimeUtil.dateToStr(new Date()));

            this.iUserService.ForceSellMessageTask();

            log.info("=====扫描用戶持倉结束，當前時间 {} =====", DateTimeUtil.dateToStr(new Date()));

        } else {

            log.info("當前時间不為週一至週五，或者不在交易時间内，不执行（強平）提醒推送消息任务");

        }

    }

}
