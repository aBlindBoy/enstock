package com.xc.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private Integer id;
    private String stockName;
    private String stockCode;
    private String stockSpell;
    private String stockType;
    private String stockGid;
    private String stockPlate;
    private Integer isLock;
    private Integer isShow;
    private Date addTime;
    /*点差费率*/
    private BigDecimal spreadRate;

    private String companyName;

    private String profile;

    private String website;

    private BigDecimal latestPrice;

    private BigDecimal chg;

    private BigDecimal chgRate;

}