package openjoe.smart.stage.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

public class PluginResourceParser {

    private static final Logger log = LoggerFactory.getLogger(PluginResourceParser.class);

    public static Set<String> parseMessages(String baseName) {
        String location = "plugin/*/" + baseName + "*.properties";
        Resource[] resources;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + location);
        } catch (IOException e) {
            log.error("", e);
            return Collections.emptySet();
        }

        if(resources == null || resources.length==0){
            return Collections.emptySet();
        }

        Set<String> messageNames = new HashSet<>();
        for (Resource resource : resources) {
            String tempName = extractMessageBaseName(resource);
            if(StringUtils.hasLength(tempName)){
                messageNames.add(tempName);
            }
        }
        return messageNames;
    }

    public static String extractMessageBaseName(Resource resource) {
        String url;
        try {
            url = resource.getURL().toString();
        } catch (IOException e) {
            log.error("", e);
            return null;
        }

        // 统一定位 plugin/ 之后的逻辑路径
        int pluginIndex = url.indexOf("/plugin/");
        if (pluginIndex < 0) {
            return null;
        }

        String relative = url.substring(pluginIndex + 1); // 去掉前面的 /
        int localeIndex = relative.indexOf('_');
        int extIndex = relative.lastIndexOf(".properties");

        if (extIndex < 0) {
            return null;
        }

        String base;
        if (localeIndex > 0) {
            base = relative.substring(0, localeIndex);
        } else {
            base = relative.substring(0, extIndex);
        }

        return base;
    }

    public static void parseConfigs(ConfigurableEnvironment environment, ResourcePatternResolver resourcePatternResolver, String profile) {
        for (PropertiesFileEnum file : PropertiesFileEnum.values()) {
            String location = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "plugin/*/application" + profile + file.getType();
            parseConfigs(environment, resourcePatternResolver, location, file.getParser());
        }
    }

    private static void parseConfigs(ConfigurableEnvironment environment, ResourcePatternResolver resourcePatternResolver, String location,
                                Function<Resource, Properties> parser) {
        try {
            Resource[] resources = resourcePatternResolver.getResources(location);
            for (Resource resource : resources) {
                if (resource.exists() && resource.isReadable()) {
                    Properties properties = parser.apply(resource);
                    if (!properties.isEmpty()) {
                        MutablePropertySources propertySources = environment.getPropertySources();
                        String pathName = resource.getURL().getPath();
                        // 优先级低于系统环境
                        propertySources.addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, new PropertiesPropertySource(pathName
                                , properties));
                    }
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
