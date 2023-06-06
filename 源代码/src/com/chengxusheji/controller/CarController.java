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
import com.chengxusheji.service.CarService;
import com.chengxusheji.po.Car;
import com.chengxusheji.service.CarStateService;
import com.chengxusheji.po.CarState;

//Car管理控制层
@Controller
@RequestMapping("/Car")
public class CarController extends BaseController {

    /*业务层对象*/
    @Resource CarService carService;

    @Resource CarStateService carStateService;
	@InitBinder("carState")
	public void initBindercarState(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("carState.");
	}
	@InitBinder("car")
	public void initBinderCar(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("car.");
	}
	/*跳转到添加Car视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Car());
		/*查询所有的CarState信息*/
		List<CarState> carStateList = carStateService.queryAllCarState();
		request.setAttribute("carStateList", carStateList);
		return "Car_add";
	}

	/*客户端ajax方式提交添加车辆信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Car car, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(carService.getCar(car.getCarNo()) != null) {
			message = "车牌号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			car.setCarPhoto(this.handlePhotoUpload(request, "carPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        carService.addCar(car);
        message = "车辆添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询车辆信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String carNo,String carName,String shitWay,String onCardDate,@ModelAttribute("carState") CarState carState,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (carNo == null) carNo = "";
		if (carName == null) carName = "";
		if (shitWay == null) shitWay = "";
		if (onCardDate == null) onCardDate = "";
		if(rows != 0)carService.setRows(rows);
		List<Car> carList = carService.queryCar(carNo, carName, shitWay, onCardDate, carState, page);
	    /*计算总的页数和总的记录数*/
	    carService.queryTotalPageAndRecordNumber(carNo, carName, shitWay, onCardDate, carState);
	    /*获取到总的页码数目*/
	    int totalPage = carService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = carService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Car car:carList) {
			JSONObject jsonCar = car.getJsonObject();
			jsonArray.put(jsonCar);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询车辆信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Car> carList = carService.queryAllCar();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Car car:carList) {
			JSONObject jsonCar = new JSONObject();
			jsonCar.accumulate("carNo", car.getCarNo());
			jsonCar.accumulate("carName", car.getCarName());
			jsonArray.put(jsonCar);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询车辆信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String carNo,String carName,String shitWay,String onCardDate,@ModelAttribute("carState") CarState carState,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (carNo == null) carNo = "";
		if (carName == null) carName = "";
		if (shitWay == null) shitWay = "";
		if (onCardDate == null) onCardDate = "";
		List<Car> carList = carService.queryCar(carNo, carName, shitWay, onCardDate, carState, currentPage);
	    /*计算总的页数和总的记录数*/
	    carService.queryTotalPageAndRecordNumber(carNo, carName, shitWay, onCardDate, carState);
	    /*获取到总的页码数目*/
	    int totalPage = carService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = carService.getRecordNumber();
	    request.setAttribute("carList",  carList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("carNo", carNo);
	    request.setAttribute("carName", carName);
	    request.setAttribute("shitWay", shitWay);
	    request.setAttribute("onCardDate", onCardDate);
	    request.setAttribute("carState", carState);
	    List<CarState> carStateList = carStateService.queryAllCarState();
	    request.setAttribute("carStateList", carStateList);
		return "Car/car_frontquery_result"; 
	}

     /*前台查询Car信息*/
	@RequestMapping(value="/{carNo}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String carNo,Model model,HttpServletRequest request) throws Exception {
		/*根据主键carNo获取Car对象*/
        Car car = carService.getCar(carNo);

        List<CarState> carStateList = carStateService.queryAllCarState();
        request.setAttribute("carStateList", carStateList);
        request.setAttribute("car",  car);
        return "Car/car_frontshow";
	}

	/*ajax方式显示车辆修改jsp视图页*/
	@RequestMapping(value="/{carNo}/update",method=RequestMethod.GET)
	public void update(@PathVariable String carNo,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键carNo获取Car对象*/
        Car car = carService.getCar(carNo);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonCar = car.getJsonObject();
		out.println(jsonCar.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新车辆信息*/
	@RequestMapping(value = "/{carNo}/update", method = RequestMethod.POST)
	public void update(@Validated Car car, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String carPhotoFileName = this.handlePhotoUpload(request, "carPhotoFile");
		if(!carPhotoFileName.equals("upload/NoImage.jpg"))car.setCarPhoto(carPhotoFileName); 


		try {
			carService.updateCar(car);
			message = "车辆更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "车辆更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除车辆信息*/
	@RequestMapping(value="/{carNo}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String carNo,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  carService.deleteCar(carNo);
	            request.setAttribute("message", "车辆删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "车辆删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条车辆记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String carNos,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = carService.deleteCars(carNos);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出车辆信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String carNo,String carName,String shitWay,String onCardDate,@ModelAttribute("carState") CarState carState, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(carNo == null) carNo = "";
        if(carName == null) carName = "";
        if(shitWay == null) shitWay = "";
        if(onCardDate == null) onCardDate = "";
        List<Car> carList = carService.queryCar(carNo,carName,shitWay,onCardDate,carState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Car信息记录"; 
        String[] headers = { "车牌号","车辆名称","车辆照片","换挡方式","上牌日期","车辆状态","训练场地"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<carList.size();i++) {
        	Car car = carList.get(i); 
        	dataset.add(new String[]{car.getCarNo(),car.getCarName(),car.getCarPhoto(),car.getShitWay(),car.getOnCardDate(),car.getCarState().getStateName(),car.getCarPlace()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Car.xls");//filename是下载的xls的名，建议最好用英文 
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
