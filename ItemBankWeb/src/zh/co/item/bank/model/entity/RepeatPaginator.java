package zh.co.item.bank.model.entity;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import zh.co.common.constant.SystemConstants;
import zh.co.common.prop.PropertiesUtils;

public class RepeatPaginator {
	
	private static final int DEFAULT_RECORDS_NUMBER = 20;
    private static final int DEFAULT_PAGE_INDEX = 1;
    private static final int DEFAULT_PAGE_RANGE = 7;

    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pagesTotal;
    private int fromIndex;
    private int toIndex;
    private Integer[] pageLinks;
    private int pageRange;
    private boolean ellipsisNeededBefore = false;
    private boolean ellipsisNeededAfter = false;
    private List<?> origModel;
    private List<?> model;
    PaginatorLogger logger;

    public RepeatPaginator(List<?> model, PaginatorLogger logger) {
        origModel = model;
        this.logger = logger;
		String recordsPerPage = PropertiesUtils.getInstance().getSgValue(
				SystemConstants.PAGE_CONTROL_RECORDS_PER_PAGE);
		records = recordsPerPage == null ? DEFAULT_RECORDS_NUMBER : Integer.valueOf(recordsPerPage);
        pageIndex = DEFAULT_PAGE_INDEX;
        pageRange = DEFAULT_PAGE_RANGE;
        recordsTotal = model.size();

        if (records > 0) {
            pagesTotal = records <= 0 ? 1 : recordsTotal / records;

            if (recordsTotal % records > 0) {
                pagesTotal++;
            }

            if (pagesTotal == 0) {
                pagesTotal = 1;
            }
        } else {
            records = 1;
            pagesTotal = 1;
        }

        updateModel();
    }

    public void updateModel() {
        fromIndex = (pageIndex * records) - records;
        toIndex = pageIndex * records;

        if(toIndex > recordsTotal) {
            toIndex = recordsTotal;
        }
        
        
        int firstLink = Math.max(pageIndex-(pageRange-1)/2, 2);
        int lastLink = Math.min(pageIndex+((pageRange-1)-(pageRange-1)/2), pagesTotal-1);
        int pageLength = Math.max(lastLink-firstLink+1, 0);
        pageLinks = new Integer[pageLength];
        if (pageLength > 0) {
        	for (int i=0; i<pageLinks.length; i++) {
        		pageLinks[i] = firstLink + i;
        	}
        	
        	ellipsisNeededBefore = (pageLinks[0] > 2);
            ellipsisNeededAfter = (pageLinks[pageLength-1] < pagesTotal-1);
        }

        model = origModel.subList(fromIndex, toIndex);
    }

    public void next() {
    	logger.logNext();
        if(pageIndex < pagesTotal) {
            pageIndex++;
        }

        updateModel();
    }
    
    public void goPage() {
    	logger.logGoto();
		FacesContext fc = FacesContext.getCurrentInstance();
	    Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	    pageIndex = Integer.valueOf(params.get("pageId")); 

        updateModel();
    }

    public void prev() {
    	logger.logPrev();
        if(pageIndex > 1) {
            pageIndex--;
        }

        updateModel();
    }   

    public int getRecords() {
        return records;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }
    
    public int getFirstPage() {
        return 1;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPagesTotal() {
        return pagesTotal;
    }
    
    public int getFromIndex() {
        return fromIndex;
    }
    
    public int getToIndex() {
        return toIndex;
    }
    
    public Integer[] getPageLinks() {
        return pageLinks;
    }
    
    public boolean getEllipsisNeededBefore() {
    	return ellipsisNeededBefore;
    }
    
    public boolean getEllipsisNeededAfter() {
    	return ellipsisNeededAfter;
    }

    public List<?> getModel() {
        return model;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

}
