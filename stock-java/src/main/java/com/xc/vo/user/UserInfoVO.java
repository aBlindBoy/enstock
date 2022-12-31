package com.xc.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    private Integer id;
    private Integer agentId;
    private String agentName;
    private String phone;
    private String nickName;
    private String realName;

    private String idCard;

    private Integer accountType;

    private String recomPhone;

    private Integer isLock;

    private Date regTime;

    private String regIp;

    private String regAddress;

    private String img1Key;

    private String img2Key;
    private String img3Key;
    private Integer isActive;
    private String authMsg;
    private BigDecimal userAmt;
    private BigDecimal enableAmt;
    private BigDecimal userIndexAmt;
    private BigDecimal enableIndexAmt;
    private BigDecimal userFuturesAmt;
    private BigDecimal enableFuturesAmt;
    private BigDecimal allProfitAndLose;
    private BigDecimal allFreezAmt;
    private BigDecimal allIndexProfitAndLose;
    private BigDecimal allIndexFreezAmt;
    private BigDecimal allFuturesProfitAndLose;
    private BigDecimal allFuturesFreezAmt;
    /**
     * 杠杆倍数,多个用/分割
     */
    private String siteLever;

    /*操盘金额*/
    private BigDecimal tradingAmount;
    private BigDecimal twUserAmt;

    private BigDecimal twEnableAmt;

}

