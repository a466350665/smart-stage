package openjoe.smart.stage.plugin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PluginEnvironmentPostProcessorTest {

    private final PluginEnvironmentPostProcessor postProcessor = new PluginEnvironmentPostProcessor();

    @Test
    void shouldLoadPluginConfigsFromAllSupportedFormats() {
        ConfigurableEnvironment environment = new StandardEnvironment();

        postProcessor.postProcessEnvironment(environment, new SpringApplication(Object.class));

        assertThat(environment.getProperty("plugin.yaml.value")).isEqualTo("yaml-default");
        assertThat(environment.getProperty("plugin.yml.value")).isEqualTo("yml-default");
        assertThat(environment.getProperty("plugin.properties.value")).isEqualTo("properties-default");
    }

    @Test
    void shouldAllowHostApplicationPropertiesToOverridePluginDefaults() {
        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("applicationConfig: [test]",
                Map.of("plugin.override.target", "application-value")));

        postProcessor.postProcessEnvironment(environment, new SpringApplication(Object.class));

        assertThat(environment.getProperty("plugin.override.target")).isEqualTo("application-value");
    }

    @Test
    void shouldAllowSystemEnvironmentToOverridePluginProperties() {
        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                new MapPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                        Map.of("plugin.override.target", "system-env-value")));

        postProcessor.postProcessEnvironment(environment, new SpringApplication(Object.class));

        assertThat(environment.getProperty("plugin.override.target")).isEqualTo("system-env-value");
    }

    @Test
    void shouldLetProfileSpecificPluginConfigOverridePluginDefault() {
        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.setActiveProfiles("dev");

        postProcessor.postProcessEnvironment(environment, new SpringApplication(Object.class));

        assertThat(environment.getProperty("plugin.profile.value")).isEqualTo("profile-dev");
    }
}
