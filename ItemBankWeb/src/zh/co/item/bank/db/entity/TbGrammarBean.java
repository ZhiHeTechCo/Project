package zh.co.item.bank.db.entity;

public class TbGrammarBean extends TbGrammarBeanKey {
    private String level;

    private String key;

    private String japanese;

    private String chinese;

    private String cource;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese == null ? null : japanese.trim();
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese == null ? null : chinese.trim();
    }

    public String getCource() {
        return cource;
    }

    public void setCource(String cource) {
        this.cource = cource == null ? null : cource.trim();
    }
}