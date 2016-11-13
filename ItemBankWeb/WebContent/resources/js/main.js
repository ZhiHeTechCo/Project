$(document).ready(function() {
	// ナビバーのマウス動作
	$('.navigation li').hover(
	// マウスイン
	function() {
		// サブメニュー表示
		$('ul', this).fadeIn(); // fadeIn will show the sub cat menu
	},
	// マウスアウト
	function() {
		// サブメニュー非表示
		$('ul', this).fadeOut(); // fadeOut will hide the sub cat menu
	});

	// フレームパス初期化
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


function logOut() {
    if (confirm("ログアウトします。よろしいですか？")) {
    	//$("#navigatorForm\\:logoutBtn").click();
    	document.getElementById('navigatorForm\:logoutBtn').click();
        logout = true;
        window.opener = null;
        window.open("", "_self");
        window.close(true);
    }
}

function menu(url) {
	$('.navigation li ul').fadeOut();     // fadeOut will hide the sub cat menu        

    //フレームパス初期化
    setIframePath('<%=request.getContextPath()%>/' + url);
}

function pageNavi(url) {
	window.location.href= contextPath + "/" + url + "?redirect=true";
}
