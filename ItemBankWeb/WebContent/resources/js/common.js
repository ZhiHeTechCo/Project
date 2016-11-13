// 二重制御用フラグ,false:実施可；True：実施不可
var buttonDisabled = true;
var dateFormat = 'yyyy/mm/dd';
// 二重制御用Token
var pageToken;
/**
 * 画面初期化処理
 */
$(document).ready(function() {
	
	// カレンダーの共通設定
    if($.datepicker) {
    	// カレンダーの言葉設定
        $.datepicker.setDefaults($.datepicker.regional['ja']);
        // カレンダーの表示内容設定
        $.datepicker.setDefaults({showButtonPanel: true, closeText: '清除', changeMonth : true,changeYear : true, dateFormat : 'yy/mm/dd'});
        // カレンダーの表示様式設定とクリアボタンのエベント設定
        $.datepicker.setDefaults({
        	beforeShow: function(input){
        		setTimeout(datepickerBeforeShow,1, input);
        	},
        	onChangeMonthYear : function(year, month, inst){
        	    var id = inst.id.replace('\\\\', '\\');
        		setTimeout(datepickerBeforeShow,1, $("#" + id)[0]);
        	},
        });
    }
    // 全体 Enterキー制御
    setEnterKeyDisable();

    $(".calendar").each(function(){
    	resetCalendar($(this));
    });
    
    $("input.integer").each(function(){
    	resetInteger($(this));
    });
    
    $("textarea").each(function(){
    	resetTextarea($(this));
    });
    
    $("input[type='submit']").each(function(){
    	resetSubmitButton($(this));
    });
    
    $("form").each(function(){
    	// 全てのFormに二重制御用Tokenを追加する
    	$("<input type='hidden' id='pageToken' name='pageToken' value='"+pageToken+"'/>").appendTo($(this)).hide();
    	// 全てのActionはTokenチェックをディフォルトで設定する
        $("<input type='hidden' id='tokenValidator' name='tokenValidator' value='1'/>").appendTo($(this)).hide();
        
        $(this).submit(function(){
        	return buttonDisable(); 
        });
    });
    
    // 共通で戻るボタンをすると、画面遷移エラー画面に遷移するように設定する。
    window.addEventListener("unload", function() {
    	history.pushState(null, null, contextPath + '/xhtml/common/page_forward_error.xhtml');
    });
    
    buttonDisabled = false;
});

/**
 * ActionはTokenチェック不要の場合、該当処理する
 * ResponseはファイルStreamの場合は、noDisabledFlgは「1」で設定する
 */
function cancelToken(srcObj, noDisabledFlg){
	$(srcObj).parents("form").find("#tokenValidator").remove();
	$("<input type='hidden' id='tokenValidator' name='tokenValidator' value='0'/>").appendTo($(srcObj).parents("form")).hide();

	if(noDisabledFlg == '1'){
		setTimeout('resetButtonDisabled()',500);
	} else if(noDisabledFlg == '2'){
		setTimeout('resetButtonDisabled()',500);
		return buttonDisable();
	} else {
		return buttonDisable();
	}
	return true;
}

/**
 * Actionは実施か判断する
 */
function buttonDisable() {
    if( buttonDisabled ) {
        return false;
    }
    buttonDisabled = true;
    return true;
}

/**
 * 画面初期状態に戻る
 */
function resetButtonDisabled(){
	buttonDisabled = false;
	$("form").each(function(){
        $(this).find("#tokenValidator").remove();
    	$("<input type='hidden' id='tokenValidator' name='tokenValidator' value='1'/>").appendTo($(this)).hide();
    	
    	$(this).unbind("submit").submit(function(){
        	return buttonDisable(); 
        });
    });
	 
	setEnterKeyDisable();
}

/**
 * カレンダーの表示様式設定とクリアボタンのエベント設定
 */
function datepickerBeforeShow(input){
	if(input){
		$(".ui-datepicker-close").unbind("click").bind("click", function (){
			$.datepicker._clearDate( input ); 
	    });
		
		$(".ui-datepicker-current").unbind("click").bind("click", function (){
		    datepickerCurrent(input);
        });
	}
	$(".ui-datepicker select.ui-datepicker-year").css("width","45%");
	$(".ui-datepicker select.ui-datepicker-month").css("width","45%");
}

function datepickerCurrent(input) {
    var inst = $.datepicker._getInst(input);
    var date = new Date();
    inst.selectedDay = date.getDate();
    inst.drawMonth = inst.selectedMonth = date.getMonth();
    inst.drawYear = inst.selectedYear = date.getFullYear();
    
    $.datepicker._notifyChange(inst);
    $.datepicker._adjustDate(input);
    
    $(".ui-datepicker-close").unbind("click").bind("click", function (){
        $.datepicker._clearDate( input ); 
    });
    $(".ui-datepicker-current").unbind("click").bind("click", function (){
        datepickerCurrent(input);
    });
    
}
/**
 * 共通のカレンダーを設定
 */
function resetCalendar(srcObj){
	$(srcObj).attr("readonly",true).datepicker(); 
}

