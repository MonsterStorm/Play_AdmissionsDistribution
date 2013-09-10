// JavaScript Document
//**************************************主页的标签处理*******************************************
/*搜索部分*/
function go_search_tab(index){//处理登陆对话框的动作，index传入需要显示第几个tab，从1开始
	var search_tab = document.getElementById("page_search_tab_top").getElementsByTagName("li");
	var search_div = document.getElementById("page_search_tab_bottom").children;//获取孩子结点（直接孩子结点）
	for(var i = 0; i < search_tab.length; i++){
		if(i == (index - 1)){
			search_tab[i].className = "tabfocus";
			search_div[i].className = "divfocus";
		} else {
			search_tab[i].className = " ";
			search_div[i].className = "divhidden";
		}
	}
}


/*跳转到某一页*/
function gotoPage(url, target){
	target = target || "";
	if(target == '_parent'){//在父页中打开新页
	 	if(parent.location)//在FixFox下可用
	       parent.location.href = url;
	} else {
		window.location.href = url;
	}
}

/*点击以后改变加关注按钮的样式，变灰*/
function changeAttentionBtnStyle(btn){
	btn.className = "basic_add_attention_visited";
	console.dir(btn.getElementsByTagName('s'));
	btn.getElementsByTagName('s')[0].style.display = 'none';
	btn.getElementsByTagName('s')[1].style.display = 'block';
}


/*改变搜索结果条目背景样式*/
function changeStyle(p, flag){//传入p：要改变样式的元素，flag是指向还是离开
	if(flag)//指向的时候
		p.style.background="#ddffff";
	else //离开的时候
		p.style.background="#ffffff";
}



function hideSignedUserCard(){
	//var card = document.getElementById('card_signed_user');//获取元素
	//card.style.display="none";	
	$("#card_signed_user").hide(250);
}
///////////////////////////输入框控制///////////////////////////////////
/**
 * 清空输入文本框的值
 * @author cst
 * @time 20110810
 */
function clearPrompt(obj, flag){
	obj.isInput = obj.isInput || false;
	if(obj.isInput == false){//尚未输入
		obj.value="";
		obj.style.color="black";
	}
}
/**
 * 设置文本框的提示信息
 * @author cst
 * @time 20110810
 */
function setPrompt(obj, flag){
	obj.isInput = obj.isInput || false;
	if(obj.value == ""){
		obj.isInput = false;//如果没有输入内容的话，重置输入标志
	}
	if(obj.isInput == false){//尚未输入
		obj.style.color="#aaaaaa";
		obj.value = getPrompt(flag);
	}
}
/**
 * 获取提示信息
 * @param flag
 * @returns {String}
 * @author cst
 * @time 20110810
 */
function getPrompt(flag){
	switch(flag){
	case 0: return "请输入...";
	case 1: return "关键字...";
	}
}
/**
 * 当输入框接收了输入后改变输入状态
 * @param obj
 */
function changeInput(obj){
	obj.isInput = true;//设置是否输入数据了标志为true
}

/**
 * 统计字数，并限制字数
 * @param 总共字数，文本输入框，提示信息显示div的id
 * @author cst
 * @time 20110821
 */
function countWords(totalWords, editor, info){
	//改变字数统计标记
	//var currentWords = $(obj).val().length;//当前已经输入的字数
	//使用了KindEditor，修改统计字数
	var currentWords = editor.count('text');
	if(currentWords > totalWords){
		alert("对不起，最多" + totalWords + "字，内容被删减。。");
		$(info).html(0);
		//$(obj).val($(obj).val().substr(0, totalWords));
		//alert(editor.text());
		//editor.html(editor.text().substr(0, totalWords));
		while(editor.count('text')>220){
			editor.undo();
		}
		//return false;//返回false
	} else {
		$(info).html(totalWords - currentWords);
		//return true;//返回true
	}
}

