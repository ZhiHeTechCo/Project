package zh.co.item.bank.web.exam.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.web.exam.dao.ResumeDao;

/**
 * 错题库
 * 
 * @author gaoya
 *
 */
@Named
public class ResumeService {

    @Inject
    private ResumeDao resumeDao;

    public List<ExamModel> selectQuestionForError(Integer userId) {
        return resumeDao.selectQuestionForError(userId);
    }

    public List<ExamModel> selectQuestionForErrorAll(Integer userId) {
        return resumeDao.selectQuestionForErrorAll(userId);
    }

    public List<ExamModel> selectErrorByFatherId(Integer fatherId) {
        return resumeDao.selectErrorByFatherId(fatherId);
    }
}
