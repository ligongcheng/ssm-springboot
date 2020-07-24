package cn.it.ssm;


import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 应用入口
 *
 * @author chengtao
 * @date 2020/07/18 10:39:41
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class AppWeb {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AppWeb.class);
        application.setBannerMode(Banner.Mode.LOG);
        application.run(args);
    }
}
