package io.renren.modules.sport.controller;

import io.renren.common.utils.PageResult;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sport.dto.GradeDTO;
import io.renren.modules.sport.entity.StudentGrade;
import io.renren.modules.sport.service.GradeService;
import io.renren.modules.sport.service.ProjectGradeService;
import io.renren.modules.sport.service.StudentGradeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 学员成绩表
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
@RestController
@RequestMapping("sport/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;


    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sport:studentgrade:save")
    public Result save(@RequestBody GradeDTO grade){
        return gradeService.saveGrade(grade);
    }

    /**
     * 保存
     */
    @RequestMapping("/info")
    @RequiresPermissions("sport:studentgrade:info")
    public Result detail(Integer studentId){
        return gradeService.queryDetail(studentId);
    }
}
