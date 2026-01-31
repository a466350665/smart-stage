package openjoe.smart.stage.core.util;

import java.text.MessageFormat;

/**
 * 默认实现
 *
 * @author Joe
 */
public class DefaultMessage implements Message {

    public static final Message INSTANCE = new DefaultMessage();

    private DefaultMessage() {
    }

    /**
     * 定义默认实现
     */
    @Override
    public String getOrDefault(String key, String defaultValue, Object... args) {
        if (args == null || args.length == 0) {
            return defaultValue;
        }
        try {
            return MessageFormat.format(defaultValue, args);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}