package openjoe.smart.stage.i18n;

import openjoe.smart.stage.core.util.DefaultMessage;
import openjoe.smart.stage.core.util.MessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class MessageUtilsIntegrationTest {

    @AfterEach
    void tearDown() {
        MessageUtils.setLocal(DefaultMessage.INSTANCE);
    }

    @Test
    void shouldUseConfiguredMessageSourceWithoutStaticFieldLeak() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("greeting", Locale.getDefault(), "hello {0}");

        I18nMessage i18nMessage = new I18nMessage(messageSource);
        MessageUtils.setLocal(i18nMessage);

        assertThat(MessageUtils.getOrDefault("greeting", "fallback {0}", "smart-stage")).isEqualTo("hello smart-stage");
    }

    @Test
    void shouldFallbackToDefaultMessageWhenKeyIsMissing() {
        StaticMessageSource messageSource = new StaticMessageSource();

        I18nMessage i18nMessage = new I18nMessage(messageSource);
        MessageUtils.setLocal(i18nMessage);

        assertThat(MessageUtils.getOrDefault("missing.key", "fallback {0}", "value")).isEqualTo("fallback value");
    }
}
