package zh.co.item.bank.db.entity;

import java.math.BigDecimal;

public class VuPointBean {
    private Integer userId;

    private BigDecimal point;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }
}