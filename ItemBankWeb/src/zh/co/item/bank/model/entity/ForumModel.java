package zh.co.item.bank.model.entity;

import java.util.HashMap;
import java.util.Map;

import zh.co.item.bank.db.entity.VbForumBean;

public class ForumModel extends VbForumBean {

    private Map<Integer, String> userName = new HashMap<Integer, String>();

    /** 提问者 */
    private int asker;

    /** 问题状态 */
    private String statusName;

    /** 行号 */
    private int rowNo;

    /** 回答 */
    private String myResponse;

    private String responserName1;
    private String responserName2;
    private String responserName3;
    private String responserName4;
    private String responserName5;
    private String responserName6;
    private String responserName7;
    private String responserName8;
    private String responserName9;
    private String responserName10;

    public int getAsker() {
        return asker;
    }

    public void setAsker(int asker) {
        this.asker = asker;
    }

    public String getStatusName() {
        return statusName;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getResponserName1() {
        return userName.get(getResponser1());
    }

    public void setResponserName1(String responserName1) {
        this.responserName1 = responserName1;
    }

    public String getResponserName2() {
        return userName.get(getResponser2());
    }

    public void setResponserName2(String responserName2) {
        this.responserName2 = responserName2;
    }

    public String getResponserName3() {
        return userName.get(getResponser3());
    }

    public void setResponserName3(String responserName3) {
        this.responserName3 = responserName3;
    }

    public String getResponserName4() {
        return userName.get(getResponser4());
    }

    public void setResponserName4(String responserName4) {
        this.responserName4 = responserName4;
    }

    public String getResponserName5() {
        return userName.get(getResponser5());
    }

    public void setResponserName5(String responserName5) {
        this.responserName5 = responserName5;
    }

    public String getResponserName6() {
        return userName.get(getResponser6());
    }

    public void setResponserName6(String responserName6) {
        this.responserName6 = responserName6;
    }

    public String getResponserName7() {
        return userName.get(getResponser7());
    }

    public void setResponserName7(String responserName7) {
        this.responserName7 = responserName7;
    }

    public String getResponserName8() {
        return userName.get(getResponser8());
    }

    public void setResponserName8(String responserName8) {
        this.responserName8 = responserName8;
    }

    public String getResponserName9() {
        return userName.get(getResponser9());
    }

    public void setResponserName9(String responserName9) {
        this.responserName9 = responserName9;
    }

    public String getResponserName10() {
        return userName.get(getResponser10());
    }

    public void setResponserName10(String responserName10) {
        this.responserName10 = responserName10;
    }

    public Map<Integer, String> getUserName() {
        return userName;
    }

    public void setUserName(Map<Integer, String> userName) {
        this.userName = userName;
    }

    public String getMyResponse() {
        return myResponse;
    }

    public void setMyResponse(String myResponse) {
        this.myResponse = myResponse;
    }

}
