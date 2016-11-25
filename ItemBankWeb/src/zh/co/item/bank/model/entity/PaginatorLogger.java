package zh.co.item.bank.model.entity;

import java.util.HashMap;
import java.util.Map;

import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;


public class PaginatorLogger {
	
	
	private CmnLogger logger;
	
	private String pageId;
	
	private String prevEventId;
	
	private String nextEventId;
	
	private String gotoEventId;
	
	private Map<String, Object> params;
	
	public PaginatorLogger(CmnLogger logger, 
			String pageId,
			String prevEventId,
			String nextEventId,
			String gotoEventId) {
		this.logger = logger;
		this.pageId = pageId;
		this.prevEventId = prevEventId;
		this.nextEventId = nextEventId;
		this.gotoEventId = gotoEventId;
		this.params = new HashMap<String, Object>();
	}
	
	public void logPrev() {
	    BaseController.eventLog(logger, pageId, prevEventId, params);
	}
	
	public void logNext() {
	    BaseController.eventLog(logger, pageId, nextEventId, params);
	}
	
	public void logGoto() {
	    BaseController.eventLog(logger, pageId, gotoEventId, params);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
