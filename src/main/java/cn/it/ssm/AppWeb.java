package cn.it.ssm;


import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class AppWeb {

    public static void main(String[] args) {
        SpringApplication.run(AppWeb.class, args);
    }

}
