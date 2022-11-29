package com.xc.utils.task.futures;


import com.xc.service.IUserService;
import com.xc.utils.DateTimeUtil;
import com.xc.utils.stock.BuyAndSellUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ForceSellFuturesTask {
    private static final Logger log = LoggerFactory.getLogger(ForceSellFuturesTask.class);
    @Autowired
    IUserService iUserService;

    /*指数強制平倉，每分鐘执行一次*/
    @Scheduled(cron = "0 */1 * * * ?")
    public void banlanceUserFuturesPositionTaskV1() {
        dotask();
    }

    public void dotask() {
        this.iUserService.ForceSellFuturesTask();
    }

    /*指数強平提醒推送消息，每分鐘检测一次*/
    @Scheduled(cron = "0 */1 * * * ?")
    public void banlanceUserFuturesPositioMessage() {
        this.iUserService.ForceSellFuturesMessageTask();
    }

    /*期货走势图定時任务*/
    @Scheduled(cron = "0 0/1 0-23 * * MON-FRI")
    public void qh1() {
        boolean am = false;
        boolean pm = false;
        try {
            am = BuyAndSellUtils.isTransTime("0:01", "12:59");
            pm = BuyAndSellUtils.isTransTime("12:59", "23:59");
        } catch (Exception e) {
            log.error("= {}", e);
        }
        if (am || pm) {
            this.iUserService.qh1();
        }
    }
}
