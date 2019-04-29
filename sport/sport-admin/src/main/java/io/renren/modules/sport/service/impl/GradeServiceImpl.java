package io.renren.modules.sport.service.impl;

import com.google.common.collect.Lists;
import io.renren.common.utils.Result;
import io.renren.modules.sport.dto.GradeDTO;
import io.renren.modules.sport.dto.ProGradeDTO;
import io.renren.modules.sport.entity.*;
import io.renren.modules.sport.service.*;
import io.renren.modules.sport.util.AgeUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service("gradeService")
public class GradeServiceImpl implements GradeService {

    @Autowired
    private StudentGradeService studentGradeService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ScoreSuggestionService suggestionService;
    @Autowired
    private ProjectConfigService projectConfigService;
    @Autowired
    private ProjectGradeService projectGradeService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BmiGradeService bmiGradeService;
    @Autowired
    private TrainGoalService trainGoalService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveGrade(GradeDTO grade) {
        //todo
        List<ProjectConfig> projectConfigList = projectConfigService.list();
        List<ScoreSuggestion> suggestionList = suggestionService.list();
        Student stu = studentService.getById(grade.getStudentId());
        Integer age = AgeUtils.getAgeByBirthday(stu.getBirthday());
        LocalDateTime checkTime = LocalDateTime.parse(grade.getCheckTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        BmiGrade bmiGrade = BmiGrade.builder()
                .checkTime(checkTime)
                .height(grade.getHeight())
                .weight(grade.getWeight())
                .studentId(grade.getStudentId())
                .teacherName(grade.getTeacherName())
                .createTime(LocalDateTime.now())
                .age(age)
                .build();

        List<ProjectGrade> gradeList = Lists.newArrayList();

        if(!CollectionUtils.isEmpty(grade.getProList())){
           grade.getProList().forEach(g ->{
                ProjectConfig pcconf = projectConfigList.stream().filter(pc ->{
                    return (age>=pc.getMinAge() && age<=pc.getMaxAge() && g.getProGrade().compareTo(pc.getMinScore())>=0
                            && g.getProGrade().compareTo(pc.getMaxScore())<=0);
                }).findFirst().get();

                ScoreSuggestion suggestion = suggestionList.stream().filter(s -> {
                    return s.getMaxScore().compareTo(pcconf.getScoreLevel())>=0 && s.getMinScore().compareTo(pcconf.getScoreLevel())<=0;
                }).findFirst().get();

                ProjectGrade pg = ProjectGrade.builder()
                        .projectGrade(g.getProGrade())
                        .projectId(g.getProjectId())
                        .studentId(grade.getStudentId())
                        .score(pcconf.getScoreLevel())
                        .suggestion(suggestion.getSuggestion())
                        .age(age)
                        .ageRange(pcconf.getMinAge()+"-"+pcconf.getMaxAge())
                        .checkTime(checkTime)
                        .teacherName(grade.getTeacherName())
                        .createTime(LocalDateTime.now())
                        .build();
                gradeList.add(pg);
                bmiGrade.setAgeRange(pg.getAgeRange());
                bmiGrade.setSuggestion(suggestion.getSuggestion());
            });
            Map<Integer,ProjectGrade> gradeMapGroupById = gradeList.stream().collect(Collectors.toMap(ProjectGrade::getProjectId, Function.identity()));
            List<Project> projectList = projectService.list();
            Map<String, BigDecimal> gradeMap = new HashMap<>();
            projectList.forEach(p -> {
                ProjectGrade pg = gradeMapGroupById.get(p.getId());
                if(Objects.isNull(pg)){
                    return;
                }
                gradeMap.put(p.getProjectCode(),pg.getProjectGrade());
            });
            StudentGrade studentGrade = StudentGrade.builder()
                    .studentId(grade.getStudentId())
                    .createTime(LocalDateTime.now())
                    .checkTime(checkTime)
                    .height(grade.getHeight())
                    .weight(grade.getWeight())
                    .age(age)
                    .ageRange(bmiGrade.getAgeRange())
                    .teacherName(grade.getTeacherName())
                    .build();
            //todo
            try {
                BeanUtils.populate(studentGrade,gradeMap);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            bmiGradeService.save(bmiGrade);
            studentGradeService.save(studentGrade);
            projectGradeService.saveBatch(gradeList);
        }
        return Result.ok();
    }
    private static double applyGradeAsDouble(ProjectGrade projectGrade) {
        BigDecimal score = /*ProjectGrade::getScore;*/projectGrade.getScore();
        return score.doubleValue();
    }

    @Override
    public Result queryDetail(Integer studentId) {
        //基本信息
        Student stu = studentService.getById(studentId);

        Integer age = AgeUtils.getAgeByBirthday(stu.getBirthday());
        //训练目标
        List<TrainGoal> trainGoalList = trainGoalService.list();
        TrainGoal trainGoal = trainGoalList.stream().filter(tg -> tg.getMinAge() <=age && tg.getMaxAge()>=age).findFirst().get();
        //BMI测试值
        BmiGrade bmiGrade = bmiGradeService.getByStudentId(studentId);
        //最近两次身体素质测评数据
        List<StudentGrade> stuGradeList = studentGradeService.getLastTwoGrade(studentId);
        List<Integer> gradeIds = stuGradeList.stream().map(StudentGrade::getId).collect(Collectors.toList());
        List<ProjectGrade> lastTwoGradeList = projectGradeService.getInGradeIds(gradeIds);

        //计算最近两次的平均分
        Map<Integer, Double> averageMap = lastTwoGradeList.stream().collect(Collectors.groupingBy(ProjectGrade::getStuGradeId,
                Collectors.averagingDouble(GradeServiceImpl::applyGradeAsDouble)));

        //测评结果判断
        /*身体素质测试总分÷项目数≤2					差
        身体素质测试总分÷项目数=2~3					较差
        身体素质测试总分÷项目数=3~4					一般
        身体素质测试总分÷项目数=4~5					良好
        身体素质测试总分÷项目数=5					优秀
        */
        Integer averageKey = averageMap.keySet().stream().max(Comparator.naturalOrder()).get();
        String scoreDesc = "";
        if(averageMap.get(averageKey) <= 2){
            scoreDesc = "差";
        }else if(averageMap.get(averageKey) <=3){
            scoreDesc = "较差";
        }else if(averageMap.get(averageKey) <=4){
            scoreDesc = "一般";
        }else if(averageMap.get(averageKey) <=5){
            scoreDesc = "良好";
        }else if(averageMap.get(averageKey) > 5){
            scoreDesc = "优秀";
        }

        return Result.ok()
                .put("student",stu)
                .put("age",age)
                .put("trainGoal",trainGoal)
                .put("bmiGrade",bmiGrade)
                .put("stuGradeList",stuGradeList)
                .put("average",averageMap.get(averageKey))
                .put("scoreDesc",scoreDesc);
    }
}
