package io.renren.modules.sport.service;

import io.renren.common.utils.Result;
import io.renren.modules.sport.dto.GradeDTO;

public interface GradeService {
    Result saveGrade(GradeDTO grade);

    Result queryDetail(Integer studentId);
}
