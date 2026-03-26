package openjoe.smart.stage.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

/**
 * Spring上下文工具
 *
 * @author Joe
 */
public final class ApplicationContextUtils implements ApplicationContextInitializer {

    private static ApplicationContext applicationContext;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (Exception exception) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception exception) {
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return applicationContext.getBean(name, clazz);
        } catch (Exception exception) {
            return null;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        try {
            return applicationContext.getBeansOfType(clazz);
        } catch (Exception exception) {
            return Collections.emptyMap();
        }
    }

    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        try {
            return applicationContext.findAnnotationOnBean(beanName, annotationType);
        } catch (Exception exception) {
            return null;
        }
    }
}