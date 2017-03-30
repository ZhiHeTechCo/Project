package zh.co.item.bank.web.manage.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TbExamListBean;
import zh.co.item.bank.web.manage.dao.ExamListDao;

@Named
public class ExamListService {

    @Inject
    private ExamListDao examListDao;

    /**
     * 检索全部表数据
     * 
     * @return
     */
    public List<TbExamListBean> selectAll() {
        return examListDao.selectAll();
    }
}
