package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.common.dao.CodeDao;
import zh.co.item.bank.db.entity.TbFirstLevelDirectoryBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.web.exam.dao.QuestionDao;

/**
 * 试题选择画面
 * 
 * @author gaoya
 *
 */
@Named
public class QuestionService {

    @Inject
    private CodeDao codeDao;

    @Inject
    private QuestionDao questionDao;

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
     * 取得CodeList
     * 
     * @return list
     */
    public List<TsCodeBean> getCodelist(Map<String, Object> param) throws Exception {
        return codeDao.selectCode(param);
    }

    /**
     * 登录试题
     * 
     * @param examModel
     */
    public void insertQuestion(ExamModel model) {
        if (!model.getSubject().isEmpty()) {
            // 登录一级表
            TbFirstLevelDirectoryBean firstLevelDirectoryBean = new TbFirstLevelDirectoryBean();
            firstLevelDirectoryBean.setSubject(model.getSubject());
            Integer id = questionDao.selectFLDByName(firstLevelDirectoryBean);
            if (id == null) {
                insertFirstLevelDirectory(firstLevelDirectoryBean);
                Integer lastId = questionDao.selectLastInsertId();
                model.setFatherId(lastId);
            } else {
                model.setFatherId(id);
            }
        }

        if (model.getSource().isEmpty()) {
            model.setSource(null);
        }
        if (model.getAnalysis().isEmpty()) {
            model.setAnalysis(null);
        }
        questionDao.insertQuestion(model);
    }

    /**
     * 更新试题
     * 
     * @param examModel
     */
    public void updateQuestion(ExamModel model) {
        if (!model.getSubject().isEmpty()) {
            // 更新一级表
            TbFirstLevelDirectoryBean firstLevelDirectoryBean = new TbFirstLevelDirectoryBean();
            firstLevelDirectoryBean.setSubject(model.getSubject());
            firstLevelDirectoryBean.setId(model.getFatherId());

            updateFirstLevelDirectory(firstLevelDirectoryBean);

        }

        if (model.getSource().isEmpty()) {
            model.setSource(null);
        }
        if (model.getAnalysis().isEmpty()) {
            model.setAnalysis(null);
        }
        questionDao.updateQuestion(model);
    }

    /**
     * 登录一级表
     */
    public void insertFirstLevelDirectory(TbFirstLevelDirectoryBean bean) {
        questionDao.insertFirstLevelDirectory(bean);
    }

    /**
     * 更新一级表
     */
    public void updateFirstLevelDirectory(TbFirstLevelDirectoryBean bean) {
        questionDao.updateFirstLevelDirectory(bean);
    }

    /**
     * 取得全部试题结构
     * 
     * @return
     */
    public List<TbQuestionStructureBean> getStructures() {
        return questionDao.selectStructureForAll();
    }

    /**
     * 根据选择获取大题目
     * 
     * @param classifyId
     * @return
     */
    public List<TbQuestionStructureBean> getStructuresByClassifyId(Integer classifyId) {
        return questionDao.getStructuresByClassifyId(classifyId);
    }

}
