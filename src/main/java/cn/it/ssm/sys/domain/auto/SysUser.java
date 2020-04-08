package cn.it.ssm.sys.domain.auto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\w{4,12}$", message = "用户名必须为4-12位的字母、数字、下划线")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{4,12}$", message = "密码必须为4-12位的字母、数字、下划线")
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别
     * 0：女
     * 1：男
     * 2：保密
     */
    private Integer sex;
    /**
     * 职业类型：
     * 1：Java开发
     * 2：前端开发
     * 3：大数据开发
     * 4：ios开发
     * 5：Android开发
     * 6：Linux系统工程师
     * 7：PHP开发
     * 8：.net开发
     * 9：C/C++
     * 10：学生
     * 11：其它
     */
    private Integer job;
    /**
     * 头像地址
     */
    private String faceImage;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 用于权限的“盐”
     */
    private String authSalt;
    /**
     * 最后一次登录IP
     */
    private String lastLoginIp;

    private Integer isDelete;

    private Date registTime;
    /**
     * 最后一次登录时间
     */
    private Date lastLoginTime;
    private Integer deptId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthSalt() {
        return authSalt;
    }

    public void setAuthSalt(String authSalt) {
        this.authSalt = authSalt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", age=").append(age);
        sb.append(", sex=").append(sex);
        sb.append(", job=").append(job);
        sb.append(", faceImage=").append(faceImage);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", district=").append(district);
        sb.append(", address=").append(address);
        sb.append(", authSalt=").append(authSalt);
        sb.append(", lastLoginIp=").append(lastLoginIp);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", registTime=").append(registTime);
        sb.append(", deptId=").append(deptId);
        sb.append("]");
        return sb.toString();
    }
}
