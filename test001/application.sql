/*
 Navicat Premium Data Transfer

 Source Server         : localhost-root
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : authorization_system

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 31/08/2025 23:51:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application`  (
  `application_id` bigint(20) NOT NULL COMMENT 'id',
  `application_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `application_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述备注',
  `application_state` bigint(20) NULL DEFAULT NULL COMMENT '状态',
  `application_type` bigint(20) NULL DEFAULT NULL COMMENT '类型',
  `application_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加密密钥',
  `application_iv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加密向量',
  `application_uid` bigint(20) NULL DEFAULT NULL COMMENT '创建用户',
  `application_cid` bigint(20) NULL DEFAULT NULL COMMENT '脚本代码',
  `application_uiid` bigint(20) NULL DEFAULT NULL COMMENT '界面代码',
  `application_sell` bigint(20) NULL DEFAULT NULL COMMENT '是否出售',
  PRIMARY KEY (`application_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for application_code
-- ----------------------------
DROP TABLE IF EXISTS `application_code`;
CREATE TABLE `application_code`  (
  `application_code_id` bigint(20) NOT NULL,
  `application_code_aid` bigint(20) NULL DEFAULT NULL,
  `application_code_time` datetime(0) NULL DEFAULT NULL,
  `application_code_file` bigint(20) NULL DEFAULT NULL COMMENT '代码文件',
  PRIMARY KEY (`application_code_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for application_recharge_card
-- ----------------------------
DROP TABLE IF EXISTS `application_recharge_card`;
CREATE TABLE `application_recharge_card`  (
  `application_recharge_card_id` bigint(20) NOT NULL COMMENT 'id',
  `application_recharge_card_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权卡',
  `application_recharge_card_val` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '额度（时间单位小时）',
  `application_recharge_card_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `application_recharge_card_use_time` datetime(0) NULL DEFAULT NULL COMMENT '使用时间',
  `application_recharge_card_use_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用者IP',
  `application_recharge_card_state` bigint(20) NULL DEFAULT NULL COMMENT '授权卡状态',
  `application_recharge_card_aid` bigint(20) NULL DEFAULT NULL COMMENT '脚本程序ID',
  `application_recharge_card_start_time` datetime(0) NULL DEFAULT NULL COMMENT '授权开始',
  `application_recharge_card_end_time` datetime(0) NULL DEFAULT NULL COMMENT '授权结束',
  PRIMARY KEY (`application_recharge_card_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for application_ui
-- ----------------------------
DROP TABLE IF EXISTS `application_ui`;
CREATE TABLE `application_ui`  (
  `application_ui_id` bigint(20) NOT NULL,
  `application_ui_aid` bigint(20) NULL DEFAULT NULL,
  `application_ui_time` datetime(0) NULL DEFAULT NULL,
  `application_ui_file` bigint(20) NULL DEFAULT NULL COMMENT '代码文件',
  PRIMARY KEY (`application_ui_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
