package openjoe.smart.stage.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 扩展加载插件配置文件
 */
public class PluginEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(PluginEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        // 解析默认配置文件，如：application.yml
        PluginResourceParser.parseConfigs(environment, resourcePatternResolver, "");

        // 解析activeProfiles配置文件，如：application-dev.yml
        for (String profile : environment.getActiveProfiles()) {
            PluginResourceParser.parseConfigs(environment, resourcePatternResolver, "-" + profile);
        }
    }
}