/**
 * 項目値は3桁ごとに、,を付与して表示する
 */
function resetInteger(srcObj){
	$(srcObj).css("text-align","right").css("width",$(srcObj).width());
	$(srcObj).bind("focus", function(){
		$(this).val($(this).val().replace(',', ''));
	});
	$(srcObj).bind("blur", function(){
		$(this).val($(this).val().replace(/\B(?=(?:\d{3})+\b)/g, ','));
	});
}
/**
 * TextareaのMaxlength設定
 */
function resetTextarea(srcObj){
	var maxLength = parseInt($(srcObj).attr("maxLength"));
	if(!maxLength){
    	var classes = $(srcObj).attr("class");
    	if(classes){
    		var r = classes.match(/maxLength\d+/i);
    		if(r){
    			maxLength = (r+"").toLowerCase() .replace("maxlength", "");
    		}
    	}
	}
	if(maxLength){
		$(srcObj).attr("maxLength", maxLength);
		/*
		$(this).bind("keyup keydown click", function(){
	    	$(this).val($(this).val().slice(0, maxLength));
	    });
	    */
	}
}

/**
 * ボタンの二重制御チェックする
 */
function resetSubmitButton(srcObj){

}


Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月
        "d+": this.getDate(), //日 
        "h+": this.getHours()%12 == 0 ? 12 : this.getHours()%12, //时 
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/** loop times */
var g_upfile_cnt = 10;

/** private : check then set message */
function selectFile_chk() {
    g_upfile_cnt--;
    var laberr = $('td.error');
    if (laberr && laberr.html()) {
        // alert(laberr.html());
        document.getElementById('ngossfw_error_display').innerHTML = laberr.html();
        document.getElementById('ngossfw_error_display').className = "warnMessage";
        laberr.parent().remove();
        resetButtonDisabled();
    } else {
        if (g_upfile_cnt >= 0)
            setTimeout('selectFile_chk()', 1000);
    }
}

/** interface for file size limit check */
function selectFile(obj, ofile_id) {
    // reset message
    document.getElementById('ngossfw_error_display').innerHTML = "";

    var ofile = document.getElementById(ofile_id);
    var filename = ofile.value;
    if (!ofile.onchange) {
        ofile.onchange = function() {
            g_upfile_cnt = 10;
            setTimeout('selectFile_chk()', 1000);
        };
    }
    ofile.click();
    var newFile = ofile.value;
    if(newFile == filename) {
    	resetButtonDisabled();
    }
}
/**
 * ページングのCallback設定
 */
function callbackAjaxForPaginator(data){
	switch (data.status) {
		case "success":
			resetButtonDisabled();
		    break;
	}
}

/**
 * 全体 Enterキー制御
 */
function setEnterKeyDisable(){
    $("input[type=text]").keydown(function(event){
        if(event.keyCode == 13) {
          event.preventDefault();
          return false;
        }
    });
    
    $("input[type=radio]").keydown(function(event){
        if(event.keyCode == 13) {
          event.preventDefault();
          return false;
        }
    });
    
    $("input[type=checkbox]").keydown(function(event){
        if(event.keyCode == 13) {
          event.preventDefault();
          return false;
        }
    });
}

function setCommaToValue(obj, orgMaxLength) {
	var resultValue = "";

	var id = obj.id.replace(":", "\\:");
	var value = obj.value.trim();
	var maxlength = parseInt($("#" + id).attr('maxlength'));

	if (value == '') {
		$("#" + id).val(resultValue);
		return;
	}

	value = obj.value.replace(',', '');
	// 整数部
	var integerValue = value;
	// 負数部
	var decimalValue = '';
	// 入力値が小数点を含む場合
	if (value.indexOf('.') != -1) {
		integerValue = value.substring(0, value.indexOf('.'));
		decimalValue = value.substring(value.indexOf('.'));
	}

	// 整数の先頭部と小数の後部中に0が含む場合
	// 0を削除する
	integerValue = integerValue.replace(/^0+/g, '');
	decimalValue = decimalValue.replace(/0+$/g, '');
	integerValue = integerValue == '' ? '0' : integerValue;
	decimalValue = decimalValue == '.' ? '' : decimalValue;

	// Substring(0, maxLength)
	var valueForremove0 = integerValue + decimalValue;
	if (valueForremove0.length > orgMaxLength) {
		valueForremove0 =  valueForremove0.substring(0, orgMaxLength);
		if (valueForremove0.indexOf('.') != -1) {
			integerValue = valueForremove0.substring(0, value.indexOf('.'));
			decimalValue = valueForremove0.substring(value.indexOf('.'));
		} else {
			integerValue = valueForremove0;
		}
	}
	maxlength = orgMaxLength;

	// 整数部で3個ごとに,を付ける
	resultValue = integerValue.replace(/\B(?=(?:\d{3})+\b)/g, ',');
	if (resultValue.length > maxlength && resultValue.indexOf(',') != -1) {
		maxlength = maxlength + resultValue.split(',').length - 1;
	}

	$("#" + id).val(resultValue + decimalValue);
	$("#" + id).attr('maxlength', maxlength);
}