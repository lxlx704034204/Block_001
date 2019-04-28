package io.renren.modules.sport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageResult;
import io.renren.modules.sport.entity.BmiConfig;

import java.util.Map;

/**
 * 年龄段体重标准
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 19:48:44
 */
public interface BmiConfigService extends IService<BmiConfig> {

    PageResult queryPage(Map<String, Object> params);
}

