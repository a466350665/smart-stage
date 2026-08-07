package openjoe.smart.stage.mybatisplus.service.impl;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import openjoe.smart.stage.mybatisplus.service.BaseService;

/**
 * MybatisPlus基础ServiceImpl
 *
 * @param <T>
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {
}
