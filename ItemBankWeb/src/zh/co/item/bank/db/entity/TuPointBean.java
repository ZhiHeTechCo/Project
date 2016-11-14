package zh.co.item.bank.db.entity;

import java.util.Date;

public class TuPointBean extends TuPointBeanKey {
    private Integer point;

    private Integer contribution;

    private Date expireTime;

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
}