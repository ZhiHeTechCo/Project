package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.NoteModel;
import zh.co.item.bank.web.exam.dao.NoteDao;

@Named
public class NoteService {

    @Inject
    private NoteDao noteDao;

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
