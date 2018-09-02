package cn.it.ssm.config;

import com.github.botaruibo.xvcode.generator.GifVCGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaConfig {

    //TODO 必须配置 才能使用验证码
    @Value("${captcha.width}")
    private Integer width;

    @Value("${captcha.height}")
    private Integer height;

    @Value("${captcha.len}")
    private Integer len;

    @Bean
    public GifVCGenerator gifVCGenerator() {
        return new GifVCGenerator(width, height, len);
    }
}
