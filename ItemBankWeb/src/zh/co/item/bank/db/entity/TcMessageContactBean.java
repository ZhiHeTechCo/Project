package zh.co.item.bank.db.entity;

public class TcMessageContractBean {
    private Integer tmpId;

    private String telephone;

    private String email;

    public Integer getTmpId() {
        return tmpId;
    }

    public void setTmpId(Integer tmpId) {
        this.tmpId = tmpId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
}