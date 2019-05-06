package io.renren.modules.sport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageResult;
import io.renren.modules.sport.entity.Area;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-05-06 22:34:15
 */
public interface AreaService extends IService<Area> {

    PageResult queryPage(Map<String, Object> params);

    List<Area> listByParentId(Integer parentId);
}

