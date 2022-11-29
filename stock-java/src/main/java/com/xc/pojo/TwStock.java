package com.xc.pojo;


public class TwStock {
    private Integer id;
    private String stockName;
    private String stockCode;
    private String stockType;
    private String stockPlate;

    public TwStock(Integer id, String stockName, String stockCode, String stockType, String stockPlate) {
        this.id = id;
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.stockType = stockType;
        this.stockPlate = stockPlate;
    }

    public TwStock() {
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getStockPlate() {
        return stockPlate;
    }

    public void setStockPlate(String stockPlate) {
        this.stockPlate = stockPlate;
    }

    @Override
    public String toString() {
        return "TwStock{" +
                "id=" + id +
                ", stockName='" + stockName + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", stockType='" + stockType + '\'' +
                ", stockPlate='" + stockPlate + '\'' +
                '}';
    }
}
