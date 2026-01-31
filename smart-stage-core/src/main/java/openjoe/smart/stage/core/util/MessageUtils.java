package openjoe.smart.stage.core.util;

/**
 * 消息处理抽象类，支持动态传参，支持I18n
 * 注：国际化能力通过继承它实现，具体查看I18nMessage
 *
 * @author Joe
 */
public class MessageUtils {

    /**
     * 当前实例，可通过子类赋值覆盖
     */
    protected static Message local = DefaultMessage.INSTANCE;

    public static void setLocal(Message message) {
        local = message;
    }

    public static String get(String key, Object... args) {
        return getOrDefault(key, key, args);
    }

    public static String getOrDefault(String key, String defaultValue, Object... args) {
        return local.getOrDefault(key, defaultValue, args);
    }
}