package zh.co.item.bank.web.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.common.log.CmnLogger;
import zh.co.item.bank.db.entity.TbExamCollectionBean;
import zh.co.item.bank.db.entity.TbMediaCollectionBean;
import zh.co.item.bank.db.entity.VbExamRateBean;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
import zh.co.item.bank.web.exam.dao.MediaDao;

@Named
public class MergeDataService {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamCollectionDao examCollectionDao;

    @Inject
    private MediaDao mediaDao;

    /**
     * 取重复数据所在考卷（source， user_id）
     * 
     * @return
     */
    public List<VbExamRateBean> getDuplicateSource() {
        return examCollectionDao.getDuplicateSource();
    }

    /**
     * 删除考卷记录中的重复数据
     * 
     * @param examRateList
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteDuplicateCollections(List<VbExamRateBean> examRateList) {
        // 1.取考卷记录中的重复数据
        List<TbExamCollectionBean> examCollectionBeans = null;
        List<TbMediaCollectionBean> mediaCollectionBeans = null;
        List<VbExamRateBean> qList = new ArrayList<VbExamRateBean>();
        List<VbExamRateBean> mList = new ArrayList<VbExamRateBean>();
        for (VbExamRateBean examRateBean : examRateList) {
            int qRate = Integer.parseInt(examRateBean.getqRate().split("%")[0]);
            int mRate = Integer.parseInt(examRateBean.getmRate().split("%")[0]);
            if (qRate > 100) {
                logger.debug(String.format("userId[%s] source[%s] qRate[%s]", examRateBean.getUserId(),
                        examRateBean.getSource(), examRateBean.getqRate()));
                qList.add(examRateBean);
            }
            if (mRate > 100) {
                logger.debug(String.format("userId[%s] source[%s] qRate[%s]", examRateBean.getUserId(),
                        examRateBean.getSource(), examRateBean.getmRate()));
                mList.add(examRateBean);
            }
        }
        if (qList.size() > 0) {
            examCollectionBeans = examCollectionDao.getDuplicateCollections(qList);
        }
        if (mList.size() > 0) {
            mediaCollectionBeans = mediaDao.getDuplicateCollections(mList);
        }
        // 2.删除重复数据
        if (examCollectionBeans != null && examCollectionBeans.size() > 0) {
            examCollectionDao.deleteDuplicateCollections(examCollectionBeans);
        }
        if (mediaCollectionBeans != null && mediaCollectionBeans.size() > 0) {
            mediaDao.deleteDuplicateCollections(mediaCollectionBeans);
        }
    }

}
