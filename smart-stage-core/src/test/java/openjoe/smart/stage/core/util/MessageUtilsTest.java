package openjoe.smart.stage.core.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageUtilsTest {

    @AfterEach
    void tearDown() {
        MessageUtils.setLocal(DefaultMessage.INSTANCE);
    }

    @Test
    void shouldFormatDefaultMessageArguments() {
        assertThat(MessageUtils.getOrDefault("greeting", "hello {0}", "smart-stage")).isEqualTo("hello smart-stage");
    }

    @Test
    void shouldRejectNullMessageImplementation() {
        assertThatThrownBy(() -> MessageUtils.setLocal(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("message must not be null");
    }
}
