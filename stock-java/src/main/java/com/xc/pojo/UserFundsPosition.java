package com.xc.pojo;

import java.io.Serializable;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  分倉交易表
 * @author lr 2020-07-27
 */
@Data
public class UserFundsPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 帳戶類型：0、实盘，1、模拟盘
     */
    private Integer positionType;

    /**
     * 訂單编号
     */
    private String positionSn;

    /**
     * 用戶id
     */
    private Integer userId;

    /**
     * 用戶真实姓名
     */
    private String nickName;

    /**
     * 子帳戶编号，默认从80000000开始
     */
    private Integer subaccountNumber;

    /**
     * 代理id
     */
    private Integer agentId;

    /**
     * 股票id
     */
    private Integer stockId;

    /**
     * 股票名称
     */
    private String stockName;

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 股票gid
     */
    private String stockGid;

    /**
     * 股票简拼
     */
    private String stockSpell;

    /**
     * 入倉單号
     */
    private String buyOrderId;

    /**
     * 入倉時间
     */
    private Date buyOrderTime;

    /**
     * 入倉單价
     */
    private BigDecimal buyOrderPrice;

    /**
     * 出倉單号
     */
    private String sellOrderId;

    /**
     * 出倉時间
     */
    private Date sellOrderTime;

    /**
     * 出倉單价
     */
    private BigDecimal sellOrderPrice;

    /**
     * 下單方向：買漲、買跌
     */
    private String orderDirection;

    /**
     * 訂單数量
     */
    private Integer orderNum;

    /**
     * 訂單杠杆
     */
    private Integer orderLever;

    /**
     * 訂單总金额
     */
    private BigDecimal orderTotalPrice;

    /**
     * 手续费
     */
    private BigDecimal orderFee;

    /**
     * 印花税
     */
    private BigDecimal orderSpread;

    /**
     * 留倉费
     */
    private BigDecimal orderStayFee;

    /**
     * 留倉天数
     */
    private Integer orderStayDays;

    /**
     * 浮动盈虧
     */
    private BigDecimal profitAndLose;

    /**
     * 總盈虧
     */
    private BigDecimal allProfitAndLose;

    /**
     * 是否锁倉：1、是，0、否
     */
    private Integer isLock;

    /**
     * 锁倉原因
     */
    private String lockMsg;

    /**
     * 股票板块：科创
     */
    private String stockPlate;

    /**
     * 点差费
     */
    private BigDecimal spreadRatePrice;

    public UserFundsPosition() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public String getPositionSn() {
        return positionSn;
    }

    public void setPositionSn(String positionSn) {
        this.positionSn = positionSn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSubaccountNumber() {
        return subaccountNumber;
    }

    public void setSubaccountNumber(Integer subaccountNumber) {
        this.subaccountNumber = subaccountNumber;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockGid() {
        return stockGid;
    }

    public void setStockGid(String stockGid) {
        this.stockGid = stockGid;
    }

    public String getStockSpell() {
        return stockSpell;
    }

    public void setStockSpell(String stockSpell) {
        this.stockSpell = stockSpell;
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(String buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public Date getBuyOrderTime() {
        return buyOrderTime;
    }

    public void setBuyOrderTime(Date buyOrderTime) {
        this.buyOrderTime = buyOrderTime;
    }

    public BigDecimal getBuyOrderPrice() {
        return buyOrderPrice;
    }

    public void setBuyOrderPrice(BigDecimal buyOrderPrice) {
        this.buyOrderPrice = buyOrderPrice;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(String sellOrderId) {
        this.sellOrderId = sellOrderId;
    }

    public Date getSellOrderTime() {
        return sellOrderTime;
    }

    public void setSellOrderTime(Date sellOrderTime) {
        this.sellOrderTime = sellOrderTime;
    }

    public BigDecimal getSellOrderPrice() {
        return sellOrderPrice;
    }

    public void setSellOrderPrice(BigDecimal sellOrderPrice) {
        this.sellOrderPrice = sellOrderPrice;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderLever() {
        return orderLever;
    }

    public void setOrderLever(Integer orderLever) {
        this.orderLever = orderLever;
    }

    public BigDecimal getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public BigDecimal getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(BigDecimal orderFee) {
        this.orderFee = orderFee;
    }

    public BigDecimal getOrderSpread() {
        return orderSpread;
    }

    public void setOrderSpread(BigDecimal orderSpread) {
        this.orderSpread = orderSpread;
    }

    public BigDecimal getOrderStayFee() {
        return orderStayFee;
    }

    public void setOrderStayFee(BigDecimal orderStayFee) {
        this.orderStayFee = orderStayFee;
    }

    public Integer getOrderStayDays() {
        return orderStayDays;
    }

    public void setOrderStayDays(Integer orderStayDays) {
        this.orderStayDays = orderStayDays;
    }

    public BigDecimal getProfitAndLose() {
        return profitAndLose;
    }

    public void setProfitAndLose(BigDecimal profitAndLose) {
        this.profitAndLose = profitAndLose;
    }

    public BigDecimal getAllProfitAndLose() {
        return allProfitAndLose;
    }

    public void setAllProfitAndLose(BigDecimal allProfitAndLose) {
        this.allProfitAndLose = allProfitAndLose;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public String getLockMsg() {
        return lockMsg;
    }

    public void setLockMsg(String lockMsg) {
        this.lockMsg = lockMsg;
    }

    public String getStockPlate() {
        return stockPlate;
    }

    public void setStockPlate(String stockPlate) {
        this.stockPlate = stockPlate;
    }

    public BigDecimal getSpreadRatePrice() {
        return spreadRatePrice;
    }

    public void setSpreadRatePrice(BigDecimal spreadRatePrice) {
        this.spreadRatePrice = spreadRatePrice;
    }
}

