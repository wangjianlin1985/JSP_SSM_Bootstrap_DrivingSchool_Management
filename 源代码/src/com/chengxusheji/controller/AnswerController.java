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
import com.chengxusheji.service.AnswerService;
import com.chengxusheji.po.Answer;
import com.chengxusheji.service.PaperService;
import com.chengxusheji.po.Paper;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//Answer管理控制层
@Controller
@RequestMapping("/Answer")
public class AnswerController extends BaseController {

    /*业务层对象*/
    @Resource AnswerService answerService;

    @Resource PaperService paperService;
    @Resource UserInfoService userInfoService;
	@InitBinder("paperObj")
	public void initBinderpaperObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("paperObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("answer")
	public void initBinderAnswer(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("answer.");
	}
	/*跳转到添加Answer视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Answer());
		/*查询所有的Paper信息*/
		List<Paper> paperList = paperService.queryAllPaper();
		request.setAttribute("paperList", paperList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "Answer_add";
	}

	/*客户端ajax方式提交添加学员答案信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Answer answer, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		answer.setAnswerFile(this.handleFileUpload(request, "answerFileFile"));
        answerService.addAnswer(answer);
        message = "学员答案添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*客户端ajax方式提交添加学员答案信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(Answer answer, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		if(answerService.queryAnswer(answer.getPaperObj(), answer.getUserObj(), "").size() > 0) {
			message = "你已经提交过了这个考试答案！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		
		String answerFile = this.handleFileUpload(request, "answerFileFile");
		if(answerFile.equals("")) {
			message = "请选择答案文件！";
			writeJsonResponse(response, success, message);
			return ;
		}
		answer.setAnswerFile(answerFile);
 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		answer.setAddTime(sdf.format(new java.util.Date()));
		
		answer.setEvaluate("--");
		answer.setScore("待审阅");
		
		UserInfo userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		answer.setUserObj(userObj);
		
		
		
        answerService.addAnswer(answer);
        message = "学员答案添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*ajax方式按照查询条件分页查询学员答案信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("paperObj") Paper paperObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (addTime == null) addTime = "";
		if(rows != 0)answerService.setRows(rows);
		List<Answer> answerList = answerService.queryAnswer(paperObj, userObj, addTime, page);
	    /*计算总的页数和总的记录数*/
	    answerService.queryTotalPageAndRecordNumber(paperObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = answerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = answerService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Answer answer:answerList) {
			JSONObject jsonAnswer = answer.getJsonObject();
			jsonArray.put(jsonAnswer);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询学员答案信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Answer> answerList = answerService.queryAllAnswer();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Answer answer:answerList) {
			JSONObject jsonAnswer = new JSONObject();
			jsonAnswer.accumulate("answerId", answer.getAnswerId());
			jsonArray.put(jsonAnswer);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询学员答案信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("paperObj") Paper paperObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (addTime == null) addTime = "";
		List<Answer> answerList = answerService.queryAnswer(paperObj, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    answerService.queryTotalPageAndRecordNumber(paperObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = answerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = answerService.getRecordNumber();
	    request.setAttribute("answerList",  answerList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("paperObj", paperObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("addTime", addTime);
	    List<Paper> paperList = paperService.queryAllPaper();
	    request.setAttribute("paperList", paperList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Answer/answer_frontquery_result"; 
	}
	
	
	/*用户前台按照查询条件分页查询学员答案信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("paperObj") Paper paperObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (addTime == null) addTime = "";
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		
		List<Answer> answerList = answerService.queryAnswer(paperObj, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    answerService.queryTotalPageAndRecordNumber(paperObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = answerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = answerService.getRecordNumber();
	    request.setAttribute("answerList",  answerList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("paperObj", paperObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("addTime", addTime);
	    List<Paper> paperList = paperService.queryAllPaper();
	    request.setAttribute("paperList", paperList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Answer/answer_userFrontquery_result"; 
	}
	

     /*前台查询Answer信息*/
	@RequestMapping(value="/{answerId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer answerId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键answerId获取Answer对象*/
        Answer answer = answerService.getAnswer(answerId);

        List<Paper> paperList = paperService.queryAllPaper();
        request.setAttribute("paperList", paperList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("answer",  answer);
        return "Answer/answer_frontshow";
	}

	/*ajax方式显示学员答案修改jsp视图页*/
	@RequestMapping(value="/{answerId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer answerId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键answerId获取Answer对象*/
        Answer answer = answerService.getAnswer(answerId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonAnswer = answer.getJsonObject();
		out.println(jsonAnswer.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新学员答案信息*/
	@RequestMapping(value = "/{answerId}/update", method = RequestMethod.POST)
	public void update(@Validated Answer answer, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String answerFileFileName = this.handleFileUpload(request, "answerFileFile");
		if(!answerFileFileName.equals(""))answer.setAnswerFile(answerFileFileName);
		try {
			answerService.updateAnswer(answer);
			message = "学员答案更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "学员答案更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除学员答案信息*/
	@RequestMapping(value="/{answerId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer answerId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  answerService.deleteAnswer(answerId);
	            request.setAttribute("message", "学员答案删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "学员答案删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条学员答案记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String answerIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = answerService.deleteAnswers(answerIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出学员答案信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("paperObj") Paper paperObj,@ModelAttribute("userObj") UserInfo userObj,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(addTime == null) addTime = "";
        List<Answer> answerList = answerService.queryAnswer(paperObj,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Answer信息记录"; 
        String[] headers = { "答题试卷","答题学员","提交时间","学员成绩","评语"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<answerList.size();i++) {
        	Answer answer = answerList.get(i); 
        	dataset.add(new String[]{answer.getPaperObj().getExamName(),answer.getUserObj().getName(),answer.getAddTime(),answer.getScore(),answer.getEvaluate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Answer.xls");//filename是下载的xls的名，建议最好用英文 
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
