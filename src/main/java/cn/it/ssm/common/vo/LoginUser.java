package cn.it.ssm.common.vo;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class LoginUser implements Serializable {

    @NotBlank(message = "用户名或密码不能为空")
    @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线")
    private String username;
    @NotBlank
    @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线")
    private String password;
    private String code;
    private String rememberMe;

}
