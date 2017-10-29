package zh.co.item.bank.db.entity;

public class VbExamRateBean {
    private String source;

    private Long userId;

    private Long current;

    private Long total;

    private String rate;

    private String qRate;

    private String mRate;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }

    public String getqRate() {
        return qRate;
    }

    public void setqRate(String qRate) {
        this.qRate = qRate == null ? null : qRate.trim();
    }

    public String getmRate() {
        return mRate;
    }

    public void setmRate(String mRate) {
        this.mRate = mRate == null ? null : mRate.trim();
    }
}