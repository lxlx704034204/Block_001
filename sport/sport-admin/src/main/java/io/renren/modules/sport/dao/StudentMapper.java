package io.renren.modules.sport.dao;

import io.renren.modules.sport.dto.StudentDTO;
import io.renren.modules.sport.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学员信息表
 * 
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    StudentDTO getStudentWithSchoolNameById(Integer studentId);
}
