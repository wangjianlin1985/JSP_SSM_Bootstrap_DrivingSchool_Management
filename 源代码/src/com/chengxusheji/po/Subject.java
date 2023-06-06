package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Subject {
    /*科目id*/
    private Integer subjectId;
    public Integer getSubjectId(){
        return subjectId;
    }
    public void setSubjectId(Integer subjectId){
        this.subjectId = subjectId;
    }

    /*科目名称*/
    @NotEmpty(message="科目名称不能为空")
    private String subjectName;
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /*适用车辆*/
    @NotEmpty(message="适用车辆不能为空")
    private String carType;
    public String getCarType() {
        return carType;
    }
    public void setCarType(String carType) {
        this.carType = carType;
    }

    /*驾照级别*/
    @NotEmpty(message="驾照级别不能为空")
    private String driveLevel;
    public String getDriveLevel() {
        return driveLevel;
    }
    public void setDriveLevel(String driveLevel) {
        this.driveLevel = driveLevel;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonSubject=new JSONObject(); 
		jsonSubject.accumulate("subjectId", this.getSubjectId());
		jsonSubject.accumulate("subjectName", this.getSubjectName());
		jsonSubject.accumulate("carType", this.getCarType());
		jsonSubject.accumulate("driveLevel", this.getDriveLevel());
		return jsonSubject;
    }}