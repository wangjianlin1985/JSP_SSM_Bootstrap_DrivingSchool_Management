package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.PaperService;
import com.chengxusheji.po.Paper;
import com.chengxusheji.service.SubjectService;
import com.chengxusheji.po.Subject;

//Paper管理控制层
@Controller
@RequestMapping("/Paper")
public class PaperController extends BaseController {

    /*业务层对象*/
    @Resource PaperService paperService;

    @Resource SubjectService subjectService;
	@InitBinder("subjectObj")
	public void initBindersubjectObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("subjectObj.");
	}
	@InitBinder("paper")
	public void initBinderPaper(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("paper.");
	}
	/*跳转到添加Paper视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Paper());
		/*查询所有的Subject信息*/
		List<Subject> subjectList = subjectService.queryAllSubject();
		request.setAttribute("subjectList", subjectList);
		return "Paper_add";
	}

	/*客户端ajax方式提交添加考试试卷信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Paper paper, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        paperService.addPaper(paper);
        message = "考试试卷添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询考试试卷信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("subjectObj") Subject subjectObj,String examName,String addDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (examName == null) examName = "";
		if (addDate == null) addDate = "";
		if(rows != 0)paperService.setRows(rows);
		List<Paper> paperList = paperService.queryPaper(subjectObj, examName, addDate, page);
	    /*计算总的页数和总的记录数*/
	    paperService.queryTotalPageAndRecordNumber(subjectObj, examName, addDate);
	    /*获取到总的页码数目*/
	    int totalPage = paperService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = paperService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Paper paper:paperList) {
			JSONObject jsonPaper = paper.getJsonObject();
			jsonArray.put(jsonPaper);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询考试试卷信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Paper> paperList = paperService.queryAllPaper();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Paper paper:paperList) {
			JSONObject jsonPaper = new JSONObject();
			jsonPaper.accumulate("paperId", paper.getPaperId());
			jsonPaper.accumulate("examName", paper.getExamName());
			jsonArray.put(jsonPaper);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询考试试卷信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("subjectObj") Subject subjectObj,String examName,String addDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (examName == null) examName = "";
		if (addDate == null) addDate = "";
		List<Paper> paperList = paperService.queryPaper(subjectObj, examName, addDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    paperService.queryTotalPageAndRecordNumber(subjectObj, examName, addDate);
	    /*获取到总的页码数目*/
	    int totalPage = paperService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = paperService.getRecordNumber();
	    request.setAttribute("paperList",  paperList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("subjectObj", subjectObj);
	    request.setAttribute("examName", examName);
	    request.setAttribute("addDate", addDate);
	    List<Subject> subjectList = subjectService.queryAllSubject();
	    request.setAttribute("subjectList", subjectList);
		return "Paper/paper_frontquery_result"; 
	}

     /*前台查询Paper信息*/
	@RequestMapping(value="/{paperId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer paperId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键paperId获取Paper对象*/
        Paper paper = paperService.getPaper(paperId);

        List<Subject> subjectList = subjectService.queryAllSubject();
        request.setAttribute("subjectList", subjectList);
        request.setAttribute("paper",  paper);
        return "Paper/paper_frontshow";
	}

	/*ajax方式显示考试试卷修改jsp视图页*/
	@RequestMapping(value="/{paperId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer paperId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键paperId获取Paper对象*/
        Paper paper = paperService.getPaper(paperId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonPaper = paper.getJsonObject();
		out.println(jsonPaper.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新考试试卷信息*/
	@RequestMapping(value = "/{paperId}/update", method = RequestMethod.POST)
	public void update(@Validated Paper paper, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			paperService.updatePaper(paper);
			message = "考试试卷更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "考试试卷更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除考试试卷信息*/
	@RequestMapping(value="/{paperId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer paperId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  paperService.deletePaper(paperId);
	            request.setAttribute("message", "考试试卷删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "考试试卷删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条考试试卷记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String paperIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = paperService.deletePapers(paperIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出考试试卷信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("subjectObj") Subject subjectObj,String examName,String addDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(examName == null) examName = "";
        if(addDate == null) addDate = "";
        List<Paper> paperList = paperService.queryPaper(subjectObj,examName,addDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Paper信息记录"; 
        String[] headers = { "考试科目","考试名称","添加日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<paperList.size();i++) {
        	Paper paper = paperList.get(i); 
        	dataset.add(new String[]{paper.getSubjectObj().getSubjectName(),paper.getExamName(),paper.getAddDate()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Paper.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
