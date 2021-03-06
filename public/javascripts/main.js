//spin 进度条
var spinOptions = {
  lines: 12, // The number of lines to draw
  length: 20, // The length of each line
  width: 10, // The line thickness
  radius: 30, // The radius of the inner circle
  corners: 1, // Corner roundness (0..1)
  rotate: 0, // The rotation offset
  direction: 1, // 1: clockwise, -1: counterclockwise
  color: '#000', // #rgb or #rrggbb or array of colors
  speed: 1, // Rounds per second
  trail: 60, // Afterglow percentage
  shadow: true, // Whether to render a shadow
  hwaccel: false, // Whether to use hardware acceleration
  className: 'spinner', // The CSS class to assign to the spinner
  zIndex: 2e9, // The z-index (defaults to 2000000000)
  top: 'auto', // Top position relative to parent in px
  left: 'auto' // Left position relative to parent in px
};

var ajaxSubmitOptions = {
	extsFuns: {
	},
	beforeSubmit : function() {
		$('.myprompt').hide();
		var target = document.getElementById('content');
		var spinner = new Spinner(spinOptions).spin(target);
		//$('#content').html('<div class="progress progress-striped active"><div class="bar" style="width: 100%;">加载中...</div><div>');
		
		var exts = this.extsFuns;
		if(exts != undefined){
			var beforeFun = exts.beforeFun;
			if(beforeFun != undefined && typeof beforeFun == 'function'){
	            beforeFun();
	        }
		}
	},
	success : function(html) { // 请求成功的返回数据
		if(!html) return false;
		$('#content').html(html);
		$('#content').before('<div class="alert alert-success myprompt"><center>成功!</center></div>');
		$(".myprompt").fadeOut({speed:"slow"});
		
		var exts = this.extsFuns;
		if(exts != undefined){
			var successFun = exts.successFun;
			if(successFun != undefined && typeof successFun == 'function'){
				successFun(html);
			}
			this.extsFuns = {};
		}
		
		return true;
	},
	error : function(obj) {
		var message = obj.responseText || '服务器错误';
		$('#content').before('<div class="alert alert-danger myprompt"><center>' + message + '</center></div>');
		$('.spinner').hide();
		
		var exts = this.extsFuns;
		if(exts != undefined){
			var errorFun = exts.errorFun;
			if(errorFun != undefined && typeof errorFun == 'function'){
				errorFun(obj);
			}
			this.extsFuns = {};
		}
		
		return false;
	}
};


// This the place for basic javascripts
/**
 * Ajax函数封装，便于调用
 * 
 * @param 请求的Ajax
 *            URL，请求方式，发送的数据，用于接收返回结果的div
 * 
 * @param extFuns {
 * 			beforeFun: function,
 * 			successFun: function,
 * 			errorFun: function	
 * 		}
 * @author cst
 * @time 20110810
 */
function ajaxBasic(ajaxUrl, ajaxData, method, promptObj, extFuns) {
	// 接收提示信息的div有一个默认值'#content'
	promptObj = promptObj || '#content';
	// 请求方式method有默认值get
	method = method || 'post';
	var ajaxOptions = {
		url : ajaxUrl,
		type : method, // 请求数据的方式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		async : true, // 是否是异步传输
		cache : false, // 使用缓存
		dataType : 'html', // 从服务器返回的数据类型
		data : ajaxData,// 传输到服务器的数据
		beforeSend : function() {
			$('.myprompt').hide();
			$(promptObj).html('<div class="progress progress-striped active"><div class="bar" style="width: 100%;">加载中...</div><div>');
			
			if(extFuns != undefined){
				var beforeFun = extFuns.beforeFun;
				if(beforeFun != undefined && typeof beforeFun == 'function'){
		            beforeFun();
		        }
			}
			
			
		},
		success : function(html) {
			$(promptObj).html(html);
			
			if(extFuns != undefined){
				var successFun = extFuns.successFun;
				if(successFun != undefined && typeof successFun == 'function'){
					successFun(html);
				}
			}
			
			return true;
		},
		error : function(obj) {
			var message = obj.responseText || '服务器错误';
			$(promptObj).html('<div class="alert alert-danger"><center>' + message + '</center></div>');
			
			if(extFuns != undefined){
				var errorFun = extFuns.errorFun;
				if(errorFun != undefined && typeof errorFun == 'function'){
					errorFun(obj);
				}
			}
			
			return false;
		}
	}
	$.ajax(ajaxOptions);
}

/**
 * 异步删除
 * @param ajaxUrl
 * @param ajaxData
 * @param method
 * @param promptObj
 */
function ajaxDelete(ajaxUrl, ajaxData, method, promptObj){
	promptObj = promptObj || '#content';
	method = method || 'post';
	var ajaxOptions = {
		url : ajaxUrl,
		type : method,
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		async : true,
		cache : false,
		dataType : 'html',
		data : ajaxData,
		beforeSend : function() {
			$('.myprompt').hide();
			var target = document.getElementById('content');
			var spinner = new Spinner(spinOptions).spin(target);
		},
		success : function(html) {
			if(!html) return false;
			$(promptObj).html(html);
			$(promptObj).before('<div class="alert alert-success myprompt"><center>成功!</center></div>');
			$(".myprompt").fadeOut({speed:"slow"});
			return true;
		},
		error : function(obj) {
			var message = obj.responseText || '服务器错误';
			$(promptObj).before('<div class="alert alert-danger myprompt"><center>' + message + '</center></div>');
			$('.spinner').hide();
			return false;
		}
	}
	$.ajax(ajaxOptions);
}

/**
 * <form id="form1" runat="server">
        <input type='file' id="imgInp" />
        <img id="blah" src="#" alt="your image" />
    </form>
 * @param input
 */
function readURL(input, imgTarget) {
	imgTarget = imgTarget || "#myImgPreview";
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $(imgTarget).attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    }
}
