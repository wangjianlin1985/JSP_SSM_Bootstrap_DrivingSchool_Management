package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.CarState;

public interface CarStateMapper {
	/*添加车辆状态信息*/
	public void addCarState(CarState carState) throws Exception;

	/*按照查询条件分页查询车辆状态记录*/
	public ArrayList<CarState> queryCarState(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有车辆状态记录*/
	public ArrayList<CarState> queryCarStateList(@Param("where") String where) throws Exception;

	/*按照查询条件的车辆状态记录数*/
	public int queryCarStateCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条车辆状态记录*/
	public CarState getCarState(int stateId) throws Exception;

	/*更新车辆状态记录*/
	public void updateCarState(CarState carState) throws Exception;

	/*删除车辆状态记录*/
	public void deleteCarState(int stateId) throws Exception;

}
