package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Subject;

import com.chengxusheji.mapper.SubjectMapper;
@Service
public class SubjectService {

	@Resource SubjectMapper subjectMapper;
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

    /*添加科目记录*/
    public void addSubject(Subject subject) throws Exception {
    	subjectMapper.addSubject(subject);
    }

    /*按照查询条件分页查询科目记录*/
    public ArrayList<Subject> querySubject(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return subjectMapper.querySubject(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Subject> querySubject() throws Exception  { 
     	String where = "where 1=1";
    	return subjectMapper.querySubjectList(where);
    }

    /*查询所有科目记录*/
    public ArrayList<Subject> queryAllSubject()  throws Exception {
        return subjectMapper.querySubjectList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = subjectMapper.querySubjectCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取科目记录*/
    public Subject getSubject(int subjectId) throws Exception  {
        Subject subject = subjectMapper.getSubject(subjectId);
        return subject;
    }

    /*更新科目记录*/
    public void updateSubject(Subject subject) throws Exception {
        subjectMapper.updateSubject(subject);
    }

    /*删除一条科目记录*/
    public void deleteSubject (int subjectId) throws Exception {
        subjectMapper.deleteSubject(subjectId);
    }

    /*删除多条科目信息*/
    public int deleteSubjects (String subjectIds) throws Exception {
    	String _subjectIds[] = subjectIds.split(",");
    	for(String _subjectId: _subjectIds) {
    		subjectMapper.deleteSubject(Integer.parseInt(_subjectId));
    	}
    	return _subjectIds.length;
    }
}
