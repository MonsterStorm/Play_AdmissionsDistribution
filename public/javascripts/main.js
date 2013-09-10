//This the place for basic javascripts
/**
 * get page
 */
function getGetPage(domain, url) {
	ajaxBasic("/" + domain + "/" + url, url, "GET", "#content");
}

/**
 * Ajax函数封装，便于调用
 * 
 * @param 请求的Ajax
 *            URL，请求方式，发送的数据，用于接收返回结果的div
 * @author cst
 * @time 20110810
 */
function ajaxBasic(ajaxUrl, ajaxData, method, promptObj) {
	// 接收提示信息的div有一个默认值'#content'
	promptObj = promptObj || '#content';
	// 请求方式method有默认值get
	method = method || 'post';
	$.ajax({
		url : ajaxUrl,// 模糊搜索企业
		type : method, // 请求数据的方式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		async : true, // 是否是异步传输
		cache : false, // 使用缓存
		dataType : 'html', // 从服务器返回的数据类型
		data : ajaxData,// 传输到服务器的数据
		beforeSend : function() {
//			if($.browser.msie) {//IE ，Jquery1.x下才这样使用,2.x被废弃
//				$(promptObj).html('<div class="alert alert-info"><center>加载中...</center></div>');
//			} else {//非IE
				$(promptObj).html('<div class="progress progress-striped active"><div class="bar" style="width: 100%;">加载中...</div><div>');
//			}
			//$(promptObj).html('<center>加载中...</center>');
		},
		success : function(html) { // 请求成功的返回数据
			$(promptObj).html(html);
			return true;
		},
		error : function() {
			$(promptObj).html('<div class="alert alert-danger"><center>加载错误!</center></div>');
//			$(promptObj).html('<center>加载错误!</center>');
			return false;
		}
	});
}