package io.renren.modules.sport.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageResult;
import io.renren.common.utils.Query;

import io.renren.modules.sport.dao.BmiConfigMapper;
import io.renren.modules.sport.entity.BmiConfig;
import io.renren.modules.sport.service.BmiConfigService;


@Service("bmiConfigService")
public class BmiConfigServiceImpl extends ServiceImpl<BmiConfigMapper, BmiConfig> implements BmiConfigService {

    @Override
    public PageResult queryPage(Map<String, Object> params) {
        IPage<BmiConfig> page = this.page(
                new Query<BmiConfig>().getPage(params),
                new QueryWrapper<BmiConfig>()
        );

        return new PageResult(page);
    }

}
