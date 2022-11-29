package com.xc.pojo;

import java.io.Serializable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  funds_lever
 * @author lr 2020-07-23
 */
@Data
public class FundsLever implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 週期類型：1按天、2按週、3按月
     */
    private Integer cycleType;

    /**
     * 杠杆
     */
    private Integer lever;

    public BigDecimal getManageRate() {
        return manageRate;
    }

    public void setManageRate(BigDecimal manageRate) {
        this.manageRate = manageRate;
    }

    /**
     * 管理费率
     */
    private BigDecimal manageRate;

    /**
     * 單股持倉比例
     */
    private BigDecimal singleHoldingRatio;

    /**
     * 狀態：1、启用，0、停用
     */
    private Integer status;

    /**
     * 添加時间
     */
    private Date addTime;

    /**
     * 修改時间
     */
    private Date updateTime;

    public FundsLever() {
    }

}
