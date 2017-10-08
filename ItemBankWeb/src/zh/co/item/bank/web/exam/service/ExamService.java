package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import zh.co.common.dao.CodeDao;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.item.bank.db.entity.TbErrorReportBean;
import zh.co.item.bank.db.entity.TbExamListBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.ExamListModel;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.MediaModel;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
import zh.co.item.bank.web.exam.dao.ExamDao;

/**
 * 试题选择画面
 * 
 * @author gaoya
 *
 */
@Named
public class ExamService {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private CodeDao codeDao;

    @Inject
    private ExamDao examDao;

    @Inject
    private ExamCollectionDao examCollectionDao;

    /**
     * 考试类别
     * 
     * @return
     * @throws Exception
     */
    public List<TsCodeBean> getExams() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("modelId", "BQD0001");
        param.put("name", "exam");
        return getCodelist(param);
    }

    /**
     * 考题种别
     * 
     * @return
     * @throws Exception
     */
    public List<TsCodeBean> getExamTypes() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("modelId", "BQD0002");
        param.put("name", "exam_type");
        return getCodelist(param);
    }

    /**
     * JLPT等级
     * 
     * @return
     * @throws Exception
     */
    public List<TsCodeBean> getJlptLevels() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("modelId", "BQD0003");
        param.put("name", "jlpt_level");
        return getCodelist(param);
    }

    /**
     * J.TEST等级
     * 
     * @return
     * @throws Exception
     */
    public List<TsCodeBean> getJtestLevels() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("modelId", "BQD0003");
        param.put("name", "jtest_level");
        return getCodelist(param);
    }

    /**
     * 错误类型
     * 
     * @return
     * @throws Exception
     */
    public List<TsCodeBean> getReasons() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("modelId", "BER0001");
        param.put("name", "reason");
        return getCodelist(param);
    }

    /**
     * 取得CodeList
     * 
     * @return list
     */
    public List<TsCodeBean> getCodelist(Map<String, Object> param) throws Exception {
        return codeDao.selectCode(param);
    }

    /**
     * 智能选题
     * 
     * @param classityBean
     * @return
     */
    public List<ExamModel> smartSearch(Map<String, Object> map) {
        return examDao.selectCollectionByUserId(map);
    }

    /**
     * 文字，阅读等特殊试题
     * 
     * @param map
     * @return
     */
    public List<ExamModel> selectSpecialForOne(Map<String, Object> map) {
        return examDao.selectSpecialForOne(map);
    }

    /**
     * 检索ClassifyId
     * 
     * @param classifyBean
     * @return
     */
    public Integer getClassifyId(TbQuestionClassifyBean classifyBean) {
        return examDao.selectClassifyId(classifyBean);
    }

    /**
     * 根据用户选择检索试题
     * 
     * @param classityBean
     * @throws CmnBizException
     */
    public List<ExamModel> classifySearch(TbQuestionClassifyBean classifyBean, Map<String, Object> map)
            throws CmnBizException {
        // 取对应试题按钮
        Integer classifyId = getClassifyId(classifyBean);
        if (classifyId == null) {
            logger.log(MessageId.ITBK_E_0004);
            CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0004);
            throw ex;
        }
        return examDao.selectQuestionBySelection(map);
    }

    /**
     * 大题题目取得
     * 
     * @param structureId
     * @return
     */
    public String getTitle(Integer structureId) {
        return examDao.selectTitleById(structureId);
    }

    /**
     * 根据试题ID检索试题
     * 
     * @param questionId
     * @return
     */
    public ExamModel selectQuestionById(Integer questionId) {
        return examDao.selectQuestionByQuestionId(questionId);
    }

    /**
     * 根据试题ID检索试题（考试模式）
     * 
     * @param map[questionId,userId]
     * @return
     */
    public ExamModel selectReportDetailByQuestionId(Map<String, Object> map) {
        return examDao.selectReportDetailByQuestionId(map);
    }

    /**
     * 检索特殊试题
     * 
     * @param map
     * @return
     */
    public List<ExamModel> selectQuestionByFatherId(Integer fatherId) {
        return examDao.selectQuestionByFatherId(fatherId);
    }

    /**
     * 获取试题结构
     * 
     * @param map[exam,examType]
     * @return
     */
    public List<ExamModel> getStructure(TbQuestionClassifyBean classifyBean) {
        return examDao.getStructure(classifyBean);
    }

    /**
     * 获取对应试题
     * 
     * @param map[structure_id,source]
     * @return
     */
    public List<ExamModel> getTestQuestionBySource(Map<String, Object> map) {
        return examDao.getTestQuestionBySource(map);
    }

    /**
     * 删除本次做题记录
     * 
     * @param map
     */
    public void deleteExamCollectionBySource(Map<String, Object> map) {
        examCollectionDao.deleteExamCollectionBySource(map);
    }

    /**
     * 去最新一年的试题
     * 
     * @param map
     * @return
     */
    public String getYear(Map<String, Object> map) {
        return examDao.getYear(map);
    }

    /**
     * 试题报错
     */
    public void insertErrorReport(TbErrorReportBean bean) {
        examDao.insertErrorReport(bean);
    }

    /**
     * 取得考卷
     * 
     * @param userId
     * 
     * @return
     */
    public List<ExamListModel> getExamListForUser(Integer userId) {
        return examDao.getExamListForUser(userId);
    }

    /**
     * 获取成绩快速查询相关试题
     * 
     * @return
     */
    public List<TbExamListBean> getQuickSourceForAll() {
        return examDao.getQuickSourceForAll();
    }

    /**
     * 获取听力列表
     * 
     * @param classifyBean
     * @param id
     * @return
     */
    public List<MediaModel> getMediaList(TbQuestionClassifyBean classifyBean, Integer id) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", id);
        if (StringUtils.isNotEmpty(classifyBean.getExam())) {
            param.put("exam", classifyBean.getExam());
        }
        if (StringUtils.isNotEmpty(classifyBean.getJlptLevel())) {
            param.put("jlptLevel", classifyBean.getJlptLevel());
        }
        if (StringUtils.isNotEmpty(classifyBean.getJtestLevel())) {
            param.put("jtestLevel", classifyBean.getJtestLevel());
        }
        if (StringUtils.isNotEmpty(classifyBean.getExamType())) {
            param.put("examType", classifyBean.getExamType());
        }
        return examDao.getMediaList(param);
    }

    /**
     * 检索当日试题
     * @param userId 用户ID
     * 
     * @return
     */
    public ExamModel selectOneQuestion(Integer userId) {
        return examDao.selectOneQuestion(userId);
    }
}
