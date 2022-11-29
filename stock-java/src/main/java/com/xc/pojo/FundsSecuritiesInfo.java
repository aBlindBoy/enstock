package com.xc.pojo;

import java.io.Serializable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  配資证券信息
 * @author lr 2020-07-24
 */
@Data
public class FundsSecuritiesInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 证券机构id
     */
    private Integer dealerInstitutionsId;

    /**
     * 证券机构名称
     */
    private String dealerInstitutionsName;

    /**
     * 证券营业部
     */
    private String salesDepartment;

    /**
     * 证券帳戶
     */
    private String accountName;

    /**
     * 交易通帳戶
     */
    private String transactAccount;

    /**
     * 交易通密碼
     */
    private String transactPassword;

    /**
     * 通讯密碼
     */
    private String communicationPassword;

    /**
     * 佣金比例
     */
    private BigDecimal commissionRatio;

    /**
     * 最低佣金，單位元
     */
    private BigDecimal minimumCommissions;

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

    public FundsSecuritiesInfo() {
    }

}
