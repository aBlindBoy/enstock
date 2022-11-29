package com.xc.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  新股申购
 * @author lr 2020-09-11
 */
@Data
public class UserStockSubscribe implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用戶id
     */
    private Integer userId;

    /**
     * 用戶真实姓名
     */
    private String realName;

    /**
     * 用戶手機號
     */
    private String phone;

    /**
     * 管理员id
     */
    private Integer adminId;

    /**
     * 管理员姓名
     */
    private String adminName;

    /**
     * 提交金额
     */
    private BigDecimal submitAmount;
    /**
     * 成交金额
     */
    private BigDecimal tradeAmount;
    /**
     * 提交張數
     */
    private Integer submitSheets;

    /**
     * 提交張數
     */
    private Integer tradeSheets;

    /**
     * 狀態：1、预约成功，2、提交成功，3、已中签，4、未中签
     */
    private Integer status;

    /**
     * 添加時间
     */
    private Date addTime;

    /**
     * 提交時间
     */
    private Date submitTime;

    /**
     * 中签時间
     */
    private Date endTime;

    /**
     * 抽籤日期
     */
    private String drawDate;

    /**
     * 股票代號
     */
    private String stockCode;

    /**
     * 股票名稱
     */
    private String stockName;

    /**
     * 發行市場
     */
    private String stockPlate;

    /**
     * 申購期間
     */
    private String subscriptionTime;

    /**
     * 撥券日期
     */
    private String ticketingDate;

    /**
     * 承銷張數
     */
    private String underwritingSheet;

    /**
     * 承銷价格
     */
    private String underwritingPrice;
    /**
     * 市價
     */
    private String marketPrice;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 扣款狀態
     */
    private Integer deductionStatus;

}
