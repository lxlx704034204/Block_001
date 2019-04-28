package io.renren.modules.sport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageResult;
import io.renren.modules.sport.entity.Project;

import java.util.List;
import java.util.Map;

/**
 * 项目
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
public interface ProjectService extends IService<Project> {

    PageResult queryPage(Map<String, Object> params);

    List<Project> getProjectByBirthday(String birthday);
}

