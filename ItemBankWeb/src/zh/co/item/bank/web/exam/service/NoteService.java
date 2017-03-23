package zh.co.item.bank.web.exam.service;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.web.exam.dao.NoteDao;

@Named
public class NoteService {

    @Inject
    private NoteDao noteDao;

    /**
     * 登录笔记
     * 
     * @param question
     */
    public void insertNote(ExamModel question, Integer userId) {
        TbNoteBean bean = new TbNoteBean();
        bean.setUserId(userId);
        bean.setQuestionId(question.getNo());
        bean.setNote(question.getNote());
        noteDao.insertNote(bean);
    }
}
