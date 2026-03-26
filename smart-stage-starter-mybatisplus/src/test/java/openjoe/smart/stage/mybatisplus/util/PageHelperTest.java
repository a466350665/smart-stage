package openjoe.smart.stage.mybatisplus.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageHelperTest {

    @Test
    void shouldConvertMybatisPlusPage() {
        Page<String> source = new Page<>(3, 5, 12);
        source.setRecords(List.of("a", "b"));

        openjoe.smart.stage.core.entity.Page<String> converted = PageHelper.convert(source);

        assertThat(converted.getCurrent()).isEqualTo(3);
        assertThat(converted.getSize()).isEqualTo(5);
        assertThat(converted.getTotal()).isEqualTo(12);
        assertThat(converted.getRecords()).containsExactly("a", "b");
    }
}
