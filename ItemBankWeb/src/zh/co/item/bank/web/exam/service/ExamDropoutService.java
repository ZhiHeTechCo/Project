package zh.co.item.bank.web.exam.service;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TbExamDropoutBean;
import zh.co.item.bank.web.exam.dao.ExamDropoutDao;

@Named
public class ExamDropoutService {

    @Inject
    private ExamDropoutDao examDropoutDao;

    public void insertExamDropout(TbExamDropoutBean bean) {
        examDropoutDao.insert(bean);
    }

    public void deleteExamDropout(TbExamDropoutBean bean) {
        examDropoutDao.deleteExamDropout(bean);
    }
}
