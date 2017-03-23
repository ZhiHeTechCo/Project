package zh.co.item.bank.web.exam.dao;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbNoteBean;

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
}
