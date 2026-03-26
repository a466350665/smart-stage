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
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

public class PluginResourceParser {

    static final String PLUGIN_PROPERTY_SOURCE_PREFIX = "smart-stage-plugin:";

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
        URL url;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            log.error("", e);
            return null;
        }

        String normalizedPath = url.toString().replace('\\', '/');
        int pluginIndex = normalizedPath.indexOf("plugin/");
        if (pluginIndex < 0) {
            return null;
        }

        String relative = normalizedPath.substring(pluginIndex);
        if (!relative.endsWith(".properties")) {
            return null;
        }

        relative = relative.substring(0, relative.length() - ".properties".length());
        int slashIndex = relative.lastIndexOf('/');
        int localeIndex = relative.indexOf('_', slashIndex + 1);
        if (localeIndex > 0) {
            return relative.substring(0, localeIndex);
        }

        if (!StringUtils.hasLength(relative)) {
            return null;
        }

        return relative;
    }

    public static void parseConfigs(ConfigurableEnvironment environment, ResourcePatternResolver resourcePatternResolver, String profile,
                                    boolean profileSpecific) {
        for (PropertiesFileEnum file : PropertiesFileEnum.values()) {
            String location = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "plugin/*/application" + profile + file.getType();
            parseConfigs(environment, resourcePatternResolver, location, file.getParser(), profileSpecific);
        }
    }

    private static void parseConfigs(ConfigurableEnvironment environment, ResourcePatternResolver resourcePatternResolver, String location,
                                     Function<Resource, Properties> parser, boolean profileSpecific) {
        try {
            Resource[] resources = resourcePatternResolver.getResources(location);
            for (Resource resource : resources) {
                if (resource.exists() && resource.isReadable()) {
                    Properties properties = parser.apply(resource);
                    if (!properties.isEmpty()) {
                        addPluginPropertySource(environment.getPropertySources(), resource.getURL().toString(), properties, profileSpecific);
                    }
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    private static void addPluginPropertySource(MutablePropertySources propertySources, String resourceName, Properties properties,
                                                boolean profileSpecific) {
        String propertySourceName = PLUGIN_PROPERTY_SOURCE_PREFIX + resourceName;
        PropertiesPropertySource propertySource = new PropertiesPropertySource(propertySourceName, properties);
        if (!profileSpecific) {
            propertySources.addLast(propertySource);
            return;
        }

        String anchor = findFirstPluginPropertySource(propertySources);
        if (StringUtils.hasLength(anchor)) {
            propertySources.addBefore(anchor, propertySource);
            return;
        }

        if (propertySources.contains(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)) {
            propertySources.addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, propertySource);
            return;
        }

        propertySources.addLast(propertySource);
    }

    private static String findFirstPluginPropertySource(MutablePropertySources propertySources) {
        for (org.springframework.core.env.PropertySource<?> propertySource : propertySources) {
            if (propertySource.getName().startsWith(PLUGIN_PROPERTY_SOURCE_PREFIX)) {
                return propertySource.getName();
            }
        }
        return null;
    }
}