/**
 * 重置字数统计
 * @param obj div名称，value 字数数值
 * @author cst
 * @time 20110822
 */
function resetCountWords(obj, value){
	$(obj).html(value);
}

///////////////////////////新鲜事回复控制///////////////////////////////////
/**
 * 新鲜事回复界面的打开与关闭
 * @author cst
 * @time 20110804
 */
function replyToggle(id, editor, options, textarea){
	var replyDiv = "#" + id;//要放置回复框的div，回复框就放置在该div后面
	if($("#replyContent").is(":hidden")){//如果回复框当前是不可见的话，则让其可见\
		//先清除Editor，使用JQuery的时候必须要先清除再重新生成
		editor.remove();
		$(replyDiv).after($("#replyContent"));//将恢复信息框移到正确的位置
		$("#replyContent").show();
		replyDiv = replyDiv + " a";//控制按钮
		$(replyDiv).html("隐藏");
		//设置回复信息域的reply_news_id,即新鲜事的id
		$("#reply_news_id").val(id);
		//$("#reply_content").val(getPrompt(0));//将文本框的内容置空
		//异步获取回复数据
		//ajax_getReply(id);//以前的获取5条的版本
		ajax_getNewsReplyList(id);//新的获取5条page的版本
		//重新生成Editor
		editor.create(textarea, options);
	} else {
		var flag = true;
		if(!(editor.isEmpty())/*$("#reply_content").val() != getPrompt(0)*/){
			flag = confirm("关闭后您所发布的信息会丢失，是否继续？");
		}
		if(flag == true){
			$("#replyContent").hide();//隐藏回复窗口
			//$('#reply_content').get(0).isInput = false;//重置输入框
			editor.html('');//清空
			//$('#reply_content').get(0).style.color="#aaaaaa";//重置输入框的样式
			$(".reply a").html("回复");//所有的按钮的文字都变
			resetCountWords("#wordLengthInfo2", '220');//字数统计恢复
		}
	}
}

/**
 * 使用Jquery的Ajax异步获取评论数据
 * @param id:新鲜事id
 * @author cst
 * @time 20110804
 */
function ajax_getReply(id){
	$.ajax({
		url : '../home/listNewsReply.action',	//接收请求的url
		type : 'post',		//请求数据的方式
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		async : true,		//是否是异步传输
		cache: false,		//是否使用缓存
		dataType : 'html',	//从服务器返回的数据类型
		data : 'newsId=' + id,	//传输到服务器的数据
		beforeSend : function() {	
			$('#totalReplies').html('<center>评论信息读取中...</center>');
		},
		success : function(html) {	//请求成功的返回数据
			$('#totalReplies').html(html);//更新评论信息
			return true;
		},
		error : function() {
			$('#totalReplies').html('<center>评论信息读取错误!</center>');
			return false;
		}
	});
}
/**
 * 得到回复列表
 */
function ajax_getNewsReplyList(id){
	$.ajax({
		url : '../home/getNewsReplyList.action',	//接收请求的url
		type : 'post',		//请求数据的方式
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		async : true,		//是否是异步传输
		cache: false,		//是否使用缓存
		dataType : 'html',	//从服务器返回的数据类型
		data : 'newsId=' + id,	//传输到服务器的数据
		beforeSend : function() {	
			$('#totalReplies').html('<center>评论信息读取中...</center>');
		},
		success : function(html) {	//请求成功的返回数据
			$('#totalReplies').html(html);//更新评论信息
			return true;
		},
		error : function() {
			$('#totalReplies').html('<center>评论信息读取错误!</center>');
			return false;
		}
	});
}
/**
 * 使用Jquery的Ajax异步提交新的回复
 * @param id
 * @author cst
 * @time 20110804
 */
