package zh.co.item.bank.model.entity;

import java.util.List;

/**
 * TopicComment.xhtml画面显示Model
 * 
 * @author gaoya
 *
 */
public class TopicModel {

    private TopicListModel currentTopic;

    private List<TopicCommentModel> topicComments;

    private List<String> contextList;

    public TopicListModel getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentTopic(TopicListModel currentTopic) {
        this.currentTopic = currentTopic;
    }

    public List<TopicCommentModel> getTopicComments() {
        return topicComments;
    }

    public void setTopicComments(List<TopicCommentModel> topicComments) {
        this.topicComments = topicComments;
    }

    public List<String> getContextList() {
        return contextList;
    }

    public void setContextList(List<String> contextList) {
        this.contextList = contextList;
    }

}
