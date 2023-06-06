package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class News {
    /*记录id*/
    private Integer newsId;
    public Integer getNewsId(){
        return newsId;
    }
    public void setNewsId(Integer newsId){
        this.newsId = newsId;
    }

    /*信息类别*/
    @NotEmpty(message="信息类别不能为空")
    private String newsType;
    public String getNewsType() {
        return newsType;
    }
    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    /*信息标题*/
    @NotEmpty(message="信息标题不能为空")
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*内容*/
    @NotEmpty(message="内容不能为空")
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*发布时间*/
    @NotEmpty(message="发布时间不能为空")
    private String publishDate;
    public String getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonNews=new JSONObject(); 
		jsonNews.accumulate("newsId", this.getNewsId());
		jsonNews.accumulate("newsType", this.getNewsType());
		jsonNews.accumulate("title", this.getTitle());
		jsonNews.accumulate("content", this.getContent());
		jsonNews.accumulate("publishDate", this.getPublishDate().length()>19?this.getPublishDate().substring(0,19):this.getPublishDate());
		return jsonNews;
    }}