function ajax_postNewReply(editor, textarea){
	if(getLoginInfoAjax('../login/login!checkLoginInfoByAjax.action','post')=="2"){//已登录
		//同步
		editor.sync();
		var newsId = $("#reply_news_id").val();
		var replyContent = $(textarea).val();
		if(editor.isEmpty()/*replyContent == "" || replyContent == getPrompt(0)*/){
			showPrompt('error', '回复内容不能为空');
			return;
		}
		$.ajax({
			url : '../home/postNewReply.action',	//接收请求的url
			type : 'post',		//请求数据的方式
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			async : true,		//是否是异步传输
			cache: false,		//是否使用缓存
			dataType : 'html',	//从服务器返回的数据类型
			data : 'newsId=' + newsId + "&replyContent=" + replyContent ,	//传输到服务器的数据
			beforeSend : function() {	
				$('#totalReplies').html('<center>评论信息提交中...</center>');
			},
			success : function(html) {	//请求成功的返回数据
				$('#totalReplies').html(html);//更新评论信息
				//$("#reply_content").val("");//将文本框的内容置空
				editor.html('');//清空
				resetCountWords("#wordLengthInfo2", '220');//字数统计恢复
				return true;
			},
			error : function() {
				$('#totalReplies').html('<center>加载评论信息错误!</center>');
				return false;
			}
		});
	}
}
/**
 * 
 * *****************************************建议部分************************************
 *  
 */
/**
 * 获取他人页面的，评估列表，建议列表，补充列表
 * @author cst
 * @time 20110816
 */
function ajax_getOtherAdditionInfo(flag, userId){
	var ajaxUrl;
	var ajaxData = "userId=" + userId;
	if(flag == 1){//评估列表
		ajaxUrl = "../home/getOtherAssessList.action";
	} else if (flag == 2){//建议列表
		ajaxUrl = "../home/getOtherSuggestionList.action";
	} else if (flag == 3){//补充列表
		ajaxUrl = "../home/getOtherAdditionList.action";
	}
	ajax_basic(ajaxUrl, ajaxData);
}


/**
 * 使用Jquery的Ajax异步发布一条新鲜事
 * @author cst
 * @time 20110809
 */
function ajax_postCompanyNews(editor){
	if(getLoginInfoAjax('../login/login!checkLoginInfoByAjax.action','post')=="2"){//已登录
		//使用Ajax，需要手动执行该函数
		editor.sync();
		var companyNewsContent = $("#postCompanyNews_content").val();
		var companyNewsTypeId = $("#postCompanyNews_infoType").val();
		var comId = $("#postCompanyNews_comId").val();
		if(companyNewsContent == null || companyNewsContent == "" || companyNewsContent == getPrompt(0)){
			showPrompt('error', '新鲜事内容不能为空!');
		} else {
			$.ajax({
				url : '../home/postCompanyNews.action',	//接收请求的url
				type : 'post',		//请求数据的方式
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				async : true,		//是否是异步传输
				cache: false,		//是否使用缓存
				dataType : 'html',	//从服务器返回的数据类型
				data : 'comId=' + comId + '&companyNewsTypeId=' + companyNewsTypeId + "&companyNewsContent=" + companyNewsContent ,	//传输到服务器的数据
				beforeSend : function() {
					showPrompt('info', '新鲜事发布中...', '', 1000);
				},
				success : function(html) {	//请求成功的返回数据
					$('#main_body_left_companyNews').html(html);//更新新鲜事信息
					editor.html("");//将文本框的内容置空
					resetCountWords("#wordLengthInfo", '220');//字数统计恢复
					return true;
				},
				error : function() {
					showPrompt('error', '发布新鲜事错误!');
					//$('#promptInfo').html('<center>发布新鲜事错误!</center>');
					return false;
				}
			});
		}
	}
}

/*****************************输入框输入回车执行动作*******************************************/
/**
 * 判断输入框的输入是否是回车，如果是则执行相应动作
 * @author cst
 * @time 20110818
 */
