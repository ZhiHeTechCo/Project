package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.model.entity.ExamModel;

@Named
public class ResumeDao extends BaseDao {

    /**
     * 检索当前用户3天以上的错题
     * 
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> selectQuestionForError(Integer userId) {
        return getIbatisTemplate().selectList("Question.selectQuestionForError", userId);
    }

    /**
     * 检索全部错题
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> selectQuestionForErrorAll(Integer userId) {
        return getIbatisTemplate().selectList("Question.selectQuestionForErrorAll", userId);
    }

    /**
     * 根据FatherId检索错题全部信息
     * 
     * @param fatherId userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> selectErrorByFatherId(Map<String, Object> map) {
        return getIbatisTemplate().selectList("Question.selectErrorByFatherId", map);
    }

}
