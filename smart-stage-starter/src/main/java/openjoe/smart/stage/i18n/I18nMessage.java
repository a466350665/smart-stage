package openjoe.smart.stage.i18n;

import openjoe.smart.stage.core.util.Message;
import openjoe.smart.stage.core.util.DefaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Objects;

/**
 * 国际化支持
 */
public class I18nMessage implements Message {

    private static final Logger log = LoggerFactory.getLogger(I18nMessage.class);

    private final MessageSource messageSource;

    public I18nMessage(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getOrDefault(String key, String defaultValue, Object... args) {
        String message = null;
        try {
            message = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return Objects.isNull(message) ? DefaultMessage.INSTANCE.getOrDefault(key, defaultValue, args) : message;
    }
}