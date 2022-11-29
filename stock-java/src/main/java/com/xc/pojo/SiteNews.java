package com.xc.pojo;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 *  新闻資讯
 * @author lr 2020-08-05
 */
@Data
public class SiteNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新闻主键id
     */
    private Integer id;

    /**
     * 新闻類型：1、财经要闻，2、经济数据，3、全球股市，4、7*24全球，5、商品資讯，6、上市公司，7、全球央行
     */
    private Integer type;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 来源id
     */
    private String sourceId;

    /**
     * 来源名称
     */
    private String sourceName;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 狀態：1、启用，0、停用
     */
    private Integer status;

    /**
     * 显示時间
     */
    private Date showTime;

    /**
     * 添加時间
     */
    private Date addTime;

    /**
     * 修改時间
     */
    private Date updateTime;

    /**
     * 图片地址
     */
    private String imgurl;

    /**
     * 描述
     */
    private String description;

    /**
     * 新闻内容
     */
    private String content;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public SiteNews() {
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
