package cn.it.ssm.sys.controller;


import cn.it.ssm.common.annotation.ApiLimit;
import cn.it.ssm.common.annotation.Log;
import cn.it.ssm.common.entity.ConResult;
import cn.it.ssm.common.shiro.util.IPUtils;
import cn.it.ssm.common.shiro.util.PasswordUtil;
import cn.it.ssm.common.vo.OnlineUserVO;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.config.CaptchaFactory;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysUser;
import cn.it.ssm.sys.domain.vo.SysUserWithRole;
import cn.it.ssm.sys.service.SessionService;
import cn.it.ssm.sys.service.UserService;
import com.github.botaruibo.xvcode.generator.PngVCGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户控制器
 *
 * @author chengtao
 * @date 2020/07/18 10:34:52
 */
@Controller
@Slf4j
//@CacheConfig(cacheNames = "spring-cache",keyGenerator = "kg")
public class UserController extends BaseController {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    SessionService sessionService;

    /**
     * captcha 可以进行配置 查看 captchaConfig
     */
    @Autowired
    CaptchaFactory captchaFactory;

    @Autowired
    private UserService userService;

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        if (getSubject().isAuthenticated() || getSubject().isRemembered()) {
            return "redirect:/";
        }
        return "sys/login";
    }

    @RequiresPermissions("sys:user:list")
    @GetMapping("sys/userPage")
    public String user() {
        return "sys/user";
    }


    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param code     代码
     * @param request  请求
     * @return {@link ConResult}
     */
    @Log("用户登录")
    @PostMapping("sys/login")
    @ResponseBody
    public ConResult login(String username, String password, String code, HttpServletRequest request) {
        ConResult rs = ConResult.error();
        if (!StringUtils.isNotBlank(code)) {
            return rs.addMsg("验证码不能为空");
        }
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return rs.addMsg("用户名或密码不能为空");
        }
        boolean result = false;
        try {
            Subject subject = getSubject();
            Session session = super.getSession();
            String sessionCode = (String) session.getAttribute("_code");
            if (!code.toLowerCase().equals(sessionCode)) {
                return rs.addMsg("验证码错误,请重新输入");
            }
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            if (subject.isAuthenticated()) {
                SysUser user = new SysUser();
                user.setUsername(username);
                user.setLastLoginIp(IPUtils.getIpAddr(request));
                user.setLastLoginTime(session.getLastAccessTime());
                userService.updateUserLoginInfo(user);
                SysRole role = userService.findRoles(username).get(0);
                session.setAttribute("user", user);
                session.setAttribute("role", role);
                result = true;
            }
            log.info("用户[{}]:登录系统成功", username);
        } catch (ExcessiveAttemptsException e) {
            log.info("用户[{}]:尝试登录次数过多，请稍后再试!", username);
            return ConResult.error().addMsg("尝试登录次数过多，请稍后再试!");
        } catch (IncorrectCredentialsException | UnknownAccountException e) {
            log.info("用户[{}]:用户名或密码错误", username);
            return ConResult.error().addMsg("用户名或密码错误");
        } catch (LockedAccountException e) {
            log.info("用户[{}]:账号被锁定", username);
            return ConResult.error().addMsg("账号被锁定，请联系管理员");
        }

        return result ? ConResult.success().addMsg("登录成功！") : rs;
    }

    /**
     * 验证码
     *
     * @param response
     */
    @GetMapping(value = "gifCode")
    public void getCaptchaCode(HttpServletResponse response) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            //response.setContentType("image/gif");
            response.setContentType("image/png");
            PngVCGenerator generator = captchaFactory.pngVCGenerator();
            generator.write2out(response.getOutputStream());

            Session session = super.getSession();
            session.removeAttribute("_code");
            session.setAttribute("_code", generator.text().toLowerCase());
        } catch (Exception e) {
            log.error("generater captchaCode error");
        }
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("sys/user/register")
    @RequiresPermissions("sys:user:add")
    @ResponseBody
    public ConResult register(@Valid SysUser user) {
        ConResult rs = ConResult.error();
        if (userService.checkExistByUserName(user.getUsername())) {
            return rs.addMsg("用户名已存在");
        }
        user.setAuthSalt(PasswordUtil.getSalt());
        String id = UUID.randomUUID().toString().replace("-", "");
        String passwordEncypt = PasswordUtil.passwordEncypt(user.getPassword(), user.getAuthSalt());
        user.setId(id);
        user.setNickname(user.getUsername());
        user.setAge(22);
        user.setPassword(passwordEncypt);
        user.setIsDelete(0);
        user.setRegistTime(new Date());
        boolean result = userService.addResgiter(user);
        if (result) {
            return ConResult.success();
        }
        return rs;
    }

    /**
     * 根据用户id查找用户
     *
     * @param id
     * @return
     * @throws Exception
     */
    //@Cacheable
    @GetMapping("sys/user/{id}")
    @RequiresPermissions("sys:user:list")
    @ResponseBody
    public SysUser findByUserId(@PathVariable("id") String id) {
        return userService.findByUserId(id);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    //@CacheEvict(key = "#root.targetClass.name + ':getUserPageList:@'")
    @RequiresPermissions("sys:user:update")
    @PutMapping("sys/user")
    @ResponseBody
    public ConResult editUserInfo(SysUserWithRole user) {
        if (userService.editUserInfo(user)) {
            return ConResult.success();
        }
        return ConResult.error();
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return {@link ConResult}
     */
    @DeleteMapping("sys/user/{id}")
    @RequiresPermissions("sys:user:delete")
    @ApiLimit("10,2") //每个用户，5秒，只能调用6次
    @ResponseBody
    public ConResult deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ConResult.success();
    }

    /**
     * 禁用用户
     *
     * @param id id
     * @return {@link ConResult}
     */
    @PutMapping("sys/user/{id}/disable")
    @RequiresPermissions("sys:user:update")
    @ResponseBody
    public ConResult disableUser(@PathVariable String id) {
        userService.disableUser(id);
        return ConResult.success();
    }

    /**
     * 启用用户
     *
     * @param id id
     * @return {@link ConResult}
     */
    @PutMapping("sys/user/{id}/enable")
    @RequiresPermissions("sys:user:update")
    @ResponseBody
    public ConResult enableUser(@PathVariable String id) {
        userService.enableUser(id);
        return ConResult.success();
    }

    @GetMapping("sys/onlinePage")
    public String onlinePage() {
        return "sys/onlinePage";
    }

    /**
     * 在线用户
     *
     * @return
     */
    @GetMapping("sys/online")
    @ResponseBody
    public List<OnlineUserVO> onlineUser() {
        List<OnlineUserVO> onlineUser = sessionService.getOnlineUser();
        return onlineUser;
    }

    /**
     * 强制退出登录
     *
     * @param id
     */
    @PostMapping("sys/forcelogout")
    @ResponseBody
    public ConResult forcelogout(String id) {
        sessionService.kickOut(id);
        return ConResult.success();
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @RequiresPermissions("sys:user:list")
    @Log("获取用户列表")
    @ApiLimit("3,1") //每个用户，5秒，只能调用6次
    @GetMapping("sys/user")
    @ResponseBody
    public PageListVO getUserWithRole(TableRequest tableRequest) {
        PageListVO pageListVO = userService.findUserWithRoleList(tableRequest);
        redisConnectionFactory.getClass();
        return pageListVO;
    }

}
