package cn.it.ssm.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {

    private final CaptchaProperties captchaProperties;

    public CaptchaConfig(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    @Bean
    public CaptchaFactory captchaFactory() {
        return new CaptchaFactory(captchaProperties.width, captchaProperties.height, captchaProperties.len);
    }
}
