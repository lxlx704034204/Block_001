package io.renren.modules.sport.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sport.entity.BmiGrade;
import io.renren.modules.sport.service.BmiGradeService;
import io.renren.common.utils.PageResult;
import io.renren.common.utils.Result;



/**
 * BMI成绩表
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
@RestController
@RequestMapping("sport/bmigrade")
public class BmiGradeController {
    @Autowired
    private BmiGradeService bmiGradeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sport:bmigrade:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageResult page = bmiGradeService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sport:bmigrade:info")
    public Result info(@PathVariable("id") Integer id){
        BmiGrade bmiGrade = bmiGradeService.getById(id);

        return Result.ok().put("bmiGrade", bmiGrade);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sport:bmigrade:save")
    public Result save(@RequestBody BmiGrade bmiGrade){
        bmiGrade.setCreateTime(LocalDateTime.now());
        bmiGradeService.save(bmiGrade);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sport:bmigrade:update")
    public Result update(@RequestBody BmiGrade bmiGrade){
        ValidatorUtils.validateEntity(bmiGrade);
        bmiGradeService.updateById(bmiGrade);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sport:bmigrade:delete")
    public Result delete(@RequestBody Integer[] ids){
        bmiGradeService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
