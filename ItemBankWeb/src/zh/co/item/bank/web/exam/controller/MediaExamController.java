package zh.co.item.bank.web.exam.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.model.entity.MediaModel;
import zh.co.item.bank.model.entity.MediaQuestionStructure;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.MediaService;

/**
 * 听力画面
 * 
 * @author gaoya
 *
 */
@Named("mediaExamController")
@Scope("session")
public class MediaExamController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private MediaService mediaService;

    private TbQuestionClassifyBean classifyBean;

    /** --共通变量-- */
    // 用户信息
    private UserModel userInfo;

    // 考试进行状态
    private String status;

    // 考试
    private String examFlag;

    // 试题来源
    private String source;

    /** --听力模式用变量-- */
    // 音频
    MediaModel mediaModel;

    // 音频播放控制
    private String mediaReady;

    // 听力试题
    List<MediaQuestionStructure> mediaQuestions;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_007;
    }

    /**
     * 1.听力画面初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("mediaExam");
            // a.对象初始化
            userInfo = WebUtils.getLoginUserInfo();
            mediaModel = null;
            mediaQuestions = new ArrayList<MediaQuestionStructure>();
            this.mediaReady = "none";

            // b.检索听力试题
            selectForMediaQuestions();
            if (mediaQuestions == null || mediaQuestions.size() == 0) {
                // 题库已空
                logger.log(MessageId.ITBK_I_0010);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0010);
                throw ex;
            }
            // 画面序号和显示设置
            CmnStringUtils.selectionLayoutSet(mediaQuestions);

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();

    }

    /**
     * b.检索听力试题
     * 
     * @return
     * @throws IOException
     */
    private void selectForMediaQuestions() throws IOException {

        // b-1.获取ClassifyId
        List<Integer> classifyIds = mediaService.getClssifyId(classifyBean);
        if (classifyIds == null || classifyIds.size() == 0) {
            return;
        }

        // b-2.获取音频
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userInfo.getId());
        for (Integer classifyId : classifyIds) {
            map.put("classifyId", classifyId);
            mediaModel = mediaService.getMedia(map);
            if (mediaModel == null) {
                continue;
            } else {
                break;
            }
        }
        if (mediaModel == null) {
            return;
        }
        // b-3.获取大题目
        map.put("mediaId", mediaModel.getId());
        mediaQuestions = mediaService.selectMediaQuestions(map);
    }

    /**
     * 2.获取音频(ajax)
     */
    public void getMedia() {
        try {
            mediaModel.setMedia(CmnStringUtils.getMedia(mediaModel.getMediaPath()));
            if (StringUtils.isEmpty(mediaModel.getMedia())) {
                this.mediaReady = "none";
            } else {
                this.mediaReady = "block";
            }
        } catch (IOException e) {
            processForException(logger, e);
        }
    }

    /**
     * 3.【考试模式】开始听力部分
     * 
     * @return
     */
    public String mediaOfExam(String source) {
        try {
            pushPathHistory("mediaExam");
            if (source != null && classifyBean != null) {
                // 初始化
                mediaModel = null;
                examFlag = "true";
                mediaQuestions = new ArrayList<MediaQuestionStructure>();

                Map<String, Object> map = new HashMap<String, Object>();
                // 获取ClassifyId
                List<Integer> classifyIds = mediaService.getClssifyId(classifyBean);
                if (classifyIds.size() != 0) {
                    // 此处只会检索到一件
                    map.put("classifyId", classifyIds.get(0));

                    // 获取音频
                    mediaModel = mediaService.selectMediaBySource(source);
                    if (mediaModel != null) {
                        // 获取大题目
                        map.put("mediaId", mediaModel.getId());
                        mediaQuestions = mediaService.selectMediaQuestions(map);
                    }
                }
                if (mediaQuestions == null || mediaQuestions.size() == 0) {
                    // 题库已空
                    logger.log(MessageId.ITBK_E_0009);
                    CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0009);
                    throw ex;
                }
                // 画面序号和显示设置
                CmnStringUtils.selectionLayoutSet(mediaQuestions);
                // 转至听力
                return getPageId();
            }
        } catch (Exception e) {
            processForException(logger, e);
        }
        // 留在考试结果一览画面
        return SystemConstants.PAGE_ITBK_EXAM_006;
    }

    /**
     * 4.听力提交
     * 
     * @return
     */
    public String submitMedia() {

        // a.登录听力做题记录表和考试做题记录表
        mediaService.doInsertCollections(mediaQuestions, userInfo, mediaModel, examFlag);

        // b.去结果一览画面
        mediaModel.setMedia(SystemConstants.EMPTY);
        this.mediaReady = "none";
        return SystemConstants.PAGE_ITBK_EXAM_008;
    }

    /**
     * 5.听力退出·返回试题选择
     * 
     * @return
     */
    public String goBackToClassify() {
        // 返回试题一览画面
        ExamClassifyBean examClassifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
        return examClassifyBean.init();
    }

    /**
     * 6.【考试模式】返回考试结果一览画面并显示结果
     * 
     * @return
     */
    public String goBackToExamResult() {
        ExamReportController examReportController = (ExamReportController) SpringAppContextManager
                .getBean("examReportController");
        return examReportController.init(mediaModel.getSource());
    }

    public TbQuestionClassifyBean getClassifyBean() {
        return classifyBean;
    }

    public void setClassifyBean(TbQuestionClassifyBean classifyBean) {
        this.classifyBean = classifyBean;
    }

    public String getExamFlag() {
        return examFlag;
    }

    public void setExamFlag(String examFlag) {
        this.examFlag = examFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MediaModel getMediaModel() {
        return mediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        this.mediaModel = mediaModel;
    }

    public String getMediaReady() {
        return mediaReady;
    }

    public void setMediaReady(String mediaReady) {
        this.mediaReady = mediaReady;
    }

    public List<MediaQuestionStructure> getMediaQuestions() {
        return mediaQuestions;
    }

    public void setMediaQuestions(List<MediaQuestionStructure> mediaQuestions) {
        this.mediaQuestions = mediaQuestions;
    }

}
