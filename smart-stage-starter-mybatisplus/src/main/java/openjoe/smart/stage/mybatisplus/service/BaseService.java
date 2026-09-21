package openjoe.smart.stage.mybatisplus.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import openjoe.smart.stage.core.entity.Page;
import openjoe.smart.stage.mybatisplus.util.PageHelper;

/**
 * MybatisPlus基础ServiceImpl
 *
 * @param <T>
 */
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 提供新的分页方法，统一分页返回Entity
     *
     * @param current
     * @param size
     * @return
     */
    public Page<T> findPage(long current, long size) {
        return findPage(current, size, Wrappers.emptyWrapper());
    }

    /**
     * 提供新的分页方法，统一分页返回Entity
     *
     * @param current
     * @param size
     * @param wrapper
     * @return
     */
    public Page<T> findPage(long current, long size, Wrapper<T> wrapper) {
        IPage<T> t = page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size), wrapper);
        return PageHelper.convert(t);
    }
}
