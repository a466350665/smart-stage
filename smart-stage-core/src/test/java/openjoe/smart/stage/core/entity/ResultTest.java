package openjoe.smart.stage.core.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {

    @Test
    void shouldCreateSuccessResultWithPayload() {
        Result<String> result = Result.success("ok");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getCode()).isEqualTo("000000");
        assertThat(result.getMessage()).isEqualTo("成功");
        assertThat(result.getData()).isEqualTo("ok");
    }

    @Test
    void shouldCreateErrorResult() {
        Result<Void> result = Result.error("E001", "failed");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getCode()).isEqualTo("E001");
        assertThat(result.getMessage()).isEqualTo("failed");
    }
}
