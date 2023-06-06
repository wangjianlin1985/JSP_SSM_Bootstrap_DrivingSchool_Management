package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Car {
    /*车牌号*/
    @NotEmpty(message="车牌号不能为空")
    private String carNo;
    public String getCarNo(){
        return carNo;
    }
    public void setCarNo(String carNo){
        this.carNo = carNo;
    }

    /*车辆名称*/
    @NotEmpty(message="车辆名称不能为空")
    private String carName;
    public String getCarName() {
        return carName;
    }
    public void setCarName(String carName) {
        this.carName = carName;
    }

    /*车辆照片*/
    private String carPhoto;
    public String getCarPhoto() {
        return carPhoto;
    }
    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    /*换挡方式*/
    @NotEmpty(message="换挡方式不能为空")
    private String shitWay;
    public String getShitWay() {
        return shitWay;
    }
    public void setShitWay(String shitWay) {
        this.shitWay = shitWay;
    }

    /*上牌日期*/
    @NotEmpty(message="上牌日期不能为空")
    private String onCardDate;
    public String getOnCardDate() {
        return onCardDate;
    }
    public void setOnCardDate(String onCardDate) {
        this.onCardDate = onCardDate;
    }

    /*车辆介绍*/
    @NotEmpty(message="车辆介绍不能为空")
    private String carDesc;
    public String getCarDesc() {
        return carDesc;
    }
    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    /*车辆状态*/
    private CarState carState;
    public CarState getCarState() {
        return carState;
    }
    public void setCarState(CarState carState) {
        this.carState = carState;
    }

    /*训练场地*/
    @NotEmpty(message="训练场地不能为空")
    private String carPlace;
    public String getCarPlace() {
        return carPlace;
    }
    public void setCarPlace(String carPlace) {
        this.carPlace = carPlace;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonCar=new JSONObject(); 
		jsonCar.accumulate("carNo", this.getCarNo());
		jsonCar.accumulate("carName", this.getCarName());
		jsonCar.accumulate("carPhoto", this.getCarPhoto());
		jsonCar.accumulate("shitWay", this.getShitWay());
		jsonCar.accumulate("onCardDate", this.getOnCardDate().length()>19?this.getOnCardDate().substring(0,19):this.getOnCardDate());
		jsonCar.accumulate("carDesc", this.getCarDesc());
		jsonCar.accumulate("carState", this.getCarState().getStateName());
		jsonCar.accumulate("carStatePri", this.getCarState().getStateId());
		jsonCar.accumulate("carPlace", this.getCarPlace());
		return jsonCar;
    }}