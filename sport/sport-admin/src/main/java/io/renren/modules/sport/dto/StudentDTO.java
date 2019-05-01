package io.renren.modules.sport.dto;

import io.renren.modules.sport.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO extends Student {
    private Integer age;
    private String schoolName;
}
