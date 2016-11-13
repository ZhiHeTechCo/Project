package zh.co.item.bank.web.sample;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] SampleManagedBean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("sampleManagedBean")
@Scope("session")
public class SampleManagedBean extends BaseController {

    @Inject
    private UserService sampleService;
    
    //list
    private List<TuUserBean> objectList;
    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_COM_001;
    }
    
    /**
     * initial
     * @return
     */
    public String init() {
        //objectList = sampleService.getObjectList();

        return SystemConstants.PAGE_ITBK_COM_001;
    }



	public List<TuUserBean> getObjectList() {
		return objectList;
	}



	public void setObjectList(List<TuUserBean> objectList) {
		this.objectList = objectList;
	}


}
