package openjoe.smart.stage.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import openjoe.smart.stage.mybatisplus.entity.BaseEntity;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入数据填充公共数据
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object obj = metaObject.getOriginalObject();
        if (obj instanceof BaseEntity entity) {
            Date now = getNow();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
        }
    }

    /**
     * 更新数据填充公共数据
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object obj = metaObject.getOriginalObject();
        if (obj instanceof BaseEntity entity) {
            entity.setUpdateTime(getNow());
        }
    }

    private Date getNow(){
        return new Date();
    }
}