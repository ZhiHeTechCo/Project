package zh.co.item.bank.model.entity;

/**
 * <p>[概要] Data holder for HTML SELECT control Entity.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class OptionBean {

    private String value;
    private String label;

    public OptionBean() {

    }

    public OptionBean(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toString() {
        return new StringBuilder().append("{value=").append(value).append(", ")
                .append("label=").append(label).append("}").toString();
    }

}
