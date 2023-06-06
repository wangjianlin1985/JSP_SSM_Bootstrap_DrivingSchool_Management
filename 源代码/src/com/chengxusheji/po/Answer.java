package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Answer {
    /*答案id*/
    private Integer answerId;
    public Integer getAnswerId(){
        return answerId;
    }
    public void setAnswerId(Integer answerId){
        this.answerId = answerId;
    }

    /*答题试卷*/
    private Paper paperObj;
    public Paper getPaperObj() {
        return paperObj;
    }
    public void setPaperObj(Paper paperObj) {
        this.paperObj = paperObj;
    }

    /*答题学员*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*作答文件*/
    private String answerFile;
    public String getAnswerFile() {
        return answerFile;
    }
    public void setAnswerFile(String answerFile) {
        this.answerFile = answerFile;
    }

    /*提交时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    /*学员成绩*/
    private String score;
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }

    /*评语*/
    @NotEmpty(message="评语不能为空")
    private String evaluate;
    public String getEvaluate() {
        return evaluate;
    }
    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonAnswer=new JSONObject(); 
		jsonAnswer.accumulate("answerId", this.getAnswerId());
		jsonAnswer.accumulate("paperObj", this.getPaperObj().getExamName());
		jsonAnswer.accumulate("paperObjPri", this.getPaperObj().getPaperId());
		jsonAnswer.accumulate("userObj", this.getUserObj().getName());
		jsonAnswer.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonAnswer.accumulate("answerFile", this.getAnswerFile());
		jsonAnswer.accumulate("addTime", this.getAddTime().length()>19?this.getAddTime().substring(0,19):this.getAddTime());
		jsonAnswer.accumulate("score", this.getScore());
		jsonAnswer.accumulate("evaluate", this.getEvaluate());
		return jsonAnswer;
    }}