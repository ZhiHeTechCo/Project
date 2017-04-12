package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.model.entity.NoteModel;

/**
 * 笔记
 * 
 * @author gyang
 *
 */
@Named
public class NoteDao extends BaseDao {

    /**
     * 登录数据
     * 
     * @param bean
     */
    public void insertNote(TbNoteBean bean) {
        getIbatisTemplate().insert("TbNote.insertSelective", bean);
    }

    /**
     * 检索笔记
     * 
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<NoteModel> selectNotesByQuestionId(Map<String, Object> param) {
        return getIbatisTemplate().selectList("Note.selectNotesByQuestionId", param);
    }

    /**
     * 更新笔记
     * 
     * @param noteBean
     */
    public void updateNote(TbNoteBean noteBean) {
        getIbatisTemplate().update("Note.updateNote", noteBean);
    }
}
