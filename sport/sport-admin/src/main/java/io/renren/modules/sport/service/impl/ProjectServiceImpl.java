package io.renren.modules.sport.service.impl;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageResult;
import io.renren.modules.sport.dao.ProjectMapper;
import io.renren.modules.sport.entity.Project;
import io.renren.modules.sport.util.AgeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.Query;

import io.renren.modules.sport.service.ProjectService;

import javax.annotation.Resource;


@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public PageResult queryPage(Map<String, Object> params) {
        IPage<Project> page = this.page(
                new Query<Project>().getPage(params),
                new QueryWrapper<Project>()
        );

        return new PageResult(page);
    }

    @Override
    public List<Project> getProjectByBirthday(String birthday) {
        Integer age = AgeUtils.getAgeByBirthday(birthday);
        List<Project> list = this.list();
        return list.stream().filter(p -> p.getMinAge() <= age && p.getMaxAge() >=age)
                .collect(Collectors.toList());
    }

}
