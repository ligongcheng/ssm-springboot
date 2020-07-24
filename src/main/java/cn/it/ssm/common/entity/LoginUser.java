package cn.it.ssm.common.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class LoginUser implements Serializable {

    private static final long serialVersionUID = -5542440924026890369L;

    @NotBlank(message = "用户名或密码不能为空")
    @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线")
    private String username;
    @NotBlank
    @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线")
    private String password;
    private String code;
    private String rememberMe;

    public LoginUser() {
    }

    public @NotBlank(message = "用户名或密码不能为空") @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线") String getUsername() {
        return this.username;
    }

    public void setUsername(@NotBlank(message = "用户名或密码不能为空") @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线") String username) {
        this.username = username;
    }

    public @NotBlank @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线") String getPassword() {
        return this.password;
    }

    public void setPassword(@NotBlank @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线") String password) {
        this.password = password;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRememberMe() {
        return this.rememberMe;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginUser)) return false;
        final LoginUser other = (LoginUser) o;
        if (!other.canEqual(this)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
        final Object this$rememberMe = this.getRememberMe();
        final Object other$rememberMe = other.getRememberMe();
        return this$rememberMe == null ? other$rememberMe == null : this$rememberMe.equals(other$rememberMe);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoginUser;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $code = this.getCode();
        result = result * PRIME + ($code == null ? 43 : $code.hashCode());
        final Object $rememberMe = this.getRememberMe();
        result = result * PRIME + ($rememberMe == null ? 43 : $rememberMe.hashCode());
        return result;
    }

    public String toString() {
        return "LoginUser(username=" + this.getUsername() + ", password=" + this.getPassword() + ", code=" + this.getCode() + ", rememberMe=" + this.getRememberMe() + ")";
    }
}
