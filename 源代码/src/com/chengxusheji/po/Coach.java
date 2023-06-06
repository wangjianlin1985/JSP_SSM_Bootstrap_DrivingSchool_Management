package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Coach {
    /*教练id*/
    private Integer coachId;
    public Integer getCoachId(){
        return coachId;
    }
    public void setCoachId(Integer coachId){
        this.coachId = coachId;
    }

    /*姓名*/
    @NotEmpty(message="姓名不能为空")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*性别*/
    @NotEmpty(message="性别不能为空")
    private String sex;
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    /*教练照片*/
    private String coachPhoto;
    public String getCoachPhoto() {
        return coachPhoto;
    }
    public void setCoachPhoto(String coachPhoto) {
        this.coachPhoto = coachPhoto;
    }

    /*出生日期*/
    @NotEmpty(message="出生日期不能为空")
    private String birthDate;
    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /*联系电话*/
    @NotEmpty(message="联系电话不能为空")
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*邮箱*/
    @NotEmpty(message="邮箱不能为空")
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /*教练介绍*/
    @NotEmpty(message="教练介绍不能为空")
    private String coachDesc;
    public String getCoachDesc() {
        return coachDesc;
    }
    public void setCoachDesc(String coachDesc) {
        this.coachDesc = coachDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonCoach=new JSONObject(); 
		jsonCoach.accumulate("coachId", this.getCoachId());
		jsonCoach.accumulate("name", this.getName());
		jsonCoach.accumulate("sex", this.getSex());
		jsonCoach.accumulate("coachPhoto", this.getCoachPhoto());
		jsonCoach.accumulate("birthDate", this.getBirthDate().length()>19?this.getBirthDate().substring(0,19):this.getBirthDate());
		jsonCoach.accumulate("telephone", this.getTelephone());
		jsonCoach.accumulate("email", this.getEmail());
		jsonCoach.accumulate("coachDesc", this.getCoachDesc());
		return jsonCoach;
    }}