package zh.co.item.bank.web.exam.controller;

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
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.ScoreModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamCollectionService;
import zh.co.item.bank.web.exam.service.ExamScoreService;

/**
 * 成绩单画面
 * 
 * @author gaoya
 *
 */
@Named("examScoreController")
@Scope("session")
public class ExamScoreController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(getClass());

    @Inject
    private ExamCollectionService examCollectionService;

    @Inject
    private ExamScoreService examScoreService;

    private List<ScoreModel> scoreModels;

    private String width;

    private String report;

    private String gobackFlag;

    private String currentSource;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_009;
    }

    public String init() {
        return init(null);
    }

    /**
     * 1.[JTEST成绩单]画面初始化
     * 
     * @return
     */
    public String init(String source) {
        try {
            pushPathHistory("examScoreController");
            if (StringUtils.isEmpty(source)) {
                logger.log(MessageId.COMMON_E_0001);
                CmnBizException ex = new CmnBizException(MessageId.COMMON_E_0001);
                throw ex;
            }
            // a.初始化
            scoreModels = new ArrayList<ScoreModel>();
            currentSource = source;
            if (source.contains("A-D")) {
                report = "AD";
            } else if (source.contains("E-F")) {
                report = "EF";
            } else {
                report = null;
            }
            // b-1.本次考试出现的试题种别
            List<String> examTypes = examCollectionService.getReportTypes(source);
            // b-2.本次考试出现的大题
            List<String> structureIds = examCollectionService.getStructureIds(source);

            // c.取成绩单
            int readingTotal = 0;
            int myReadingTotal = 0;
            int listenTotal = 0;
            int myListenTotal = 0;
            // c-1.按试题种别取成绩
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("source", source);
            param.put("userId", WebUtils.getLoginUserInfo().getId());
            for (String examType : examTypes) {
                param.put("examType", examType);
                ScoreModel scoreModel = examCollectionService.getMyScoreByExamType(param);
                if (scoreModel != null) {
                    if ("6".equals(scoreModel.getExamType())) {
                        listenTotal = listenTotal + scoreModel.getTotalScore();
                        myListenTotal = myListenTotal + scoreModel.getMyTotalScore();
                    } else {
                        readingTotal = readingTotal + scoreModel.getTotalScore();
                        myReadingTotal = myReadingTotal + scoreModel.getMyTotalScore();
                    }
                    scoreModels.add(scoreModel);
                }
            }
            // c-2.按大题取成绩
            for (String structureId : structureIds) {
                param.put("structureId", structureId);
                ScoreModel scoreModel = examCollectionService.getMyScoreByStructureId(param);
                if (scoreModel != null) {
                    scoreModels.add(scoreModel);
                }
            }
            // c-3.总分设置
            examCollectionService.setTotalScore(scoreModels, myReadingTotal, readingTotal, myListenTotal, listenTotal,
                    source);

            // d.结果判断
            // d-1.成绩单取得失败
            if (scoreModels.size() == 0) {
                logger.log(MessageId.ITBK_E_0011);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0011);
                throw ex;
            }
            // d-2.成绩单取得成功
            // 设置宽度
            if (WebUtils.judgeIsMoblie()) {
                width = "100%";
            } else {
                width = "40%";
            }

        } catch (Exception e) {
            processForException(this.logger, e);
            // 停留在考试结果一览画面
            return SystemConstants.PAGE_ITBK_EXAM_006;
        }
        return getPageId();
    }

    /**
     * 2.返回结果一览画面（返回前页按钮）
     * 
     * @return
     */
    public String goBackToResult() {
        return SystemConstants.PAGE_ITBK_EXAM_006;
    }

    /**
     * 3.[重测]按钮
     * 
     * @return
     */
    public String reDoExam() {
        // 删除记录
        UserModel userInfo = WebUtils.getLoginUserInfo();
        examScoreService.prepareForRedo(currentSource, userInfo.getId());

        // 跳转至成绩录入
        ExamScoreInputController examScoreInputController = (ExamScoreInputController) SpringAppContextManager
                .getBean("examScoreInputController");
        return examScoreInputController.init();
    }

    /**
     * 4.[退出]按钮
     * 
     * @return
     */
    public String doExist() {
        // 转至模式选择
        ExamIndexController examIndexController = (ExamIndexController) SpringAppContextManager
                .getBean("examIndexController");
        return examIndexController.init();
    }

    public List<ScoreModel> getScoreModels() {
        return scoreModels;
    }

    public void setScoreModels(List<ScoreModel> scoreModels) {
        this.scoreModels = scoreModels;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getGobackFlag() {
        return gobackFlag;
    }

    public void setGobackFlag(String gobackFlag) {
        this.gobackFlag = gobackFlag;
    }

}
