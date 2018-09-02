package cn.it.ssm.web;

import cn.it.ssm.common.util.SpringContextUtils;
import cn.it.ssm.web.controller.PermissionController;
import cn.it.ssm.web.controller.UserController;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppWebTest {

    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    @Autowired
    UserController userController;
    @Autowired
    PermissionController permissionController;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(AppWebTest.class, args);
    }

    @Test
    @PerfTest(invocations = 100, threads = 1)
    public void permTree() {
        permissionController.permTree();
    }


    @Test
    /*@PerfTest(invocations = 1, threads = 1)*/
    public void ds() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = SpringContextUtils.getBean(LifecycleBeanPostProcessor.class);
    }

}
