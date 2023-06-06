package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Subject;
import com.chengxusheji.po.Paper;

import com.chengxusheji.mapper.PaperMapper;
@Service
public class PaperService {

	@Resource PaperMapper paperMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加考试试卷记录*/
    public void addPaper(Paper paper) throws Exception {
    	paperMapper.addPaper(paper);
    }

    /*按照查询条件分页查询考试试卷记录*/
    public ArrayList<Paper> queryPaper(Subject subjectObj,String examName,String addDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != subjectObj && subjectObj.getSubjectId()!= null && subjectObj.getSubjectId()!= 0)  where += " and t_paper.subjectObj=" + subjectObj.getSubjectId();
    	if(!examName.equals("")) where = where + " and t_paper.examName like '%" + examName + "%'";
    	if(!addDate.equals("")) where = where + " and t_paper.addDate like '%" + addDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return paperMapper.queryPaper(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Paper> queryPaper(Subject subjectObj,String examName,String addDate) throws Exception  { 
     	String where = "where 1=1";
    	if(null != subjectObj && subjectObj.getSubjectId()!= null && subjectObj.getSubjectId()!= 0)  where += " and t_paper.subjectObj=" + subjectObj.getSubjectId();
    	if(!examName.equals("")) where = where + " and t_paper.examName like '%" + examName + "%'";
    	if(!addDate.equals("")) where = where + " and t_paper.addDate like '%" + addDate + "%'";
    	return paperMapper.queryPaperList(where);
    }

    /*查询所有考试试卷记录*/
    public ArrayList<Paper> queryAllPaper()  throws Exception {
        return paperMapper.queryPaperList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Subject subjectObj,String examName,String addDate) throws Exception {
     	String where = "where 1=1";
    	if(null != subjectObj && subjectObj.getSubjectId()!= null && subjectObj.getSubjectId()!= 0)  where += " and t_paper.subjectObj=" + subjectObj.getSubjectId();
    	if(!examName.equals("")) where = where + " and t_paper.examName like '%" + examName + "%'";
    	if(!addDate.equals("")) where = where + " and t_paper.addDate like '%" + addDate + "%'";
        recordNumber = paperMapper.queryPaperCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取考试试卷记录*/
    public Paper getPaper(int paperId) throws Exception  {
        Paper paper = paperMapper.getPaper(paperId);
        return paper;
    }

    /*更新考试试卷记录*/
    public void updatePaper(Paper paper) throws Exception {
        paperMapper.updatePaper(paper);
    }

    /*删除一条考试试卷记录*/
    public void deletePaper (int paperId) throws Exception {
        paperMapper.deletePaper(paperId);
    }

    /*删除多条考试试卷信息*/
    public int deletePapers (String paperIds) throws Exception {
    	String _paperIds[] = paperIds.split(",");
    	for(String _paperId: _paperIds) {
    		paperMapper.deletePaper(Integer.parseInt(_paperId));
    	}
    	return _paperIds.length;
    }
}
