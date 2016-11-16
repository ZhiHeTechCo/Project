package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.VuUserModelBean;

public class UserModel extends VuUserModelBean{

	//画面表示用生日
	private String  birthdayEx;

	public String getBirthdayEx() {
		return birthdayEx;
	}

	public void setBirthdayEx(String birthdayEx) {
		this.birthdayEx = birthdayEx;
	}
	
	
}
