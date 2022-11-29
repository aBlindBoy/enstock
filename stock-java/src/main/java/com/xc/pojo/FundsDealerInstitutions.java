package com.xc.pojo;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 *  配資券商机构
 * @author lr 2020-07-24
 */
@Data
public class FundsDealerInstitutions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 券商编号id
     */
    private Integer dealerNumber;

    /**
     * 券商名称
     */
    private String dealerName;

    /**
     * 客戶端版本号
     */
    private String clientVersionNumber;

    /**
     * 备注
     */
    private String remarks;

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

    public FundsDealerInstitutions() {
    }

}
