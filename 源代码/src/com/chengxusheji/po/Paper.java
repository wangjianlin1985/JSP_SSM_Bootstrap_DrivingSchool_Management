package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Paper {
    /*试卷id*/
    private Integer paperId;
    public Integer getPaperId(){
        return paperId;
    }
    public void setPaperId(Integer paperId){
        this.paperId = paperId;
    }

    /*考试科目*/
    private Subject subjectObj;
    public Subject getSubjectObj() {
        return subjectObj;
    }
    public void setSubjectObj(Subject subjectObj) {
        this.subjectObj = subjectObj;
    }

    /*考试名称*/
    @NotEmpty(message="考试名称不能为空")
    private String examName;
    public String getExamName() {
        return examName;
    }
    public void setExamName(String examName) {
        this.examName = examName;
    }

    /*考题内容*/
    @NotEmpty(message="考题内容不能为空")
    private String paperContent;
    public String getPaperContent() {
        return paperContent;
    }
    public void setPaperContent(String paperContent) {
        this.paperContent = paperContent;
    }

    /*添加日期*/
    @NotEmpty(message="添加日期不能为空")
    private String addDate;
    public String getAddDate() {
        return addDate;
    }
    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonPaper=new JSONObject(); 
		jsonPaper.accumulate("paperId", this.getPaperId());
		jsonPaper.accumulate("subjectObj", this.getSubjectObj().getSubjectName());
		jsonPaper.accumulate("subjectObjPri", this.getSubjectObj().getSubjectId());
		jsonPaper.accumulate("examName", this.getExamName());
		jsonPaper.accumulate("paperContent", this.getPaperContent());
		jsonPaper.accumulate("addDate", this.getAddDate().length()>19?this.getAddDate().substring(0,19):this.getAddDate());
		return jsonPaper;
    }}