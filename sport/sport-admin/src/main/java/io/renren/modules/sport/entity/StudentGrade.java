package io.renren.modules.sport.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 学员成绩表
 * 
 * @author ó¯òë
 * @email ${email}
 * @date 2019-04-25 15:56:20
 */
@Data
@TableName("sport_student_grade")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGrade implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 学号
	 */
	private Integer studentId;
	private Integer age;
	/**
	 * 年龄段
	 */
	private String ageRange;
	/**
	 * 测试员
	 */
	private String teacherName;
	/**
	 * 身高/cm
	 */
	private BigDecimal height;
	/**
	 * 体重/kg
	 */
	private BigDecimal weight;
	/**
	 * 速度 m/s
	 */
	private BigDecimal speed;
	/**
	 * 下肢力量
	 */
	private BigDecimal lowStrength;
	/**
	 * 上肢力量
	 */
	private BigDecimal upperStrength;
	/**
	 * 柔韧性
	 */
	private BigDecimal flexibility;
	/**
	 * 心肺能力
	 */
	private BigDecimal cardiopulmonary;
	/**
	 * 核心力量
	 */
	private BigDecimal coreStrength;
	/**
	 * TGMD-3测试
	 */
	private BigDecimal tgmd3Check;
	/**
	 * 灵敏度
	 */
	private BigDecimal sensitivity;
	/**
	 * 检测时间
	 */
	@JsonFormat( pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private LocalDateTime checkTime;
	/**
	 * 综合评分
	 */
	private BigDecimal score;
	/**
	 * 评测建议
	 */
	private String suggestion;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	@JsonFormat( pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	@JsonFormat( pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private LocalDateTime updateTime;

	private BigDecimal trainHours;
	private BigDecimal attendance;
}
