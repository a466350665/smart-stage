package openjoe.smart.stage.validation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 验证配置
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({MessageSource.class})
public class ValidationAutoConfiguration implements WebMvcConfigurer {

    private final MessageSource messageSource;

    public ValidationAutoConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 国际化处理
     */
    @Bean
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        // 验证模块支持兼容spring.messages设置的国际化文件
        bean.setValidationMessageSource(this.messageSource);
        return bean;
    }
}