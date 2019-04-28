package io.renren.modules.sport.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sport.entity.BmiConfig;
import io.renren.modules.sport.service.BmiConfigService;
import io.renren.common.utils.PageResult;
import io.renren.common.utils.Result;



/**
 * 年龄段体重标准
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 19:48:44
 */
@RestController
@RequestMapping("sport/bmiconfig")
public class BmiConfigController {
    @Autowired
    private BmiConfigService bmiConfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sport:bmiconfig:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageResult page = bmiConfigService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sport:bmiconfig:info")
    public Result info(@PathVariable("id") Integer id){
        BmiConfig bmiConfig = bmiConfigService.getById(id);

        return Result.ok().put("bmiConfig", bmiConfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sport:bmiconfig:save")
    public Result save(@RequestBody BmiConfig bmiConfig){
        bmiConfigService.save(bmiConfig);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sport:bmiconfig:update")
    public Result update(@RequestBody BmiConfig bmiConfig){
        ValidatorUtils.validateEntity(bmiConfig);
        bmiConfigService.updateById(bmiConfig);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sport:bmiconfig:delete")
    public Result delete(@RequestBody Integer[] ids){
        bmiConfigService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
