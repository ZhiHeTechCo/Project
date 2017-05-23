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
     * 1.[非考试模式]听力画面初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("mediaExam");
            // a.对象初始化
            userInfo = WebUtils.getLoginUserInfo();
            examFlag = null;
            mediaQuestions = new ArrayList<MediaQuestionStructure>();
            this.mediaReady = "none";

            if (mediaModel != null) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("classifyId", mediaModel.getClassifyId());
                param.put("mediaId", mediaModel.getId());
                if ("100%".equals(mediaModel.getmRate())) {
                    param.put("userId", userInfo.getId());
                    // b-1.成绩一览
                    mediaQuestions = mediaService.selectMediaResult(param);
                } else {
                    // b-2.检索听力试题
                    mediaQuestions = mediaService.selectMediaQuestions(param);
                }
            }

            if (mediaQuestions == null || mediaQuestions.size() == 0) {
                // 题库已空
                logger.log(MessageId.ITBK_I_0010);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0010);
                throw ex;
            }
            // c.画面序号和显示设置
            CmnStringUtils.selectionLayoutSet(mediaQuestions);

        } catch (Exception e) {
            processForException(logger, e);
        }

        if ("100%".equals(mediaModel.getmRate())) {
            return SystemConstants.PAGE_ITBK_EXAM_008;
        } else {
            return getPageId();
        }

    }

    /**
     * 2.获取音频(ajax)
     */
    public void getMedia() {
        try {

            if (StringUtils.isEmpty(mediaModel.getMediaPath())) {
                this.mediaReady = "none";
            } else {
                mediaModel.setMedia(CmnStringUtils.getMedia(mediaModel.getMediaPath()));
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
            if (source != null) {
                // a.对象初始化
                userInfo = WebUtils.getLoginUserInfo();
                mediaModel = null;
                examFlag = SystemConstants.TRUE;
                mediaQuestions = new ArrayList<MediaQuestionStructure>();

                Map<String, Object> map = new HashMap<String, Object>();

                // b.获取音频
                mediaModel = mediaService.selectMediaBySource(source);
                if (mediaModel != null) {
                    map.put("mediaId", mediaModel.getId());
                    map.put("classifyId", mediaModel.getClassifyId());
                    // 获取大题目
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
        try {

            // a.初始化
            this.mediaReady = "none";
            mediaModel.setMedia(SystemConstants.EMPTY);

            // b.登录听力做题记录表和考试做题记录表
            mediaService.doInsertCollections(mediaQuestions, userInfo, mediaModel.getSource(), SystemConstants.TRUE);

        } catch (Exception e) {
            processForException(logger, e);
            return getPageId();
        }
        // c.去结果一览画面
        return SystemConstants.PAGE_ITBK_EXAM_008;
    }

    /**
     * 5.听力退出·返回试题选择
     * 
     * @return
     */
    public String goBackToClassify() {
        // 返回试题选择
        ExamClassifyController examClassifyController = (ExamClassifyController) SpringAppContextManager
                .getBean("examClassifyController");
        return examClassifyController.init();
    }

    /**
     * 6.【考试模式】返回考试结果一览画面并显示结果（点击查看考试成绩）
     * 
     * @return
     */
    public String goBackToExamResult() {
        ExamReportController examReportController = (ExamReportController) SpringAppContextManager
                .getBean("examReportController");
        examReportController.setMediaFlag("0");
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
