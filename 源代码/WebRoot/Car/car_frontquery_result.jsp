<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Car" %>
<%@ page import="com.chengxusheji.po.CarState" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Car> carList = (List<Car>)request.getAttribute("carList");
    //获取所有的carState信息
    List<CarState> carStateList = (List<CarState>)request.getAttribute("carStateList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String carNo = (String)request.getAttribute("carNo"); //车牌号查询关键字
    String carName = (String)request.getAttribute("carName"); //车辆名称查询关键字
    String shitWay = (String)request.getAttribute("shitWay"); //换挡方式查询关键字
    String onCardDate = (String)request.getAttribute("onCardDate"); //上牌日期查询关键字
    CarState carState = (CarState)request.getAttribute("carState");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>车辆查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>Car/frontlist">车辆信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>Car/car_frontAdd.jsp" style="display:none;">添加车辆</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<carList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		Car car = carList.get(i); //获取到车辆对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>Car/<%=car.getCarNo() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=car.getCarPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		车牌号:<%=car.getCarNo() %>
			     	</div>
			     	<div class="field">
	            		车辆名称:<%=car.getCarName() %>
			     	</div>
			     	<div class="field">
	            		换挡方式:<%=car.getShitWay() %>
			     	</div>
			     	<div class="field">
	            		上牌日期:<%=car.getOnCardDate() %>
			     	</div>
			     	<div class="field">
	            		车辆状态:<%=car.getCarState().getStateName() %>
			     	</div>
			     	<div class="field">
	            		训练场地:<%=car.getCarPlace() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>Car/<%=car.getCarNo() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="carEdit('<%=car.getCarNo() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="carDelete('<%=car.getCarNo() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>车辆查询</h1>
		</div>
		<form name="carQueryForm" id="carQueryForm" action="<%=basePath %>Car/frontlist" class="mar_t15">
			<div class="form-group">
				<label for="carNo">车牌号:</label>
				<input type="text" id="carNo" name="carNo" value="<%=carNo %>" class="form-control" placeholder="请输入车牌号">
			</div>
			<div class="form-group">
				<label for="carName">车辆名称:</label>
				<input type="text" id="carName" name="carName" value="<%=carName %>" class="form-control" placeholder="请输入车辆名称">
			</div>
			<div class="form-group">
				<label for="shitWay">换挡方式:</label>
				<input type="text" id="shitWay" name="shitWay" value="<%=shitWay %>" class="form-control" placeholder="请输入换挡方式">
			</div>
			<div class="form-group">
				<label for="onCardDate">上牌日期:</label>
				<input type="text" id="onCardDate" name="onCardDate" class="form-control"  placeholder="请选择上牌日期" value="<%=onCardDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <div class="form-group">
            	<label for="carState_stateId">车辆状态：</label>
                <select id="carState_stateId" name="carState.stateId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(CarState carStateTemp:carStateList) {
	 					String selected = "";
 					if(carState!=null && carState.getStateId()!=null && carState.getStateId().intValue()==carStateTemp.getStateId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=carStateTemp.getStateId() %>" <%=selected %>><%=carStateTemp.getStateName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="carEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;车辆信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="carEditForm" id="carEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="car_carNo_edit" class="col-md-3 text-right">车牌号:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="car_carNo_edit" name="car.carNo" class="form-control" placeholder="请输入车牌号" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="car_carName_edit" class="col-md-3 text-right">车辆名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="car_carName_edit" name="car.carName" class="form-control" placeholder="请输入车辆名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="car_carPhoto_edit" class="col-md-3 text-right">车辆照片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="car_carPhotoImg" border="0px"/><br/>
			    <input type="hidden" id="car_carPhoto" name="car.carPhoto"/>
			    <input id="carPhotoFile" name="carPhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="car_shitWay_edit" class="col-md-3 text-right">换挡方式:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="car_shitWay_edit" name="car.shitWay" class="form-control" placeholder="请输入换挡方式">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="car_onCardDate_edit" class="col-md-3 text-right">上牌日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date car_onCardDate_edit col-md-12" data-link-field="car_onCardDate_edit" data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="car_onCardDate_edit" name="car.onCardDate" size="16" type="text" value="" placeholder="请选择上牌日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="car_carDesc_edit" class="col-md-3 text-right">车辆介绍:</label>
		  	 <div class="col-md-9">
			 	<textarea name="car.carDesc" id="car_carDesc_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="car_carState_stateId_edit" class="col-md-3 text-right">车辆状态:</label>
		  	 <div class="col-md-9">
			    <select id="car_carState_stateId_edit" name="car.carState.stateId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="car_carPlace_edit" class="col-md-3 text-right">训练场地:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="car_carPlace_edit" name="car.carPlace" class="form-control" placeholder="请输入训练场地">
			 </div>
		  </div>
		</form> 
	    <style>#carEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxCarModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script>
//实例化编辑器
var car_carDesc_edit = UE.getEditor('car_carDesc_edit'); //车辆介绍编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.carQueryForm.currentPage.value = currentPage;
    document.carQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.carQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.carQueryForm.currentPage.value = pageValue;
    documentcarQueryForm.submit();
}

/*弹出修改车辆界面并初始化数据*/
function carEdit(carNo) {
	$.ajax({
		url :  basePath + "Car/" + carNo + "/update",
		type : "get",
		dataType: "json",
		success : function (car, response, status) {
			if (car) {
				$("#car_carNo_edit").val(car.carNo);
				$("#car_carName_edit").val(car.carName);
				$("#car_carPhoto").val(car.carPhoto);
				$("#car_carPhotoImg").attr("src", basePath +　car.carPhoto);
				$("#car_shitWay_edit").val(car.shitWay);
				$("#car_onCardDate_edit").val(car.onCardDate);
				car_carDesc_edit.setContent(car.carDesc, false);
				$.ajax({
					url: basePath + "CarState/listAll",
					type: "get",
					success: function(carStates,response,status) { 
						$("#car_carState_stateId_edit").empty();
						var html="";
		        		$(carStates).each(function(i,carState){
		        			html += "<option value='" + carState.stateId + "'>" + carState.stateName + "</option>";
		        		});
		        		$("#car_carState_stateId_edit").html(html);
		        		$("#car_carState_stateId_edit").val(car.carStatePri);
					}
				});
				$("#car_carPlace_edit").val(car.carPlace);
				$('#carEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除车辆信息*/
function carDelete(carNo) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Car/deletes",
			data : {
				carNos : carNo,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#carQueryForm").submit();
					//location.href= basePath + "Car/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交车辆信息表单给服务器端修改*/
function ajaxCarModify() {
	$.ajax({
		url :  basePath + "Car/" + $("#car_carNo_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#carEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#carQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

    /*上牌日期组件*/
    $('.car_onCardDate_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd',
    	minView: 2,
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

