$(document).ready(function() {
	// navigator
	$('.navigation li').hover(
	// 鼠标
	function() {
		// sub menu
		$('ul', this).fadeIn(); // fadeIn will show the sub cat menu
	},
	// 鼠标
	function() {
		// sub menu
		$('ul', this).fadeOut(); // fadeOut will hide the sub cat menu
	});

	// frame初始化
	// setIframePath(contextPath + '${request.getParameter("initPath")}');

	$("#win1").load(function() {
		resetIframeHeight();
	});
});

function setIframePath(path) {
	$("#win1").attr("src", path);
}

function resetIframeHeight() {
	var iframe = $("#win1")[0];
	if (iframe) {
		var iframeWin = iframe.contentWindow
				|| iframe.contentDocument.parentWindow;
		if (iframeWin.document.body) {
			iframe.height = iframeWin.document.documentElement.scrollHeight
					|| iframeWin.document.body.scrollHeight;
		}
		
		/*
		var title = iframeWin.document.getElementById("title");

		if (title && title.value && title.value != '') {
			document.title = title.value;
		} else if (title && title.innerText && title.innerText != '') {
			document.title = title.innerText;
		} else {
			document.title = iframeWin.document.title;
		}
		*/
	}

	setTimeout("resetIframeHeight()", 500);
}


function menu(url) {
	$('.navigation li ul').fadeOut();     // fadeOut will hide the sub cat menu        

    //frame初始化
    setIframePath('<%=request.getContextPath()%>/' + url);
}

function pageNavi(url) {
	window.location.href= contextPath + "/" + url + "?redirect=true";
}
