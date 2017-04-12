package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.NoteModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.web.exam.dao.NoteDao;
import zh.co.item.bank.web.exam.dao.QuestionDao;

@Named
public class ExamResultService {

    @Inject
    private QuestionDao questionDao;

    @Inject
    private NoteDao noteDao;

    /**
     * 关联试题查询
     * 
     * @param examModel
     * @return
     */
    public List<QuestionStructure> searchCorrelationQuestions(ExamModel examModel) {
        String key = null;
        switch (examModel.getAnswer()) {
        case "1":
            key = examModel.getA();
            break;
        case "2":
            key = examModel.getB();
            break;
        case "3":
            key = examModel.getC();
            break;
        case "4":
            key = examModel.getD();
            break;
        default:
            break;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("questionId", examModel.getNo());
        return questionDao.searchCorrelationQuestions(map);
    }

    /**
     * 记笔记
     * 
     * @param noteBean
     */
    public void doNote(TbNoteBean noteBean) {
        noteDao.insertNote(noteBean);
    }

    /**
     * 检索笔记
     * 
     * @param questions
     * @param userId
     * @return
     */
    public List<NoteModel> selectNotesByQuestionId(List<ExamModel> questions, Integer userId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("questions", questions);
        param.put("userId", userId);
        return noteDao.selectNotesByQuestionId(param);
    }

    /**
     * 更新笔记
     * 
     * @param noteBean
     */
    public void updateNote(TbNoteBean noteBean) {
        noteDao.updateNote(noteBean);
    }
}
