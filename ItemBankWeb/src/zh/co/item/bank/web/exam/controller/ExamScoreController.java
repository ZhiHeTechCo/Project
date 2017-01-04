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
import zh.co.item.bank.web.exam.service.ExamCollectionService;

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

    private List<ScoreModel> scoreModels;

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
            // b.本次考试出现的试题种别
            List<String> examTypes = examCollectionService.getReportTypes(source);

            // c.取成绩单
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("source", source);
            for (String examType : examTypes) {
                param.put("examType", examType);
                ScoreModel scoreModel = examCollectionService.getMyScore(param);
                if (scoreModel != null) {
                    scoreModels.add(scoreModel);
                }
            }
            // c-1.成绩单取得失败
            if (scoreModels.size() == 0) {
                logger.log(MessageId.ITBK_E_0011);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0011);
                throw ex;
            }

        } catch (Exception e) {
            processForException(this.logger, e);
            return SystemConstants.PAGE_ITBK_EXAM_006;
        }
        // c-2.成绩单取得成功
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

    public List<ScoreModel> getScoreModels() {
        return scoreModels;
    }

    public void setScoreModels(List<ScoreModel> scoreModels) {
        this.scoreModels = scoreModels;
    }

}
