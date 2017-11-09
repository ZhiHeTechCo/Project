package zh.co.item.bank.web.exam.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.db.entity.TbCollectionDetailBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.dao.CollectionDao;
import zh.co.item.bank.web.exam.dao.CollectionDetailDao;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;

@Named
public class CollectionService {
    @Inject
    private CollectionDao collectionDao;

    @Inject
    private ExamCollectionDao examCollectionDao;

    @Inject
    private CollectionDetailDao collectionDetailDao;

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateCollection(ExamModel examModel) {

        // 记录表
        TbCollectionBean collection = selectCollectionForOne(examModel);
        // 做题详细表
        TbCollectionDetailBean collectionDetail = new TbCollectionDetailBean();

        // 用户ID
        collection.setId(examModel.getUserId());
        collectionDetail.setUserId(examModel.getUserId());

        // 试题ID
        Integer questionId = Integer.valueOf(examModel.getNo());
        collection.setQuestionId(questionId);
        collectionDetail.setQuestionId(questionId);

        // 我的答案
        collectionDetail.setMyAnswer(examModel.getMyAnswer());

        // finish
        boolean flag = examModel.getAnswer().equals(examModel.getMyAnswer());
        String finish = collection.getFinish();
        // 0:错误;1:一次正确;2:错误->正确;二次正确->错误3:二次正确;9:永久正确;
        if ("1".equals(finish) || "9".equals(finish)) {
            finish = flag ? "9" : "0";
        } else if ("0".equals(finish)) {
            finish = flag ? "2" : "0";
        } else if ("2".equals(finish)) {
            finish = flag ? "3" : "0";
        } else if ("3".equals(finish)) {
            finish = flag ? "1" : "2";
        }

        // 是否完成
        collection.setFinish(finish);

        collectionDao.updateCollection(collection);
        collectionDetailDao.insertCollectionDetail(collectionDetail);
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
        List<TbCollectionDetailBean> collectionDetails = new ArrayList<TbCollectionDetailBean>();
        for (int i = 0; i < questions.size(); i++) {

            ExamModel examModel = (ExamModel) questions.get(i);
            examModel.setUserId(userInfo.getId());
            // a-1.取当前用户当前题目做题记录
            TbCollectionBean collection = selectCollectionForOne(examModel);
            boolean isNew = collection.getQuestionId() == null ? true : false;
            // 做题详细表
            TbCollectionDetailBean collectionDetail = new TbCollectionDetailBean();

            // 用户ID
            collection.setId(userInfo.getId());
            collectionDetail.setUserId(userInfo.getId());

            // 试题ID
            Integer questionId = Integer.valueOf(examModel.getNo());
            collection.setQuestionId(questionId);
            collectionDetail.setQuestionId(questionId);

            //我的答案
            if (StringUtils.isEmpty(examModel.getMyAnswer()) && StringUtils.isEmpty(examModel.getA())) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("");
                if (StringUtils.isNotEmpty(examModel.getMyAnswer1())) {
                    buffer.append(examModel.getMyAnswer1());
                }
                if (StringUtils.isNotEmpty(examModel.getMyAnswer1())
                        || StringUtils.isNotEmpty(examModel.getMyAnswer2())) {
                    buffer.append(";");
                }
                if (StringUtils.isNotEmpty(examModel.getMyAnswer2())) {
                    buffer.append(examModel.getMyAnswer2());
                }
                examModel.setMyAnswer(buffer.toString());
            }
            collectionDetail.setMyAnswer(examModel.getMyAnswer());

            // finish
            String finish = collection.getFinish();

            // 本次是否做对 Flag
            boolean flag = StringUtils.isNotEmpty(examModel.getAnswer())
                    && examModel.getAnswer().equals(examModel.getMyAnswer());
            if (StringUtils.isEmpty(examModel.getAnswer())
                    && isAnswerEquals(examModel.getAnswer1(), examModel.getMyAnswer1())
                    && isAnswerEquals(examModel.getAnswer2(), examModel.getMyAnswer2())) {
                flag = true;
            }

            // 0:错误;1:一次正确;2:错误->正确;二次正确->错误3:二次正确;9:永久正确;
            if (StringUtils.isEmpty(finish)) {
                finish = flag ? "9" : "0";
            } else if ("1".equals(finish) || "9".equals(finish)) {
                finish = flag ? "9" : "0";
            } else if ("0".equals(finish)) {
                finish = flag ? "2" : "0";
            } else if ("2".equals(finish)) {
                finish = flag ? "3" : "0";
            } else if ("3".equals(finish)) {
                finish = flag ? "1" : "2";
            }

            // 是否完成
            collection.setFinish(finish);

            // 错题表登录·更新
            if (isNew) {
                collections.add(collection);

            } else {
                collectionDao.updateCollection(collection);
            }
            // 考试记录表登录
            if ("ing".equals(status) || "exist".equals(status)) {
                if (StringUtils.isEmpty(examModel.getAnswer())) {
                    String answer2 = StringUtils.isEmpty(examModel.getAnswer2()) ? "" : examModel.getAnswer2();
                    examModel.setAnswer(examModel.getAnswer1() + ";" + answer2);
                }
                examCollections.add(examModel);
            }
            // 做题记录详细登录
            collectionDetails.add(collectionDetail);
        }
        // a-1.批量登录做题记录表
        if (collections.size() != 0) {
            collectionDao.insertCollections(collections);
        }
        // a-2.批量登录考试做题记录表
        if (examCollections.size() != 0) {
            examCollectionDao.insertExamCollections(examCollections);
        }
        // a-3.批量登录做题详细表
        if (collectionDetails.size() != 0) {
            collectionDetailDao.insertCollectionDetails(collectionDetails);
        }
    }

    /**
     * 记叙题正误判断
     * 
     * @param answer
     * @param myAnswer
     * @return
     */
    private boolean isAnswerEquals(String answer, String myAnswer) {
        return StringUtils.isNotEmpty(answer) ? answer.equals(myAnswer) : true;
    }

}
