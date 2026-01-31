package openjoe.smart.stage.core.util;

/**
 * 消息处理抽象类，支持动态传参，支持I18n
 * 注：国际化能力通过继承它实现，具体查看I18nMessage
 *
 * @author Joe
 */
public interface Message {

    String getOrDefault(String key, String defaultValue, Object... args);
}