function inputKeyDown(event, flag, type){
	type = type || "search";//输入框所在类型，默认为搜索
	if (event.keyCode==13) {//回车键
		if(type == "search"){
			ajax_search(flag);
		}
	}
}
///////////////////////////分页跳转控制///////////////////////////////////

/**
 * 使用Jquery的Ajax异步分页跳转
 * @author cst
 * @time 20110809
 */
function ajax_jumpToPage(pageNo, resultObj){
	var page = $("#pageName").val();
	var orderBy = $("#orderBy").val();
	var order = $("#order").val();
	var pageUrl = $("#pageUrl").val();
	//设置一下接受返回结果的默认值
	resultObj = resultObj || "#main_body_left";
	
	$.ajax({
		url : pageUrl,	//接收请求的url
		type : 'post',		//请求数据的方式
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		async : true,		//是否是异步传输
		cache: false,		//是否使用缓存
		dataType : 'html',	//从服务器返回的数据类型
		data : page + '.pageNo=' + pageNo + '&' + page + '.orderBy=' + orderBy + '&' + page + '.order=' + order,
		beforeSend : function() {
			$('#promptInfo').html('<center>数据请求中...</center>');
		},
		success : function(html) {	//请求成功的返回数据
			$(resultObj).html(html);//更新结果信息
			$("#promptInfo").val("");//将文本框的内容置空
			return true;
		},
		error : function() {
			$('#promptInfo').html('<center>数据请求错误!</center>');
			return false;
		}
	});
}

function ajax_jumpToPage2(pageNo, resultObj){
	var page = $("#pageName2").val();
	var orderBy = $("#orderBy2").val();
	var order = $("#order2").val();
	var pageUrl = $("#pageUrl2").val();
	//设置一下接受返回结果的默认值
	resultObj = resultObj || "#main_body_left";
	
	$.ajax({
		url : pageUrl,	//接收请求的url
		type : 'post',		//请求数据的方式
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		async : true,		//是否是异步传输
		cache: false,		//是否使用缓存
		dataType : 'html',	//从服务器返回的数据类型
		data : page + '.pageNo=' + pageNo + '&' + page + '.orderBy=' + orderBy + '&' + page + '.order=' + order,
		beforeSend : function() {
			//$('#promptInfo').html('<center>数据请求中...</center>');
		},
		success : function(html) {	//请求成功的返回数据
			$(resultObj).html(html);//更新结果信息
			//$("#promptInfo").val("");//将文本框的内容置空
			return true;
		},
		error : function() {
			//$('#promptInfo').html('<center>数据请求错误!</center>');
			return false;
		}
	});
}
///////////////////////////------------///////////////////////////////////
///////////////////////////搜索控制///////////////////////////////////
/**
 * 企业类型联动
 * 取得某个企业类型的子类型
 * @author cst
 * @time 20110810
 */
function getChildrenTypes(obj, id){
	if(obj == '#search_business_2'){//如果选定第一层，则同时将第三次置空
		$('#search_business_3').html("");
		if(id == 0){//如果在第一层选中的是全部，还需要设置一下第二层的内容
			$('#search_business_2').html("");
		}
	} 
	if(id != 0){//0表示全部，不需要访问数据库
		$.ajax({
			url : '../search/getChildrenTypes.action',	//接收请求的url
			type : 'post',		//请求数据的方式
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			async : true,		//是否是异步传输
			cache: true,		//使用缓存
			dataType : 'html',	//从服务器返回的数据类型
			data : 'companyTypeId=' + id,	//传输到服务器的数据
			success : function(html) {	//请求成功的返回数据
				$(obj).html(html);//更新评论信息
				return true;
			}
		});
	}
}
/**
 * 产品类型联动
 * 取得某个产品类型的子类型
 * @author cst
 * @time 20110823
 */
