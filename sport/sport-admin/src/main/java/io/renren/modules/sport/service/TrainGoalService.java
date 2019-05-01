package io.renren.modules.sport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageResult;
import io.renren.modules.sport.entity.TrainGoal;

import java.util.List;
import java.util.Map;

/**
 * 年龄段训练目标
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
public interface TrainGoalService extends IService<TrainGoal> {

    PageResult queryPage(Map<String, Object> params);

    List<TrainGoal> queryAll();
}

