package openjoe.smart.stage.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class MybatisPlusAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MybatisPlusAutoConfiguration.class));

    @Test
    void shouldRegisterDefaultBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MybatisPlusInterceptor.class);
            assertThat(context).hasSingleBean(MetaObjectHandler.class);
        });
    }

    @Test
    void shouldApplyConfiguredDbType() {
        contextRunner
                .withPropertyValues("smart.stage.mybatis-plus.page-db-type=mysql")
                .run(context -> {
                    MybatisPlusInterceptor interceptor = context.getBean(MybatisPlusInterceptor.class);
                    assertThat(interceptor.getInterceptors()).hasSize(1);

                    InnerInterceptor innerInterceptor = interceptor.getInterceptors().get(0);
                    assertThat(innerInterceptor).isInstanceOf(PaginationInnerInterceptor.class);
                    assertThat(((PaginationInnerInterceptor) innerInterceptor).getDbType()).isEqualTo(DbType.MYSQL);
                });
    }
}
