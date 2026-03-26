package openjoe.smart.stage.autoconfigure;

import openjoe.smart.stage.core.util.Message;
import openjoe.smart.stage.exception.ExceptionAutoConfiguration;
import openjoe.smart.stage.exception.GlobalExceptionHandler;
import openjoe.smart.stage.i18n.I18nAutoConfiguration;
import openjoe.smart.stage.validation.ValidationAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class StarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ExceptionAutoConfiguration.class,
                    I18nAutoConfiguration.class,
                    ValidationAutoConfiguration.class
            ))
            .withPropertyValues(
                    "smart.stage.i18n.enabled=true",
                    "spring.messages.basename=messages"
            );

    @Test
    void shouldLoadStarterBeansAndPluginMessages() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
            assertThat(context).hasSingleBean(MessageSource.class);
            assertThat(context).hasSingleBean(Message.class);
            assertThat(context).hasSingleBean(Validator.class);
            assertThat(context).hasSingleBean(MessageSourceProperties.class);

            Message message = context.getBean(Message.class);
            MessageSource messageSource = context.getBean(MessageSource.class);

            assertThat(message.getOrDefault("base.message", "fallback")).isEqualTo("base message");
            assertThat(message.getOrDefault("plugin.message", "fallback")).isEqualTo("plugin message");
            assertThat(messageSource.getMessage("plugin.message", null, Locale.getDefault())).isEqualTo("plugin message");
        });
    }
}
