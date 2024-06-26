package openjoe.smart.stage.i18n.entity;

import openjoe.smart.stage.core.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Objects;

/**
 * 国际化支持
 */
public class I18nMessage extends Message {

    private static final Logger log = LoggerFactory.getLogger(I18nMessage.class);

    private static MessageSource messageSource;

    public I18nMessage(MessageSource messageSource) {
        Message.local = this;
        I18nMessage.messageSource = messageSource;
    }

    public static String get(Locale locale, String key, Object... args) {
        return getOrDefault(locale, key, key, args);
    }

    public static String getOrDefault(Locale locale, String key, String defaultValue, Object... args) {
        return getLocaleMessageOrDefault(locale, key, defaultValue, args);
    }

    private static String getLocaleMessageOrDefault(Locale locale, String key, String defaultValue, Object... args) {
        String message = getLocaleMessage(locale, key, args);
        if (Objects.isNull(message)) {
            return DEFAULT.getMessageOrDefault(key, defaultValue, args);
        } else {
            return message;
        }
    }

    private static String getLocaleMessage(Locale locale, String key, Object... args) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("use country[{}]-language[{}]", locale.getCountry(), locale.getLanguage());
            }
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public String getMessageOrDefault(String key, String defaultValue, Object... args) {
        return getLocaleMessageOrDefault(LocaleContextHolder.getLocale(), key, defaultValue, args);
    }
}