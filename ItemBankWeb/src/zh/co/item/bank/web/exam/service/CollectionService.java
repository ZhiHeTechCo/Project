package zh.co.item.bank.web.exam.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.dao.CollectionDao;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;

@Named
public class CollectionService {
    @Inject
    private CollectionDao collectionDao;

    @Inject
    private ExamCollectionDao examCollectionDao;

    /**
     * 检索当前做题记录是否存在
     * 
     * @param examModel
     * @return
     */
    public TbCollectionBean selectCollectionForOne(ExamModel examModel) {
        TbCollectionBean collection = collectionDao.selectCollectionForOne(examModel);
        collection = collection == null ? new TbCollectionBean() : collection;
        return collection;
    }

    /**
     * 更新做题记录
     * 
     * @param collection
     */
    public void updateCollection(TbCollectionBean collection) {
        collectionDao.updateCollection(collection);
    }

    /**
     * 批量登录做题记录
     * 
     * @param questions
     * @param userInfo
     * @param status
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertCollections(List<ExamModel> questions, UserModel userInfo, String status) throws Exception {
        // 批量登录数据用
        List<TbCollectionBean> collections = new ArrayList<TbCollectionBean>();
        List<ExamModel> examCollections = new ArrayList<ExamModel>();
        for (int i = 0; i < questions.size(); i++) {

            ExamModel examModel = (ExamModel) questions.get(i);
            examModel.setUserId(userInfo.getId());
            // a-1.取当前用户当前题目做题记录
            TbCollectionBean collection = selectCollectionForOne(examModel);

            // 用户ID
            collection.setId(userInfo.getId());

            // 试题ID
            collection.setQuestionId(Integer.valueOf(examModel.getNo()));

            // 第几次做
            short count = collection.getCount() == null ? 0 : collection.getCount();
            count = (short) (count + 1);
            collection.setCount(count);

            // resultX
            String param = "setResult" + count;

            Method method = collection.getClass().getMethod(param, new Class[] { String.class });
            String choice = StringUtils.isEmpty(examModel.getMyAnswer()) ? "" : examModel.getMyAnswer();
            method.invoke(collection, new Object[] { choice });
            if (examModel.getAnswer().equals(choice)) {
                collection.setFinish("1");
            } else {
                collection.setFinish("0");
            }
            // 错题表登录·更新
            if (count == 1) {
                collections.add(collection);
            } else {
                updateCollection(collection);
            }
            // 考试记录表登录
            if ("ing".equals(status) || "exist".equals(status)) {
                examCollections.add(examModel);
            }
        }
        // a-1.批量登录做题记录表
        if (collections.size() != 0) {
            collectionDao.insertCollections(collections);
        }
        // a-2.批量登录考试做题记录表
        if (examCollections.size() != 0) {
            examCollectionDao.insertExamCollections(examCollections);
        }
    }

}
