package com.xc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private Integer agentId;
    private String agentName;
    private String phone;
    private String userPwd;
    private String withPwd;
    private String nickName;
    private String realName;
    private String idCard;


    private Integer accountType;

    private BigDecimal userAmt;

    private BigDecimal enableAmt;

    private BigDecimal sumChargeAmt;

    private BigDecimal sumBuyAmt;
    private String recomPhone;
    private Integer isLock;
    private Integer isLogin;
    private Date regTime;
    private String regIp;
    private String regAddress;
    private String img1Key;
    private String img2Key;
    private String img3Key;
    private Integer isActive;
    private String authMsg;
    private BigDecimal userIndexAmt;
    private BigDecimal enableIndexAmt;
    private BigDecimal userFutAmt;
    private BigDecimal enableFutAmt;
    private String withdrawalPwd;
    /*总操盘金额*/
    private BigDecimal tradingAmount;

    // tw_user_amt,tw_enable_amt
    private BigDecimal twUserAmt;

    private BigDecimal twEnableAmt;

}
