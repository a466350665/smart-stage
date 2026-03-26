package openjoe.smart.stage.core.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PageTest {

    @Test
    void shouldConvertRecordsAndKeepPagingMetadata() {
        Page<Integer> page = Page.of(2, 3, Arrays.asList(1, 2, 3)).setTotal(5);

        Page<String> converted = page.convert(String::valueOf);

        assertThat(converted.getCurrent()).isEqualTo(2);
        assertThat(converted.getSize()).isEqualTo(3);
        assertThat(converted.getTotal()).isEqualTo(5);
        assertThat(converted.getPages()).isEqualTo(2);
        assertThat(converted.getRecords()).containsExactly("1", "2", "3");
    }

    @Test
    void shouldHandleNullRecordsSafely() {
        Page<String> page = new Page<>(1, 10, (java.util.List<String>) null);
        page.setRecords(null);

        Page<Integer> converted = page.convert(String::length);

        assertThat(page.getTotal()).isZero();
        assertThat(converted.getRecords()).isEqualTo(Collections.emptyList());
        assertThat(Page.of(1, 0, 10).getPages()).isZero();
    }
}
