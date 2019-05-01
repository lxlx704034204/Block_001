package io.renren.modules.sport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageResult;
import io.renren.modules.sport.dto.StudentDTO;
import io.renren.modules.sport.entity.Student;

import java.util.Map;

/**
 * 学员信息表
 *
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
public interface StudentService extends IService<Student> {

    PageResult queryPage(Map<String, Object> params);

    StudentDTO getStudentWithSchoolNameById(Integer studentId);
}

