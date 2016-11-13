package zh.co.item.bank.db.entity;

public class TsAuthorityBeanKey {
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }
}