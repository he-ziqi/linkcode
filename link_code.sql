/*
 Navicat Premium Data Transfer

 Source Server         : hzq-link_code
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : link_code

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 17/02/2023 22:46:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_accepted_topic
-- ----------------------------
DROP TABLE IF EXISTS `t_accepted_topic`;
CREATE TABLE `t_accepted_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` varchar(8) NOT NULL DEFAULT 'untried',
  `topic_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `topic_id` (`topic_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `t_accepted_topic_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `t_topic` (`id`),
  CONSTRAINT `t_accepted_topic_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_apply_record
-- ----------------------------
DROP TABLE IF EXISTS `t_apply_record`;
CREATE TABLE `t_apply_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请记录表主键',
  `apply_status` varchar(8) DEFAULT 'applying' COMMENT '审批状态,reject,pass,applying,timeout',
  `apply_time` datetime DEFAULT NULL COMMENT '提交时间',
  `expiration_time` datetime DEFAULT NULL COMMENT '过期时间',
  `classroom_id` bigint DEFAULT NULL COMMENT '班级id',
  `teacher_id` bigint DEFAULT NULL COMMENT '创建者教师id',
  `student_id` bigint DEFAULT NULL COMMENT '申请者id',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '申请者设置的昵称',
  PRIMARY KEY (`id`),
  KEY `classroom_id` (`classroom_id`),
  KEY `teacher_id` (`teacher_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `t_apply_record_ibfk_1` FOREIGN KEY (`classroom_id`) REFERENCES `t_classroom` (`id`),
  CONSTRAINT `t_apply_record_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `t_apply_record_ibfk_3` FOREIGN KEY (`student_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_choice_topic
-- ----------------------------
DROP TABLE IF EXISTS `t_choice_topic`;
CREATE TABLE `t_choice_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '选择题表主键',
  `content` text COMMENT '作业内容',
  `answer` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '作业答案(a,b,c,d)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `stop_time` datetime DEFAULT NULL COMMENT '截止时间',
  `classroom_id` bigint DEFAULT NULL COMMENT '班级id',
  `teacher_id` bigint DEFAULT NULL COMMENT '发布人(教师id)',
  `type` varchar(17) DEFAULT 'choice_topic_type',
  PRIMARY KEY (`id`),
  KEY `classroom_id` (`classroom_id`),
  KEY `teacher_id` (`teacher_id`),
  CONSTRAINT `t_choice_topic_ibfk_1` FOREIGN KEY (`classroom_id`) REFERENCES `t_classroom` (`id`),
  CONSTRAINT `t_choice_topic_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_classroom
-- ----------------------------
DROP TABLE IF EXISTS `t_classroom`;
CREATE TABLE `t_classroom` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班级表主键',
  `name` varchar(20) NOT NULL COMMENT '班级名称',
  `accouncement` text COMMENT '班级公告',
  `number` int DEFAULT NULL COMMENT '班级人数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `t_constraint_check1` CHECK ((`number` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_classroom_student
-- ----------------------------
DROP TABLE IF EXISTS `t_classroom_student`;
CREATE TABLE `t_classroom_student` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班级学生表主键',
  `classroom_id` bigint DEFAULT NULL COMMENT '班级id',
  `student_id` bigint DEFAULT NULL COMMENT '学生id',
  `join_time` datetime DEFAULT NULL COMMENT '加入时间',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '学生在班级中的名称',
  PRIMARY KEY (`id`),
  KEY `classroom_id` (`classroom_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `t_classroom_student_ibfk_1` FOREIGN KEY (`classroom_id`) REFERENCES `t_classroom` (`id`),
  CONSTRAINT `t_classroom_student_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_classroom_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_classroom_teacher`;
CREATE TABLE `t_classroom_teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班级教师表主键',
  `classroom_id` bigint DEFAULT NULL COMMENT '班级id',
  `teacher_id` bigint DEFAULT NULL COMMENT '教师id',
  PRIMARY KEY (`id`),
  KEY `classroom_id` (`classroom_id`),
  KEY `teacher_id` (`teacher_id`),
  CONSTRAINT `t_classroom_teacher_ibfk_1` FOREIGN KEY (`classroom_id`) REFERENCES `t_classroom` (`id`),
  CONSTRAINT `t_classroom_teacher_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_job_commit
-- ----------------------------
DROP TABLE IF EXISTS `t_job_commit`;
CREATE TABLE `t_job_commit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '选择题作业提交表主键',
  `job_id` bigint DEFAULT NULL COMMENT '作业id(选择题或编程题)',
  `type` varchar(18) DEFAULT NULL COMMENT '题目类型',
  `student_id` bigint DEFAULT NULL COMMENT '学生id',
  `content` text COMMENT '提交内容',
  `status` varchar(15) DEFAULT NULL COMMENT '提交状态',
  `commit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `classroom_id` bigint DEFAULT NULL COMMENT '班级id',
  `accepted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`),
  KEY `classroom_id` (`classroom_id`),
  CONSTRAINT `t_job_choice_commit_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `t_job_choice_commit_ibfk_2` FOREIGN KEY (`classroom_id`) REFERENCES `t_classroom` (`id`),
  CONSTRAINT `t_job_commit_chk_1` CHECK (((`type` = _utf8mb4'choice_topic_type') or (`type` = _utf8mb4'program_topic_type')))
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_programing_topic
-- ----------------------------
DROP TABLE IF EXISTS `t_programing_topic`;
CREATE TABLE `t_programing_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编程题表主键',
  `description` text COMMENT '作业描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `stop_time` datetime NOT NULL COMMENT '截止时间',
  `classroom_id` bigint DEFAULT NULL COMMENT '班级id',
  `teacher_id` bigint DEFAULT NULL COMMENT '发布人(教师id)',
  `topic_id` bigint DEFAULT NULL COMMENT '编程题题目id',
  `type` varchar(18) DEFAULT 'program_topic_type',
  PRIMARY KEY (`id`),
  KEY `classroom_id` (`classroom_id`),
  KEY `teacher_id` (`teacher_id`),
  KEY `t_programing_topic_ibfk_3` (`topic_id`),
  CONSTRAINT `t_programing_topic_ibfk_1` FOREIGN KEY (`classroom_id`) REFERENCES `t_classroom` (`id`),
  CONSTRAINT `t_programing_topic_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `t_programing_topic_ibfk_3` FOREIGN KEY (`topic_id`) REFERENCES `t_topic` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色表主键',
  `user_id` bigint DEFAULT NULL COMMENT '用户主键',
  `role_name` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色名称',
  `authority_level` tinyint DEFAULT NULL COMMENT '权限等级',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `t_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_solution
-- ----------------------------
DROP TABLE IF EXISTS `t_solution`;
CREATE TABLE `t_solution` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题解主键',
  `answer_code` text NOT NULL COMMENT '题解代码',
  `language_type` varchar(10) NOT NULL COMMENT '语言类型',
  `comment` text NOT NULL COMMENT '题解描述',
  `topic_id` bigint DEFAULT NULL COMMENT '题目id',
  `user_id` bigint DEFAULT '-1' COMMENT '创建题解的用户id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `topic_id` (`topic_id`),
  KEY `t_solution_ibfk_2` (`user_id`),
  CONSTRAINT `t_solution_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `t_topic` (`id`),
  CONSTRAINT `t_solution_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_solution_record
-- ----------------------------
DROP TABLE IF EXISTS `t_solution_record`;
CREATE TABLE `t_solution_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户题解记录主键',
  `user_id` bigint DEFAULT NULL COMMENT '用户主键',
  `topic_id` bigint DEFAULT NULL COMMENT '题目主键',
  `topic_status_type` varchar(10) DEFAULT NULL COMMENT '题解状态类型:exception,error,not_pass,pass',
  `msg` text COMMENT '执行信息',
  `code_content` text COMMENT '代码内容',
  `language_type` varchar(10) NOT NULL COMMENT '代码类型',
  `update_time` datetime NOT NULL COMMENT '执行时间',
  `accept_count` int NOT NULL DEFAULT '0' COMMENT '通过用例个数',
  `invoke_time` int NOT NULL COMMENT '执行耗时',
  PRIMARY KEY (`id`),
  KEY `topic_id` (`topic_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `t_solution_record_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `t_topic` (`id`),
  CONSTRAINT `t_solution_record_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_topic
-- ----------------------------
DROP TABLE IF EXISTS `t_topic`;
CREATE TABLE `t_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目id',
  `topic_name` varchar(20) DEFAULT NULL COMMENT '题目名称',
  `topic_comment` text NOT NULL COMMENT '题目描述',
  `difficulty_level` tinyint NOT NULL COMMENT '题目难度级别',
  `timeout` int DEFAULT NULL COMMENT '时间限制 单位:ms',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'untried' COMMENT '题目状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `topic_name` (`topic_name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_topic_input
-- ----------------------------
DROP TABLE IF EXISTS `t_topic_input`;
CREATE TABLE `t_topic_input` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '输入集主键',
  `input_content` text NOT NULL COMMENT '输入内容',
  `topic_id` bigint DEFAULT NULL COMMENT '题目id',
  PRIMARY KEY (`id`),
  KEY `topic_id` (`topic_id`),
  CONSTRAINT `t_topic_input_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `t_topic` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_topic_output
-- ----------------------------
DROP TABLE IF EXISTS `t_topic_output`;
CREATE TABLE `t_topic_output` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '输出集主键',
  `output_content` text NOT NULL COMMENT '输出内容',
  `topic_input_id` bigint DEFAULT NULL COMMENT '输入集id',
  PRIMARY KEY (`id`),
  KEY `topic_input_id` (`topic_input_id`),
  CONSTRAINT `t_topic_output_ibfk_1` FOREIGN KEY (`topic_input_id`) REFERENCES `t_topic_input` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户表主键',
  `nick_name` varchar(15) DEFAULT NULL COMMENT '用户昵称',
  `avatar_addr` varchar(255) NOT NULL DEFAULT 'file-center/default.jpg' COMMENT '头像地址',
  `password` varbinary(100) NOT NULL COMMENT '密码',
  `email` varchar(30) DEFAULT NULL COMMENT '邮件地址',
  `phone_number` char(11) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nick_name` (`nick_name`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
