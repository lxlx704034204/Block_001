package io.renren.modules.sport.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageResult;
import io.renren.common.utils.Query;

import io.renren.modules.sport.dao.AreaMapper;
import io.renren.modules.sport.entity.Area;
import io.renren.modules.sport.service.AreaService;


@Service("areaService")
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Override
    public PageResult queryPage(Map<String, Object> params) {
        IPage<Area> page = this.page(
                new Query<Area>().getPage(params),
                new QueryWrapper<Area>()
        );

        return new PageResult(page);
    }

    @Override
    public List<Area> listByParentId(Integer parentId) {
        QueryWrapper wrapper = new QueryWrapper();
        Area area = Area.builder().parentId(parentId).build();
        wrapper.setEntity(area);
        return this.baseMapper.selectList(wrapper);
    }

}
