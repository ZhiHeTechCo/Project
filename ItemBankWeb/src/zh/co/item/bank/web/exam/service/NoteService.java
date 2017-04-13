package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.common.constant.SystemConstants;
import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.db.entity.TuPointHistoryBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.NoteModel;
import zh.co.item.bank.web.exam.dao.NoteDao;
import zh.co.item.bank.web.user.dao.PointHistoryDao;

@Named
public class NoteService {

    @Inject
    private NoteDao noteDao;
    
    @Inject
    private PointHistoryDao pointHistoryDao;

    /**
     * 记笔记
     * 
     * @param noteBean
     */
    public void doNote(TbNoteBean noteBean) {
        // 登录笔记
        noteDao.insertNote(noteBean);
        // 积分事件登录
        TuPointHistoryBean tuPointHistoryBean = new TuPointHistoryBean();
        tuPointHistoryBean.setUserId(noteBean.getUserId());
        tuPointHistoryBean.setEvent(SystemConstants.EVENT_001);
        pointHistoryDao.insertPointHistory(tuPointHistoryBean);
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
