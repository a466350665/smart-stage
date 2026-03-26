package openjoe.smart.stage.mybatisplus.handler;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class MybatisPlusMetaObjectHandlerTest {

    private final MybatisPlusMetaObjectHandler handler = new MybatisPlusMetaObjectHandler();

    @Test
    void shouldFillCreateAndUpdateTimeOnInsert() {
        AuditEntity entity = new AuditEntity();
        MetaObject metaObject = SystemMetaObject.forObject(entity);

        handler.insertFill(metaObject);

        assertThat(entity.getCreateTime()).isNotNull();
        assertThat(entity.getUpdateTime()).isNotNull();
    }

    @Test
    void shouldRefreshUpdateTimeOnUpdate() throws InterruptedException {
        AuditEntity entity = new AuditEntity();
        entity.setUpdateTime(new Date(1L));
        MetaObject metaObject = SystemMetaObject.forObject(entity);

        handler.updateFill(metaObject);

        assertThat(entity.getUpdateTime()).isAfter(new Date(1L));
    }

    static class AuditEntity {
        private Date createTime;
        private Date updateTime;

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }
    }
}
