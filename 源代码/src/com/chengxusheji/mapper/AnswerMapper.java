package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Answer;

public interface AnswerMapper {
	/*添加学员答案信息*/
	public void addAnswer(Answer answer) throws Exception;

	/*按照查询条件分页查询学员答案记录*/
	public ArrayList<Answer> queryAnswer(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有学员答案记录*/
	public ArrayList<Answer> queryAnswerList(@Param("where") String where) throws Exception;

	/*按照查询条件的学员答案记录数*/
	public int queryAnswerCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条学员答案记录*/
	public Answer getAnswer(int answerId) throws Exception;

	/*更新学员答案记录*/
	public void updateAnswer(Answer answer) throws Exception;

	/*删除学员答案记录*/
	public void deleteAnswer(int answerId) throws Exception;

}
