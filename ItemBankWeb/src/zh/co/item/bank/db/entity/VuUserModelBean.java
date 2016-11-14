package zh.co.item.bank.db.entity;

import java.util.Date;

public class VuUserModelBean {
    private Integer id;

    private String name;

    private String password;

    private String role;

    private Date createtime;

    private Date updatetime;

    private Date birthday;

    private String telephone;

    private String email;

    private String jlptlevel;

    private String jtestlevel;

    private Integer point;

    private Integer contribution;

    private Date expiretime;

    private Long questionnum;

    private Long errornum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public String getJlptlevel() {
        return jlptlevel;
    }

    public void setJlptlevel(String jlptlevel) {
        this.jlptlevel = jlptlevel == null ? null : jlptlevel.trim();
    }

    public String getJtestlevel() {
        return jtestlevel;
    }

    public void setJtestlevel(String jtestlevel) {
        this.jtestlevel = jtestlevel == null ? null : jtestlevel.trim();
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getContribution() {
        return contribution;
    }

    public void setContribution(Integer contribution) {
        this.contribution = contribution;
    }

    public Date getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Date expiretime) {
        this.expiretime = expiretime;
    }

    public Long getQuestionnum() {
        return questionnum;
    }

    public void setQuestionnum(Long questionnum) {
        this.questionnum = questionnum;
    }

    public Long getErrornum() {
        return errornum;
    }

    public void setErrornum(Long errornum) {
        this.errornum = errornum;
    }
}