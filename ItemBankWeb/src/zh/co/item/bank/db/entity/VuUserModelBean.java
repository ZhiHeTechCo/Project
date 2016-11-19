package zh.co.item.bank.db.entity;

import java.util.Date;

public class VuUserModelBean {
    private Integer id;

    private String name;

    private String password;

    private String role;

    private Date createTime;

    private Date updateTime;

    private Date birthday;

    private String telephone;

    private String email;

    private String jlptLevel;

    private String jtestLevel;

    private Integer point;

    private Integer contribution;

    private Date expireTime;

    private Long questionNum;

    private Long errorNum;
    
    private String nickName;
    
    private String wechat;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getJlptLevel() {
        return jlptLevel;
    }

    public void setJlptLevel(String jlptLevel) {
        this.jlptLevel = jlptLevel == null ? null : jlptLevel.trim();
    }

    public String getJtestLevel() {
        return jtestLevel;
    }

    public void setJtestLevel(String jtestLevel) {
        this.jtestLevel = jtestLevel == null ? null : jtestLevel.trim();
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

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Long getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Long questionNum) {
        this.questionNum = questionNum;
    }

    public Long getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Long errorNum) {
        this.errorNum = errorNum;
    }

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
    
}