function getProductChildrenTypes(obj, id){
	if(obj == '#search_business_2'){//如果选定第一层，则同时将第三次置空
		$('#search_business_3').html("");
		if(id == 0){//如果在第一层选中的是全部，还需要设置一下第二层的内容
			$('#search_business_2').html("");
		}
	} 
	if(id != 0){//0表示全部，不需要访问数据库
		$.ajax({
			url : '../product/getChildrenTypes.action',	//接收请求的url
			type : 'post',		//请求数据的方式
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			async : true,		//是否是异步传输
			cache: true,		//使用缓存
			dataType : 'html',	//从服务器返回的数据类型
			data : 'companyTypeId=' + id,	//传输到服务器的数据
			success : function(html) {	//请求成功的返回数据
				$(obj).html(html);//更新评论信息
				return true;
			}
		});
	}
}
/**
 * 搜企业的精确搜索和模糊搜索控制
 * @author cst
 * @time 20110810
 */
function search_toggle(flag){
	if(flag){//是否是关闭，true表示关闭地区和行业设置
		$("#search_toggle_1").attr("style", "color:gray;");//颜色变灰
		$("#search_toggle_1 select").attr("disabled", "disabled");//使控件不可用
		$("#search_toggle_2").attr("style", "color:gray;");//颜色变灰
		$("#search_toggle_2 select").attr("disabled", "disabled");//使控件不可用
		$("#search_toggle_3").attr("style", "color:gray;");//颜色变灰
		$("#search_toggle_3 select").attr("disabled", "disabled");//使控件不可用
		//在搜索框上添加一个标志位，用于后面进行不同的搜索定位
		$("#search_keyword_1").attr("exactSearch", true);
	} else {
		$("#search_toggle_1").attr("style", "color:black");//颜色变黑
		$("#search_toggle_1 select").removeAttr("disabled");//使控件可用
		$("#search_toggle_2").attr("style", "color:black");//颜色变黑
		$("#search_toggle_2 select").removeAttr("disabled");//使控件可用
		$("#search_toggle_3").attr("style", "color:black");//颜色变黑
		$("#search_toggle_3 select").removeAttr("disabled");//使控件可用
		//在搜索框上添加一个标志位，用于后面进行不同的搜索定位
		$("#search_keyword_1").attr("exactSearch", false);
	}
}
/**
 * 搜产品和网站的精确搜索和模糊搜索控制
 * @author cst
 * @time 20110810
 */
function search_toggle2(flag, obj){
	if(flag){//是否是关闭，true表示关闭地区和行业设置
		//在搜索框上添加一个标志位，用于后面进行不同的搜索定位
		$(obj).attr("exactSearch", true);
	} else {
		//在搜索框上添加一个标志位，用于后面进行不同的搜索定位
		$(obj).attr("exactSearch", false);
	}
}
/**
 * 搜索
 * @param flag
 * @author cst
 * @time 20110810
 */
