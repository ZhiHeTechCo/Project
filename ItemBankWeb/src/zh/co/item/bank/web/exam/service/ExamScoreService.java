package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
import zh.co.item.bank.web.exam.dao.MediaDao;

@Named
public class ExamScoreService {

    @Inject
    private MediaDao mediaDao;

    @Inject
    private ExamCollectionDao examCollectionDao;

    /**
     * 成绩重测准备[速测]
     * 
     * @param currentSource
     * @param id
     */
    public void prepareForRedo(String currentSource, Integer id) {
        // 删除听力部分tb_media_collection
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", id);
        param.put("source", currentSource);
        mediaDao.deleteMediaCollectionBySource(param);
        // 删除tb_exam_collection
        examCollectionDao.deleteExamCollectionBySource(param);
    }

}
