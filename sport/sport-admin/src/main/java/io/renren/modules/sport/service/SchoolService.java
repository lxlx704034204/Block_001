package io.renren.modules.sport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageResult;
import io.renren.modules.sport.entity.School;

import java.util.Map;

/**
 * 
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-05-02 01:23:09
 */
public interface SchoolService extends IService<School> {

    PageResult queryPage(Map<String, Object> params);
}