function ajax_search(flag){
	//搜索类型
	var searchType;
	//搜索URL
	var ajaxUrl = '../search/searchInfo.action';

	if(flag == 1){//搜企业
		var exactSearch = $("#search_keyword_1").attr("exactSearch");
		//排序方式，传入的是Long
		var searchSortType = $("#search_sort_1").val();
		//搜索关键字，传入的是String
		var searchKeyword = $("#search_keyword_1").val();
		//去掉搜索关键字的两边的空白符号，trim在IE下不可用,需要使用Jquery的trim方法
		searchKeyword = $.trim(searchKeyword);
		if(searchKeyword == "" || searchKeyword == getPrompt(1)) {//没有输入关键字
			//弹出提示框
			showPrompt('error', "请输入搜索关键字!", '', 1000);
			return;
		}
		if(!exactSearch || exactSearch == false || exactSearch == 'false'){//模糊搜索，exact为undefined或者false时为精确搜索
			//地区和企业类型传入的是String
			var searchCompanyRegion = getMultiValue("#search_province", "#search_city", "#search_county");
			var searchCompanyType = getMultiValue("#search_business_1", "#search_business_2", "#search_business_3");
			//平台传入的是Long
			var searchCompanyPlatform = $("#search_platform").val();
			//发送ajax请求，进行模糊查询
			//搜索类型传入的是int
			searchType = 1;
			var ajaxData = 'searchType=' + searchType + '&searchCompanyRegion=' + searchCompanyRegion + '&searchCompanyType=' + searchCompanyType + '&searchCompanyPlatform=' + searchCompanyPlatform + '&searchSortType=' + searchSortType + '&searchKeyword=' + searchKeyword;
			ajax_basic(ajaxUrl, ajaxData);
		} else {//精确搜索
			//发送ajax请求，进行精确查询
			searchType = 2;
			var ajaxData = 'searchType=' + searchType + '&searchSortType=' + searchSortType + '&searchKeyword=' + searchKeyword;
			ajax_basic(ajaxUrl, ajaxData);
		}
	} else if (flag == 2){//搜产品
		var exactSearch = $("#search_keyword_2").attr("exactSearch");
		//排序方式，传入的是Long
		var searchSortType = $("#search_sort_2").val();
		//搜索关键字，传入的是String
		var searchKeyword = $("#search_keyword_2").val();
		//去掉搜索关键字的两边的空白符号
		searchKeyword = $.trim(searchKeyword);
		if(searchKeyword == "" || searchKeyword == getPrompt(1)) {//没有输入关键字
			showPrompt('error', "请输入搜索关键字!", '', 1000);
			return;
		}
		if(!exactSearch || exactSearch == false || exactSearch == 'false'){//模糊搜索，exact为undefined或者false时为精确搜索
			//发送ajax请求，进行模糊查询
			//搜索类型传入的是int
			searchType = 3;
			var ajaxData = 'searchType=' + searchType + '&searchSortType=' + searchSortType + '&searchKeyword=' + searchKeyword;
			ajax_basic(ajaxUrl, ajaxData);
		} else {//精确搜索
			//发送ajax请求，进行精确查询
			searchType = 4;
			var ajaxData = 'searchType=' + searchType + '&searchSortType=' + searchSortType + '&searchKeyword=' + searchKeyword;
			ajax_basic(ajaxUrl, ajaxData);
		}
	} else if (flag == 3){//搜网站
		var exactSearch = $("#search_keyword_3").attr("exactSearch");
		//排序方式，传入的是Long
		var searchSortType = $("#search_sort_3").val();
		//搜索关键字，传入的是String
		var searchKeyword = $("#search_keyword_3").val();
		//去掉搜索关键字的两边的空白符号
		searchKeyword = $.trim(searchKeyword);
		if(searchKeyword == "" || searchKeyword == getPrompt(1)) {//没有输入关键字
			showPrompt('error', "请输入搜索关键字!", '', 1000);
			return;
		}
		if(!exactSearch || exactSearch == false || exactSearch == 'false'){//模糊搜索，exact为undefined或者false时为精确搜索
			//发送ajax请求，进行模糊查询
			//搜索类型传入的是int
			searchType = 5;
			var ajaxData = 'searchType=' + searchType + '&searchSortType=' + searchSortType + '&searchKeyword=' + searchKeyword;
			ajax_basic(ajaxUrl, ajaxData);
		} else {//精确搜索
			//发送ajax请求，进行精确查询
			searchType = 6;
			var ajaxData = 'searchType=' + searchType + '&searchSortType=' + searchSortType + '&searchKeyword=' + searchKeyword;
			ajax_basic(ajaxUrl, ajaxData);
		}
	} else if (flag == 4){//搜个人
		//搜索关键字，传入的是String
		var searchKeyword = $("#search_keyword_4").val();
		//去掉搜索关键字的两边的空白符号
		searchKeyword = $.trim(searchKeyword);
		if(searchKeyword == "" || searchKeyword == getPrompt(1)) {//没有输入关键字
			showPrompt('error', "请输入搜索关键字!", '', 1000);
			return;
		}
		//发送ajax请求，进行精确查询
		searchType = 7;
		var ajaxData = 'searchType=' + searchType + '&searchKeyword=' + searchKeyword;
		ajax_basic(ajaxUrl, ajaxData);
	}
}

