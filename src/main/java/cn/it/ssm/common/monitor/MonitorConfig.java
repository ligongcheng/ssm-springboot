package cn.it.ssm.common.monitor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitorConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setEnabled(true);
        registrationBean.setFilter(new MonitorFilter());
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
