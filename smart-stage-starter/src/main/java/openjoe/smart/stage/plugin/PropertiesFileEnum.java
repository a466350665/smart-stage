package openjoe.smart.stage.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Function;

public enum PropertiesFileEnum {

    YML(".yml", PropertiesFileEnum::loadYamlFile),
    YAML(".yaml", PropertiesFileEnum::loadYamlFile),
    PROPERTIES(".properties", PropertiesFileEnum::loadPropertiesFile);

    private String type;
    private final Function<Resource, Properties> parser;

    PropertiesFileEnum(String type, Function<Resource, Properties> parser) {
        this.type = type;
        this.parser = parser;
    }

    public String getType() {
        return type;
    }

    public Function<Resource, Properties> getParser() {
        return parser;
    }

    private static final Logger log = LoggerFactory.getLogger(PropertiesFileEnum.class);

    private static Properties loadPropertiesFile(Resource resource) {
        Properties properties = new Properties();
        try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            properties.load(reader);
        } catch (IOException e) {
            log.error("Error loading properties file: {}", e.getMessage(), e);
        }
        return properties;
    }

    public static Properties loadYamlFile(Resource resource) {
        Properties properties = new Properties();
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        EncodedResource encodedResource = new EncodedResource(resource, StandardCharsets.UTF_8);
        factoryBean.setResources(encodedResource.getResource());
        properties.putAll(factoryBean.getObject());
        return properties;
    }
}
