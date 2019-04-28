package io.renren.modules.sport.dao;

import io.renren.modules.sport.entity.ProjectGrade;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 项目成绩表
 * 
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
@Mapper
public interface ProjectGradeMapper extends BaseMapper<ProjectGrade> {

    List<ProjectGrade> getInGradeIds(@Param("gradeIds")Collection<Integer> gradeIds);
}
