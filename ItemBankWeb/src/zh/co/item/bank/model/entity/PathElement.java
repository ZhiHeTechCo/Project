package zh.co.item.bank.model.entity;


/**
 * <p>[概要] 页面，Title，Controller对应信息Entity</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class PathElement {

    private String pageId;
    
    private String pageTitle;
    
    private String pageController;

    /**
     * <p>[概 要]を取得する</p>
     * <p>[備 考]</p>
     *
     * @return pageId
     */
    public String getPageId() {
        return pageId;
    }

    /**
     * <p>[概 要]を設定する</p>
     * <p>[備 考]</p>
     *
     * @param pageId pageId
     */
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    /**
     * <p>[概 要]を取得する</p>
     * <p>[備 考]</p>
     *
     * @return pageTitle
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * <p>[概 要]を設定する</p>
     * <p>[備 考]</p>
     *
     * @param pageTitle pageTitle
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * <p>[概 要]を取得する</p>
     * <p>[備 考]</p>
     *
     * @return pageController
     */
    public String getPageController() {
        return pageController;
    }

    /**
     * <p>[概 要]を設定する</p>
     * <p>[備 考]</p>
     *
     * @param pageController pageController
     */
    public void setPageController(String pageController) {
        this.pageController = pageController;
    }

    

}