/**
 * 获取多个下拉列表的组合值，从第一个开始如果前面某一个的值为全部的话，则停止查找直接返回结果
 * @author cst
 * @time 20110812
 */
function getMultiValue(id1, id2, id3){
	var totalValue = "";
	//找到当前选中的option的文本
	var value1 = $(id1).find("option:selected").text();
	var value2 = $(id2).find("option:selected").text();
	var value3 = $(id3).find("option:selected").text();
	//去掉空白,trim在IE下不可用
	if(value1 != null)
		value1 = $.trim(value1);
	if(value2 != null)
		value2 = $.trim(value2);
	if(value3 != null)
		value3 = $.trim(value3);
	if(value1 == '全部' || value1 == null){
		return totalValue;
	} 
	totalValue = value1;
	if(value2 == '全部' || value2 == null){
		return totalValue;
	} else {
		totalValue = totalValue + ",";
	}
	
	totalValue = totalValue + value2;
	if(value3 == '全部' || value3 == null){
		return totalValue;
	} else {
		totalValue = totalValue + ",";
	}
	totalValue = totalValue + value3;
	return totalValue;
}



/**
 * Ajax函数封装，便于调用
 * @param 请求的Ajax URL，请求方式，发送的数据，用于接收返回结果的div
 * @author cst
 * @time 20110810
 */
function ajax_basic(ajaxUrl, ajaxData, method, promptObj){
	//接收提示信息的div有一个默认值'#main_body_left'
	promptObj = promptObj || '#main_body_left';
	//请求方式method有默认值get
	method = method || 'post';
	$.ajax({
		url : ajaxUrl,//模糊搜索企业
		type : method,		//请求数据的方式
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		async : true,		//是否是异步传输
		cache: false,		//使用缓存
		dataType : 'html',	//从服务器返回的数据类型
		data : ajaxData,//传输到服务器的数据
		beforeSend : function() {
			$(promptObj).html('<center>处理中...</center>');
		},
		success : function(html) {	//请求成功的返回数据
			$(promptObj).html(html);//更新新鲜事信息
			$("#main_body").removeAttr("style");//去掉首页的长度限制，这样内容就可以显示完整
			return true;
		},
		error : function() {
			$(promptObj).html('<center>错误!</center>');
			return false;
		}
	});
}

/*********************添加黑名单操作***********************/
function ajax_addBlacklist(comBName, comBId){
	if(getLoginInfoAjax('../login/login!checkLoginInfoByAjax.action','post')=="2"){//已登录
		var flag = confirm("你确定将 \"" + comBName + "\" 加入黑名单？");
		if(flag == true){//如果确定添加才添加黑名单
			$.ajax({
				url : "../society/addBlacklist.action",//模糊搜索企业
				type : "post",		//请求数据的方式
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				async : true,		//是否是异步传输
				cache: false,		//使用缓存
				dataType : 'html',	//从服务器返回的数据类型
				data : "comBId=" + comBId,//传输到服务器的数据
				success : function(html) {	//请求成功的返回数据
					showPrompt('info', html);
					return true;
				},
				error : function() {
					showPrompt('error', '添加黑名单错误!');
					return false;
				}
			});
		}
	}
}
/*********************************添加关注*****************************************/
/**
 * 添加所有关注
 * @author cst
 * @time 20110822
 */
