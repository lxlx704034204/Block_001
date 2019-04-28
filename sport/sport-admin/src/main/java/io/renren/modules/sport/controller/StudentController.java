package io.renren.modules.sport.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sport.entity.ProjectConfig;
import io.renren.modules.sport.service.ProjectConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sport.entity.Student;
import io.renren.modules.sport.service.StudentService;
import io.renren.common.utils.PageResult;
import io.renren.common.utils.Result;



/**
 * 学员信息表
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
@RestController
@RequestMapping("sport/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sport:student:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageResult page = studentService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sport:student:info")
    public Result info(@PathVariable("id") Integer id){
        Student student = studentService.getById(id);

        return Result.ok().put("student", student);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sport:student:save")
    public Result save(@RequestBody Student student){
        studentService.save(student);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sport:student:update")
    public Result update(@RequestBody Student student){
        ValidatorUtils.validateEntity(student);
        studentService.updateById(student);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sport:student:delete")
    public Result delete(@RequestBody Integer[] ids){
        studentService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
