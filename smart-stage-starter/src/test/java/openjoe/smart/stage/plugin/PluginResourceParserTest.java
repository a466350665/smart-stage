package openjoe.smart.stage.plugin;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

class PluginResourceParserTest {

    @Test
    void shouldExtractMessageBaseNameFromPluginMessageResource() {
        String baseName = PluginResourceParser.extractMessageBaseName(
                new ClassPathResource("plugin/demo/messages.properties"));

        assertThat(baseName).isEqualTo("plugin/demo/messages");
    }

    @Test
    void shouldExtractMessageBaseNameFromLocalizedPluginMessageResource() {
        String baseName = PluginResourceParser.extractMessageBaseName(
                new ClassPathResource("plugin/demo/messages_en.properties"));

        assertThat(baseName).isEqualTo("plugin/demo/messages");
    }

    @Test
    void shouldCollectPluginMessageBaseNamesWithoutLocaleSuffix() {
        assertThat(PluginResourceParser.parseMessages("messages"))
                .contains("plugin/demo/messages")
                .doesNotContain("plugin/demo/messages_en");
    }
}
