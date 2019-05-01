package io.renren.modules.sport.dto;

import io.renren.modules.sport.entity.ProjectGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectGradeDTO extends ProjectGrade {
    private String projectCode;
    private String projectName;
}
