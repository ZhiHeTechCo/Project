
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
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamCollectionService;

/**
 * 考试结果一览画面
 * 
 * @author gaoya
 *
 */
@Named("examReportController")
@Scope("session")
public class ExamReportController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(getClass());

    @Inject
    private ExamCollectionService examCollectionService;

    private List<ExamModel> questions;

    private TbQuestionClassifyBean classifyBean;

    private UserModel userInfo;

    /** 画面显示变量 */
    private List<ExamReportModel> reportModels;
    
    List<QuestionStructure> currentQuestions;

    // 是否显示听力
    private String mediaFlag;

    // 是否显示成绩单
    private String jtestFlag;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_006;
    }

    public String init() {
        return init(null);
    }

    /**
     * 1.[考试结果一览]画面初始化
     * 
     * @return
     */
    public String init(String source) {
        try {
            pushPathHistory("examReportController");
            if (StringUtils.isEmpty(source)) {
                logger.log(MessageId.COMMON_E_0001);
                CmnBizException ex = new CmnBizException(MessageId.COMMON_E_0001);
                throw ex;
            }
            // a.变量初始化
            userInfo = (UserModel) WebUtils.getLoginUserInfo();

            reportModels = new ArrayList<ExamReportModel>();
            // 是否显示听力
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("userId", userInfo.getId());
            paramMap.put("source", source);
            String rate = examCollectionService.getMediaRate(paramMap);
            // 听力完成度100%显示出现【重做听力】选项
            mediaFlag = "100%".equals(rate) ? "2" : mediaFlag;
            // 初始化jtestFlag
            if (SystemConstants.FALSE.equals(jtestFlag)) {
                // 中途退出不显示成绩单
                jtestFlag = null;
            } else {
                // 根据source和听力完成度判断
                jtestFlag = source.contains("J.TEST") && "100%".equals(rate) ? SystemConstants.TRUE : null;
            }

            // b.显示本次考试结果
            // b-1.本次考试出现的试题种别
            List<String> examTypes = examCollectionService.getReportTypes(source);
            // b-2.对应种别正确率
            ExamReportModel param = new ExamReportModel();
            param.setSource(source);
            param.setUserId(userInfo.getId());
            for (String type : examTypes) {
                param.setExamType(type);
                ExamReportModel record = examCollectionService.getPercentage(param);
                reportModels.add(record);
            }
            // b-3.考试试题
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("source", source);
            map.put("userId", userInfo.getId());
            questions = examCollectionService.getExamReport(map);

            // 没有查询到当前考题的成绩
            if (questions == null || questions.size() == 0) {
                // 去试题选择
                ExamClassifyController classifyBean = (ExamClassifyController) SpringAppContextManager
                        .getBean("examClassifyController");
                return classifyBean.init();
            }

        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return getPageId();

    }

    /**
     * 2.[考试结果一览]转至听力画面
     * 
     * @return
     */
    public String toMediaExam() {
        try {

            String source = questions.get(0).getSource();
            // 重做本套听力，则删除之前记录
            if ("2".equals(mediaFlag)) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("userId", userInfo.getId());
                param.put("source", source);
                param.put("examType", "6");
                examCollectionService.deleteMediaCollectionBySource(param);
            }
            MediaExamController mediaExamController = (MediaExamController) SpringAppContextManager
                    .getBean("mediaExamController");
            // 题型设置为听力
            classifyBean.setExamType("6");
            mediaExamController.setStatus("ing");
            mediaExamController.setClassifyBean(classifyBean);
            return mediaExamController.mediaOfExam(source);

        } catch (Exception e) {
            processForException(this.logger, e);
        }

        return getPageId();
    }

    /**
     * 3.跳转至[试题详细]画面
     * 
     * @return
     */
    public String showDetail() {
        try {
            ExamDetailController examDetailController = (ExamDetailController) SpringAppContextManager
                    .getBean("questionDetail");
            return examDetailController.init();

        } catch (Exception e) {
            processForException(this.logger, e);
        }

        return getPageId();
    }

    /**
     * 4.返回试题选择
     * 
     * @return
     */
    public String goBackToClassify() {
        ExamClassifyController examClassifyController = (ExamClassifyController) SpringAppContextManager
                .getBean("examClassifyController");
        return examClassifyController.init();
    }

    /**
     * 5.显示成绩单
     * 
     * @return
     */
    public String toShowScore() {
        try {
            ExamScoreController examScoreController = (ExamScoreController) SpringAppContextManager
                    .getBean("examScoreController");
            examScoreController.setGobackFlag(SystemConstants.TRUE);
            return examScoreController.init(questions.get(0).getSource());

        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return getPageId();
    }

    /**
     * 6.浏览试卷
     * 
     * @return
     */
    public String showPaperDetail(String examType) {
        try {

            // 跳转至[试卷一览]画面
            ExamPaperController examPaperController = (ExamPaperController) SpringAppContextManager
                    .getBean("examPaperController");
            examPaperController.setSource(questions.get(0).getSource());
            examPaperController.setBeforePage(SystemConstants.PAGE_ITBK_EXAM_006);
            examPaperController.setExamType(examType);
            return examPaperController.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 7. 重做指定部分TODO
     * 
     * @return
     */
    public String reDo(String examType) {
        try {
            Integer[] structureIds = new Integer[5];
            int i = 0;
            for (ExamModel question : questions) {
                if (examType.equals(question.getExamType())) {
                    structureIds[i] = question.getStructureId();
                    i++;
                }
            }
            examCollectionService.reDo(userInfo.getId(), structureIds);

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    public List<ExamModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamModel> questions) {
        this.questions = questions;
    }

    public TbQuestionClassifyBean getClassifyBean() {
        return classifyBean;
    }

    public void setClassifyBean(TbQuestionClassifyBean classifyBean) {
        this.classifyBean = classifyBean;
    }

    public List<ExamReportModel> getReportModels() {
        return reportModels;
    }

    public void setReportModels(List<ExamReportModel> reportModels) {
        this.reportModels = reportModels;
    }

    public String getMediaFlag() {
        return mediaFlag;
    }

    public void setMediaFlag(String mediaFlag) {
        this.mediaFlag = mediaFlag;
    }

    public String getJtestFlag() {
        return jtestFlag;
    }

    public void setJtestFlag(String jtestFlag) {
        this.jtestFlag = jtestFlag;
    }

}
