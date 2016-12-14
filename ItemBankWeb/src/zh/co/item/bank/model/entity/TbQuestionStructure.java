package zh.co.item.bank.model.entity;

import java.util.List;

import zh.co.item.bank.db.entity.TbQuestionStructureBean;

public class TbQuestionStructure extends TbQuestionStructureBean {

	private List<MediaModel> mediaQuestions;

	public List<MediaModel> getMediaQuestions() {
		return mediaQuestions;
	}

	public void setMediaQuestions(List<MediaModel> mediaQuestions) {
		this.mediaQuestions = mediaQuestions;
	}



}
