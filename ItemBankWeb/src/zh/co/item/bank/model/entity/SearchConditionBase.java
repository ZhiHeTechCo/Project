package zh.co.item.bank.model.entity;

import zh.co.common.constant.SystemConstants;
import zh.co.common.prop.PropertiesUtils;

/**
 * <p>[概 要]检索条件</p>
 * <p>[備 考]检索条件JavaBean</p>
 * 
 * <p></p>
 * @author 王飞
 */
public class SearchConditionBase {
	
	private static final int DEFAULT_MAX_SEARCH_COUNT = 10000;
    private static final int DEFAULT_MAX_DISPLAY_COUNT = 1000;
	
	/** 最大検索件数 */
	private Integer maxSearchCount;
	/** 最大取得件数 */
	private Integer maxFetchCount;
	/** 最大表示件数 */
	private Integer maxDisplayCount;
	
	public SearchConditionBase() {
		String maxSearchCountStr = PropertiesUtils.getInstance().getSgValue(
				SystemConstants.PAGE_CONTROL_MAX_SEARCH_COUNT);
		maxSearchCount = maxSearchCountStr == null ? DEFAULT_MAX_SEARCH_COUNT
				: Integer.valueOf(maxSearchCountStr);
		String maxDisplayCountStr = PropertiesUtils.getInstance().getSgValue(
				SystemConstants.PAGE_CONTROL_MAX_DISPLAY_COUNT);
		maxDisplayCount = maxDisplayCountStr == null ? DEFAULT_MAX_DISPLAY_COUNT
				: Integer.valueOf(maxDisplayCountStr);
		maxFetchCount = maxSearchCount + 1;
	}
	
	public Integer getMaxSearchCount() {
		return maxSearchCount;
	}
	
	public void setMaxSearchCount(Integer maxSearchCount) {
		this.maxSearchCount = maxSearchCount;
	}
	
	public Integer getMaxDisplayCount() {
		return maxDisplayCount;
	}
	
	public void setMaxDisplayCount(Integer maxDisplayCount) {
		this.maxDisplayCount = maxDisplayCount;
	}

	public Integer getMaxFetchCount() {
		return maxFetchCount;
	}

	public void setMaxFetchCount(Integer maxFetchCount) {
		this.maxFetchCount = maxFetchCount;
	}

}
