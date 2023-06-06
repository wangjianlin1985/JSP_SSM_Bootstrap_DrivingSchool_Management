package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Paper;

public interface PaperMapper {
	/*添加考试试卷信息*/
	public void addPaper(Paper paper) throws Exception;

	/*按照查询条件分页查询考试试卷记录*/
	public ArrayList<Paper> queryPaper(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有考试试卷记录*/
	public ArrayList<Paper> queryPaperList(@Param("where") String where) throws Exception;

	/*按照查询条件的考试试卷记录数*/
	public int queryPaperCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条考试试卷记录*/
	public Paper getPaper(int paperId) throws Exception;

	/*更新考试试卷记录*/
	public void updatePaper(Paper paper) throws Exception;

	/*删除考试试卷记录*/
	public void deletePaper(int paperId) throws Exception;

}