function ajax_addAllAttention(comId){
	if(getLoginInfoAjax('../login/login!checkLoginInfoByAjax.action','post')=="2"){//已登录
		$.ajax({
			url : "../society/addAllAttention.action",//模糊搜索企业
			type : "post",		//请求数据的方式
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			async : true,		//是否是异步传输
			cache: false,		//使用缓存
			dataType : 'html',	//从服务器返回的数据类型
			data : "comId=" + comId,//传输到服务器的数据
			success : function(html) {	//请求成功的返回数据
				showPrompt('ok', '关注成功', '', 1000);
				$("#main_header_right").html(html);
				return true;
			},
			error : function() {
				showPrompt('error', '添加关注错误!');
				return false;
			}
		});
	}
}
/***************************他人页面的操作**********************************/
/**
 * 获取他人页面的，评估列表，建议列表，补充列表
 * @author cst
 * @time 20110816
 */
function ajax_getOtherList(flag, userId){
	var ajaxUrl;
	var ajaxData = "userId=" + userId;
	if(flag == 1){//评估列表
		ajaxUrl = "../home/getOtherAssessList.action";
	} else if (flag == 2){//建议列表
		ajaxUrl = "../home/getOtherSuggestionList.action";
	} else if (flag == 3){//补充列表
		ajaxUrl = "../home/getOtherAdditionList.action";
	}
	ajax_basic(ajaxUrl, ajaxData);
}
/********************************得到签约用户列表******************************************/
/**
 * 获取签约用户列表
 * @author cst
 * @time 20110819
 */
function getSignedUserList(){
	var ajaxUrl = "../index/listSignedUser.action";
	ajax_basic(ajaxUrl, "");
}
/*********************************绑定企业************************/
/**
 * 绑定企业
 * @author cst
 * @time 20110824
 */
function ajax_bingingCompany(comId){
	if(getLoginInfoAjax('../login/login!checkLoginInfoByAjax.action','post')=="2"){//已登录
		$.ajax({
			url : "../society/bindCompany.action",//模糊搜索企业
			type : "post",		//请求数据的方式
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			async : true,		//是否是异步传输
			cache: false,		//使用缓存
			dataType : 'html',	//从服务器返回的数据类型
			data : "comId=" + comId,//传输到服务器的数据
			success : function(html) {	//请求成功的返回数据
				//showPrompt('info', html);
				$("#main_header_right").html(html);
				return true;
			},
			error : function() {
				showPrompt('error', '绑定错误');
				return false;
			}
		});
	}
}
/**********************************Jquery的弹出式对话框，使用了JQuery插件****************/
function showPrompt(type, title, content, timeout, showTime){
	 var background;
	 var border;
	 //根据传入的参数选择设置不同的颜色
	 if(type == 'error'){//错误
		 background = '-moz-linear-gradient(0% 100% 90deg, rgba(252, 128, 114, 0.7), rgba(252, 128, 114, 0.4)) repeat scroll 0 0 transparent';
	     border = '1px solid rgba(255, 0, 0, 0.7)';
	 } else if (type == 'info'){//提示
         background = '-moz-linear-gradient(0% 100% 90deg, rgba(52, 172, 248, 0.7), rgba(52, 172, 248, 0.4)) repeat scroll 0 0 transparent';
         border = '1px solid rgba(0, 0, 255, 0.7)';
	 } else if (type == 'ok') {//完成
		background = '-moz-linear-gradient(0% 100% 90deg, rgba(30, 157, 68, 0.7), rgba(30, 157, 68, 0.4)) repeat scroll 0 0 transparent';
	    border = '1px solid rgba(0, 255, 0, 0.7)';
	 }
	 timeout = timeout || 2000;
	 if(showTime){//如果设置了时间属性
		 $.notty({
	         title : title,
	         content: content,
	         showTime: true,
	         timeout: timeout,
	         background: background,
	         border: border
	     });
	 } else {
		 $.notty({
	         title : title,
	         content: content,
	         showTime: false,
	         timeout: timeout,
	         background: background,
	         border: border
	     });
	 }
}