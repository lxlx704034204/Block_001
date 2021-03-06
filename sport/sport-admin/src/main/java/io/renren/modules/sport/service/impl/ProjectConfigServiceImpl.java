package io.renren.modules.sport.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageResult;
import io.renren.common.utils.Query;

import io.renren.modules.sport.dao.ProjectConfigMapper;
import io.renren.modules.sport.entity.ProjectConfig;
import io.renren.modules.sport.service.ProjectConfigService;


@Service("projectConfigService")
public class ProjectConfigServiceImpl extends ServiceImpl<ProjectConfigMapper, ProjectConfig> implements ProjectConfigService {

    @Override
    public PageResult queryPage(Map<String, Object> params) {
        IPage<ProjectConfig> page = this.page(
                new Query<ProjectConfig>().getPage(params),
                new QueryWrapper<ProjectConfig>()
        );

        return new PageResult(page);
    }

    @Override
    public List<ProjectConfig> getByIds(List<Integer> projectIds) {
        return this.baseMapper.selectBatchIds(projectIds);
    }

    @Override
    public List<ProjectConfig> getByProjectIds(List<Integer> projectIds) {
        return this.baseMapper.getByProjectIds(projectIds);
    }

}
