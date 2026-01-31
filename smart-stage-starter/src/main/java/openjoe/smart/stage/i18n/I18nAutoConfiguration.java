package openjoe.smart.stage.i18n;

import openjoe.smart.stage.core.util.Message;
import openjoe.smart.stage.core.util.MessageUtils;
import openjoe.smart.stage.plugin.PluginResourceParser;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnBooleanProperty({"smart.stage.i18n.enabled"})
@AutoConfigureBefore(MessageSourceAutoConfiguration.class)
@EnableConfigurationProperties({MessageSourceProperties.class})
public class I18nAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = {AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME})
    public MessageSource messageSource(MessageSourceProperties properties) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (!CollectionUtils.isEmpty(properties.getBasename())) {
            List<String> baseNameList = new ArrayList<>();
            baseNameList.addAll(properties.getBasename());
            // 获取所有插件的资源文件目录
            baseNameList.addAll(PluginResourceParser.parseMessages(baseNameList.get(0)));
            messageSource.setBasenames(baseNameList.toArray(new String[0]));
        }
        if (properties.getEncoding() != null) {
            messageSource.setDefaultEncoding(properties.getEncoding().name());
        }
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public Message i18nMessage(MessageSource messageSource) {
        Message message = new I18nMessage(messageSource);
        MessageUtils.setLocal(message);
        return message;
    }
}