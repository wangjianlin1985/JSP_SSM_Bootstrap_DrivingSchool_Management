<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Answer" %>
<%@ page import="com.chengxusheji.po.Paper" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Answer> answerList = (List<Answer>)request.getAttribute("answerList");
    //获取所有的paperObj信息
    List<Paper> paperList = (List<Paper>)request.getAttribute("paperList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Paper paperObj = (Paper)request.getAttribute("paperObj");
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String addTime = (String)request.getAttribute("addTime"); //提交时间查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>学员答案查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#answerListPanel" aria-controls="answerListPanel" role="tab" data-toggle="tab">学员答案列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Answer/answer_frontAdd.jsp" style="display:none;">添加学员答案</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="answerListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>答题试卷</td><td>答题学员</td><td>作答文件</td><td>提交时间</td><td>学员成绩</td><td>评语</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<answerList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Answer answer = answerList.get(i); //获取到学员答案对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=answer.getPaperObj().getExamName() %></td>
 											<td><%=answer.getUserObj().getName() %></td>
 											<td><%=answer.getAnswerFile().equals("")?"暂无文件":"<a href='" + basePath + answer.getAnswerFile() + "' target='_blank'>" + answer.getAnswerFile() + "</a>"%>
 											<td><%=answer.getAddTime() %></td>
 											<td><%=answer.getScore() %></td>
 											<td><%=answer.getEvaluate() %></td>
 											<td>
 												<a href="<%=basePath  %>Answer/<%=answer.getAnswerId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="answerEdit('<%=answer.getAnswerId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="answerDelete('<%=answer.getAnswerId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

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
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>学员答案查询</h1>
		</div>
		<form name="answerQueryForm" id="answerQueryForm" action="<%=basePath %>Answer/frontlist" class="mar_t15">
            <div class="form-group">
            	<label for="paperObj_paperId">答题试卷：</label>
                <select id="paperObj_paperId" name="paperObj.paperId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(Paper paperTemp:paperList) {
	 					String selected = "";
 					if(paperObj!=null && paperObj.getPaperId()!=null && paperObj.getPaperId().intValue()==paperTemp.getPaperId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=paperTemp.getPaperId() %>" <%=selected %>><%=paperTemp.getExamName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <div class="form-group">
            	<label for="userObj_user_name">答题学员：</label>
                <select id="userObj_user_name" name="userObj.user_name" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(UserInfo userInfoTemp:userInfoList) {
	 					String selected = "";
 					if(userObj!=null && userObj.getUser_name()!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
	 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="addTime">提交时间:</label>
				<input type="text" id="addTime" name="addTime" class="form-control"  placeholder="请选择提交时间" value="<%=addTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="answerEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;学员答案信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="answerEditForm" id="answerEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="answer_answerId_edit" class="col-md-3 text-right">答案id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="answer_answerId_edit" name="answer.answerId" class="form-control" placeholder="请输入答案id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="answer_paperObj_paperId_edit" class="col-md-3 text-right">答题试卷:</label>
		  	 <div class="col-md-9">
			    <select id="answer_paperObj_paperId_edit" name="answer.paperObj.paperId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="answer_userObj_user_name_edit" class="col-md-3 text-right">答题学员:</label>
		  	 <div class="col-md-9">
			    <select id="answer_userObj_user_name_edit" name="answer.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="answer_answerFile_edit" class="col-md-3 text-right">作答文件:</label>
		  	 <div class="col-md-9">
			    <a id="answer_answerFileA" target="_blank"></a><br/>
			    <input type="hidden" id="answer_answerFile" name="answer.answerFile"/>
			    <input id="answerFileFile" name="answerFileFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="answer_addTime_edit" class="col-md-3 text-right">提交时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date answer_addTime_edit col-md-12" data-link-field="answer_addTime_edit">
                    <input class="form-control" id="answer_addTime_edit" name="answer.addTime" size="16" type="text" value="" placeholder="请选择提交时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="answer_score_edit" class="col-md-3 text-right">学员成绩:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="answer_score_edit" name="answer.score" class="form-control" placeholder="请输入学员成绩">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="answer_evaluate_edit" class="col-md-3 text-right">评语:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="answer_evaluate_edit" name="answer.evaluate" class="form-control" placeholder="请输入评语">
			 </div>
		  </div>
		</form> 
	    <style>#answerEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxAnswerModify();">提交</button>
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
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.answerQueryForm.currentPage.value = currentPage;
    document.answerQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.answerQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.answerQueryForm.currentPage.value = pageValue;
    documentanswerQueryForm.submit();
}

/*弹出修改学员答案界面并初始化数据*/
function answerEdit(answerId) {
	$.ajax({
		url :  basePath + "Answer/" + answerId + "/update",
		type : "get",
		dataType: "json",
		success : function (answer, response, status) {
			if (answer) {
				$("#answer_answerId_edit").val(answer.answerId);
				$.ajax({
					url: basePath + "Paper/listAll",
					type: "get",
					success: function(papers,response,status) { 
						$("#answer_paperObj_paperId_edit").empty();
						var html="";
		        		$(papers).each(function(i,paper){
		        			html += "<option value='" + paper.paperId + "'>" + paper.examName + "</option>";
		        		});
		        		$("#answer_paperObj_paperId_edit").html(html);
		        		$("#answer_paperObj_paperId_edit").val(answer.paperObjPri);
					}
				});
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#answer_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#answer_userObj_user_name_edit").html(html);
		        		$("#answer_userObj_user_name_edit").val(answer.userObjPri);
					}
				});
				$("#answer_answerFile").val(answer.answerFile);
				$("#answer_answerFileA").text(answer.answerFile);
				$("#answer_answerFileA").attr("href", basePath +　answer.answerFile);
				$("#answer_addTime_edit").val(answer.addTime);
				$("#answer_score_edit").val(answer.score);
				$("#answer_evaluate_edit").val(answer.evaluate);
				$('#answerEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除学员答案信息*/
function answerDelete(answerId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Answer/deletes",
			data : {
				answerIds : answerId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#answerQueryForm").submit();
					//location.href= basePath + "Answer/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交学员答案信息表单给服务器端修改*/
function ajaxAnswerModify() {
	$.ajax({
		url :  basePath + "Answer/" + $("#answer_answerId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#answerEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#answerQueryForm").submit();
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

    /*提交时间组件*/
    $('.answer_addTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
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

