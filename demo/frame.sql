/*
 Navicat Premium Data Transfer

 Source Server         : localhost-root
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : frame

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 21/09/2025 14:07:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `sys_file_id` bigint(20) NOT NULL COMMENT 'ID',
  `sys_file_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相对路径',
  `sys_file_md5` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据摘要',
  `sys_file_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `sys_file_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  PRIMARY KEY (`sys_file_id`) USING BTREE,
  INDEX `suoyin_sys_file_path`(`sys_file_path`) USING BTREE,
  INDEX `suoyin_sys_file_md5`(`sys_file_md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_group`;
CREATE TABLE `sys_group`  (
  `sys_group_id` bigint(20) NOT NULL COMMENT '权限组ID',
  `sys_group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限组名称',
  `sys_group_type` bigint(255) NULL DEFAULT NULL COMMENT '权限组类型',
  `sys_group_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限组描述',
  `sys_group_sup` bigint(255) NULL DEFAULT NULL COMMENT '权限组上级',
  PRIMARY KEY (`sys_group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_group
-- ----------------------------
INSERT INTO `sys_group` VALUES (1, '管理员权限组', 1, NULL, 0);

-- ----------------------------
-- Table structure for sys_group_column
-- ----------------------------
DROP TABLE IF EXISTS `sys_group_column`;
CREATE TABLE `sys_group_column`  (
  `sys_group_column_id` bigint(20) NOT NULL COMMENT '组列级别控制表',
  `sys_group_column_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询key',
  `sys_group_column_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名',
  `sys_group_column_table` bigint(20) NULL DEFAULT NULL COMMENT '是否参与表格显示',
  `sys_group_column_edit` bigint(20) NULL DEFAULT NULL COMMENT '是否参与编辑',
  `sys_group_column_update` bigint(20) NULL DEFAULT NULL COMMENT '是否参与修改',
  `sys_group_column_save` bigint(20) NULL DEFAULT NULL COMMENT '是否参与添加',
  `sys_group_column_query` bigint(20) NULL DEFAULT NULL COMMENT '是否参与查询',
  `sys_group_column_save_def` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '添加默认值',
  `sys_group_column_update_def` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改默认值',
  `sys_group_column_query_symbol` bigint(20) NULL DEFAULT NULL COMMENT '查询运算符',
  `sys_group_column_query_montage` bigint(20) NULL DEFAULT NULL COMMENT '查询连接符',
  `sys_group_column_query_def` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询默认值',
  `sys_group_column_gid` bigint(20) NULL DEFAULT NULL COMMENT '组id',
  PRIMARY KEY (`sys_group_column_id`) USING BTREE,
  INDEX `suoyin_sys_group_column_key`(`sys_group_column_key`) USING BTREE,
  INDEX `suoyin_sys_group_column_name`(`sys_group_column_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_group_column
-- ----------------------------
INSERT INTO `sys_group_column` VALUES (1758075113782000000, 'sysFile', 'sys_file_id', 0, 0, 0, 0, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113790000000, 'sysFile', 'sys_file_path', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113794000000, 'sysFile', 'sys_file_md5', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113798000000, 'sysFile', 'sys_file_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113802000000, 'sysFile', 'sys_file_type', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113809000000, 'sysGroup', 'sys_group_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113813000000, 'sysGroup', 'sys_group_name', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113818000000, 'sysGroup', 'sys_group_type', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113822000000, 'sysGroup', 'sys_group_desc', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113827000000, 'sysGroup', 'sys_group_sup', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113833000000, 'sysGroupColumn', 'sys_group_column_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113837000000, 'sysGroupColumn', 'sys_group_column_key', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113842000000, 'sysGroupColumn', 'sys_group_column_name', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113846000000, 'sysGroupColumn', 'sys_group_column_table', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113850000000, 'sysGroupColumn', 'sys_group_column_edit', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113854000000, 'sysGroupColumn', 'sys_group_column_update', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113858000000, 'sysGroupColumn', 'sys_group_column_save', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113862000000, 'sysGroupColumn', 'sys_group_column_query', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113865000000, 'sysGroupColumn', 'sys_group_column_save_def', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113870000000, 'sysGroupColumn', 'sys_group_column_update_def', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113874000000, 'sysGroupColumn', 'sys_group_column_query_symbol', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113879000000, 'sysGroupColumn', 'sys_group_column_query_montage', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113882000000, 'sysGroupColumn', 'sys_group_column_query_def', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113887000000, 'sysGroupColumn', 'sys_group_column_gid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113894000000, 'sysGroupPermission', 'sys_group_permission_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113897000000, 'sysGroupPermission', 'sys_group_permission_pid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113902000000, 'sysGroupPermission', 'sys_group_permission_gid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113906000000, 'sysGroupPermission', 'sys_group_permission_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113913000000, 'sysGroupTable', 'sys_group_table_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113917000000, 'sysGroupTable', 'sys_group_table_key', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113921000000, 'sysGroupTable', 'sys_group_table_gid', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113926000000, 'sysGroupTable', 'sys_group_table_but_save_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113930000000, 'sysGroupTable', 'sys_group_table_but_delete_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113933000000, 'sysGroupTable', 'sys_group_table_table_update_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113937000000, 'sysGroupTable', 'sys_group_table_table_delete_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113942000000, 'sysGroupTable', 'sys_group_table_but_query_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113945000000, 'sysGroupTable', 'sys_group_table_table_but_width', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113951000000, 'sysLog', 'sys_log_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113955000000, 'sysLog', 'sys_log_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113959000000, 'sysLog', 'sys_log_ms', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113963000000, 'sysLog', 'sys_log_sql', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113969000000, 'sysMapping', 'sys_mapping_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113974000000, 'sysMapping', 'sys_mapping_key', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113979000000, 'sysMapping', 'sys_mapping_title', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113983000000, 'sysMapping', 'sys_mapping_val', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113987000000, 'sysMapping', 'sys_mapping_table_width', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113992000000, 'sysMapping', 'sys_mapping_select', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075113996000000, 'sysMapping', 'sys_mapping_image', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114001000000, 'sysMapping', 'sys_mapping_file', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114005000000, 'sysMapping', 'sys_mapping_date', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114010000000, 'sysMapping', 'sys_mapping_script', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114014000000, 'sysMapping', 'sys_mapping_sort', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114021000000, 'sysOption', 'sys_option_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114025000000, 'sysOption', 'sys_option_key', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114028000000, 'sysOption', 'sys_option_title', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114032000000, 'sysOption', 'sys_option_value', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114036000000, 'sysOption', 'sys_option_state', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114044000000, 'sysPermission', 'sys_permission_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114048000000, 'sysPermission', 'sys_permission_name', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114053000000, 'sysPermission', 'sys_permission_data', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114058000000, 'sysPermission', 'sys_permission_type', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114062000000, 'sysPermission', 'sys_permission_desc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114066000000, 'sysPermission', 'sys_permission_sup', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114069000000, 'sysPermission', 'sys_permission_sort', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114076000000, 'sysUsers', 'sys_users_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114079000000, 'sysUsers', 'sys_users_acc', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114083000000, 'sysUsers', 'sys_users_pwd', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114087000000, 'sysUsers', 'sys_users_phone', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114091000000, 'sysUsers', 'sys_users_open_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114095000000, 'sysUsers', 'sys_users_status', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114099000000, 'sysUsers', 'sys_users_type', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114103000000, 'sysUsers', 'sys_users_reg_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114107000000, 'sysUsers', 'sys_users_start_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114111000000, 'sysUsers', 'sys_users_end_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114114000000, 'sysUsers', 'sys_users_price', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114117000000, 'sysUsers', 'sys_users_desc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114121000000, 'sysUsers', 'sys_users_sup', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114126000000, 'sysUsers', 'sys_users_mail', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114129000000, 'sysUsers', 'sys_users_group', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114135000000, 'sysUsersLoginLog', 'sys_users_login_log_id', 1, 0, 0, 0, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114138000000, 'sysUsersLoginLog', 'sys_users_login_log_ip', 1, 0, 0, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114143000000, 'sysUsersLoginLog', 'sys_users_login_log_desc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114147000000, 'sysUsersLoginLog', 'sys_users_login_log_time', 1, 0, 0, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114150000000, 'sysUsersLoginLog', 'sys_users_login_log_uid', 1, 0, 0, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114154000000, 'sysUsersLoginLog', 'sys_users_login_log_token', 1, 0, 0, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1758075114158000000, 'sysUsersLoginLog', 'sys_users_login_log_mac', 1, 0, 0, 1, 0, NULL, NULL, 1, 1, NULL, 1);

-- ----------------------------
-- Table structure for sys_group_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_group_permission`;
CREATE TABLE `sys_group_permission`  (
  `sys_group_permission_id` bigint(20) NOT NULL,
  `sys_group_permission_pid` bigint(20) NULL DEFAULT NULL,
  `sys_group_permission_gid` bigint(20) NULL DEFAULT NULL,
  `sys_group_permission_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`sys_group_permission_id`) USING BTREE,
  INDEX `suoyin_sys_group_permission_pid`(`sys_group_permission_pid`) USING BTREE,
  INDEX `suoyin_sys_group_permission_gid`(`sys_group_permission_gid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_group_permission
-- ----------------------------
INSERT INTO `sys_group_permission` VALUES (1758075114161000000, 1, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114165000000, 2, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114166000000, 3, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114168000000, 4, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114170000000, 5, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114171000000, 6, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114174000000, 7, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114175000000, 8, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114177000000, 1748214311232000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114179000000, 1748214311240000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114180000000, 1748214311244000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114182000000, 1748214311247000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114184000000, 1748214311251000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114186000000, 1748214311254000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114187000000, 1748214311257000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114189000000, 1748214311260000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114192000000, 1748214311263000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114193000000, 1748214311265000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114195000000, 1748214311268000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114196000000, 1748214311271000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114197000000, 1748214313833000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114199000000, 1748214313837000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114201000000, 1748214313839000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114203000000, 1748214313841000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114204000000, 1748214313843000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114207000000, 1748214313845000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114209000000, 1748214313846000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114210000000, 1748214313847000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114212000000, 1748214313849000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114213000000, 1748214313850000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114215000000, 1748214313852000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114217000000, 1748214313854000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114219000000, 1748214313857000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114220000000, 1748214313859000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114222000000, 1748214313860000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114224000000, 1748214313862000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114226000000, 1748214313864000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114228000000, 1748214313865000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114230000000, 1748214313867000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114231000000, 1748214313869000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114233000000, 1748214313872000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114235000000, 1748214313875000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114237000000, 1748214313877000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114239000000, 1748214313879000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114240000000, 1748214313881000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114241000000, 1748214313883000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114243000000, 1748214313885000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114245000000, 1748214313886000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114246000000, 1748214313888000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114248000000, 1748214313890000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114249000000, 1748214313892000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114251000000, 1748214313894000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114253000000, 1748214313896000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114254000000, 1748214313898000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114256000000, 1748214313899000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114258000000, 1748214313901000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114260000000, 1748214313902000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114261000000, 1748214313904000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114263000000, 1748214313906000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114265000000, 1748214313908000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114267000000, 1748214313911000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114269000000, 1748214313912000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114270000000, 1748214313914000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114272000000, 1748214313916000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114275000000, 1748214313917000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114277000000, 1748214313919000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114278000000, 1748214313922000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114280000000, 1748214313927000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114282000000, 1748214313929000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114283000000, 1748214313930000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114285000000, 1748214313932000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114286000000, 1748214313933000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114289000000, 1748214313935000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114291000000, 1748214313937000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114293000000, 1748214313939000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114295000000, 1748214313941000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114297000000, 1748214313943000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114298000000, 1748214313946000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114300000000, 1748214313948000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114302000000, 1748214313949000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114303000000, 1748214313951000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114305000000, 1748214313953000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114307000000, 1748214313955000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114309000000, 1748214313958000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114311000000, 1748214313960000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114313000000, 1748214313962000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114314000000, 1748214313964000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114316000000, 1748214313965000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114317000000, 1748214313967000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114319000000, 1748214313968000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114320000000, 1748214313970000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114322000000, 1748214313971000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114324000000, 1748214313973000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114325000000, 1748214313976000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114327000000, 1748214313978000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114328000000, 1748214313980000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114330000000, 1748214313982000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114331000000, 1748214313983000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114333000000, 1748214313985000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114334000000, 1748214313986000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114336000000, 1748214313989000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114337000000, 1748214313991000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114339000000, 1748214313993000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114342000000, 1748214313995000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114343000000, 1748214313996000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114345000000, 1748214313998000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114347000000, 1748214314000000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114348000000, 1748214314001000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114350000000, 1748214314003000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758075114351000000, 1748214314005000001, 1, '2025-09-17 10:11:54');
INSERT INTO `sys_group_permission` VALUES (1758085927388000000, 1758076467581004200, 1, '2025-09-17 13:12:07');
INSERT INTO `sys_group_permission` VALUES (1758294229802000000, 1758235382057001800, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229803000000, 1758240288180000000, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229805000000, 1758240288180000300, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229807000000, 1758240288180001100, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229808000000, 1758240288180001600, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229810000000, 1758240288180001700, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229811000000, 1758240288180002200, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229813000000, 1758240288180002400, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229814000000, 1758240288180003500, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229816000000, 1758240288180004500, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229818000000, 1758240288180005100, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229819000000, 1758240288180005200, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229821000000, 1758240288180005400, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229823000000, 1758240288180005900, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229824000000, 1758240288180006000, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229825000000, 1758240288180007000, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229827000000, 1758240288180007600, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229828000000, 1758240288180007700, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229830000000, 1758240288180009000, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758294229832000000, 1758240288180009100, 1, '2025-09-19 23:03:49');
INSERT INTO `sys_group_permission` VALUES (1758410304507000000, 1758333555880000000, 1, '2025-09-21 07:18:24');
INSERT INTO `sys_group_permission` VALUES (1758410304509000000, 1758410304049000000, 1, '2025-09-21 07:18:24');

-- ----------------------------
-- Table structure for sys_group_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_group_table`;
CREATE TABLE `sys_group_table`  (
  `sys_group_table_id` bigint(20) NOT NULL,
  `sys_group_table_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名称',
  `sys_group_table_gid` bigint(20) NULL DEFAULT NULL COMMENT '归属组ID',
  `sys_group_table_but_save_open` bigint(20) NULL DEFAULT NULL COMMENT '全局-按钮-添加',
  `sys_group_table_but_delete_open` bigint(20) NULL DEFAULT NULL COMMENT '全局-按钮-删除',
  `sys_group_table_table_update_open` bigint(20) NULL DEFAULT NULL COMMENT '表格-按钮-修改',
  `sys_group_table_table_delete_open` bigint(20) NULL DEFAULT NULL COMMENT '表格-按钮-删除',
  `sys_group_table_but_query_open` bigint(20) NULL DEFAULT NULL COMMENT '表格-按钮-查询',
  `sys_group_table_table_but_width` bigint(20) NULL DEFAULT NULL COMMENT '表格-按钮-宽度',
  PRIMARY KEY (`sys_group_table_id`) USING BTREE,
  INDEX `suoyin_sys_group_table_key`(`sys_group_table_key`) USING BTREE,
  INDEX `suoyin_sys_group_table_gid`(`sys_group_table_gid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_group_table
-- ----------------------------
INSERT INTO `sys_group_table` VALUES (1758075113773000000, 'sysFile', 1, 0, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075113804000000, 'sysGroup', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075113829000000, 'sysGroupColumn', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075113889000000, 'sysGroupPermission', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075113909000000, 'sysGroupTable', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075113947000000, 'sysLog', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075113965000000, 'sysMapping', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075114016000000, 'sysOption', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075114038000000, 'sysPermission', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075114071000000, 'sysUsers', 1, 1, 1, 1, 1, 1, 0);
INSERT INTO `sys_group_table` VALUES (1758075114131000000, 'sysUsersLoginLog', 1, 1, 1, 1, 1, 1, 0);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `sys_log_id` bigint(20) NOT NULL COMMENT '日志ID',
  `sys_log_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `sys_log_ms` bigint(20) NULL DEFAULT NULL COMMENT '耗时',
  `sys_log_sql` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'sql',
  PRIMARY KEY (`sys_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, '2025-09-21 08:57:49', 1, NULL);

-- ----------------------------
-- Table structure for sys_mapping
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapping`;
CREATE TABLE `sys_mapping`  (
  `sys_mapping_id` bigint(20) NOT NULL COMMENT '基础映射信息表',
  `sys_mapping_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列查询key',
  `sys_mapping_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列标题',
  `sys_mapping_val` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列名称',
  `sys_mapping_table_width` bigint(20) NULL DEFAULT NULL COMMENT '本列宽度',
  `sys_mapping_select` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列非空为select内容体支持sql',
  `sys_mapping_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列非空为图片模板',
  `sys_mapping_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列非空为文件模板',
  `sys_mapping_date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列非空为时间格式',
  `sys_mapping_script` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本列非空为执行js，极端条件下再用',
  `sys_mapping_sort` bigint(20) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`sys_mapping_id`) USING BTREE,
  INDEX `suoyin_sys_mapping_key`(`sys_mapping_key`) USING BTREE,
  INDEX `suoyin_sys_mapping_sort`(`sys_mapping_sort`) USING BTREE,
  INDEX `suoyin_sys_mapping_val`(`sys_mapping_val`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_mapping
-- ----------------------------
INSERT INTO `sys_mapping` VALUES (1747771167222000001, 'sysFile', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 11);
INSERT INTO `sys_mapping` VALUES (1747771167230000001, 'sysFile', 'ID', 'sys_file_id', 120, NULL, NULL, NULL, NULL, NULL, 102);
INSERT INTO `sys_mapping` VALUES (1747771167235000001, 'sysFile', '路径', 'sys_file_path', 120, NULL, NULL, NULL, NULL, NULL, 103);
INSERT INTO `sys_mapping` VALUES (1747771167240000001, 'sysFile', 'MD5', 'sys_file_md5', 120, NULL, NULL, NULL, NULL, NULL, 104);
INSERT INTO `sys_mapping` VALUES (1747771167245000001, 'sysFile', '时间', 'sys_file_time', 120, NULL, NULL, NULL, 'datetime', NULL, 105);
INSERT INTO `sys_mapping` VALUES (1747771167248000001, 'sysFile', '类型', 'sys_file_type', 120, NULL, NULL, NULL, NULL, NULL, 106);
INSERT INTO `sys_mapping` VALUES (1747771167251000001, 'sysGroup', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 17);
INSERT INTO `sys_mapping` VALUES (1747771167255000001, 'sysGroup', 'ID', 'sys_group_id', 120, NULL, NULL, NULL, NULL, NULL, 108);
INSERT INTO `sys_mapping` VALUES (1747771167259000001, 'sysGroup', '组名称', 'sys_group_name', 120, NULL, NULL, NULL, NULL, NULL, 109);
INSERT INTO `sys_mapping` VALUES (1747771167263000001, 'sysGroup', '组类型', 'sys_group_type', 120, NULL, NULL, NULL, NULL, NULL, 110);
INSERT INTO `sys_mapping` VALUES (1747771167265000001, 'sysGroup', '组备注', 'sys_group_desc', 120, NULL, NULL, NULL, NULL, NULL, 111);
INSERT INTO `sys_mapping` VALUES (1747771167268000001, 'sysGroup', '组上级', 'sys_group_sup', 120, NULL, NULL, NULL, NULL, NULL, 112);
INSERT INTO `sys_mapping` VALUES (1747771167270000001, 'sysGroupPermission', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 23);
INSERT INTO `sys_mapping` VALUES (1747771167274000001, 'sysGroupPermission', 'ID', 'sys_group_permission_id', 120, NULL, NULL, NULL, NULL, NULL, 114);
INSERT INTO `sys_mapping` VALUES (1747771167277000001, 'sysGroupPermission', '权限名', 'sys_group_permission_pid', 120, 'select sys_permission.sys_permission_name as sys_option_title,sys_permission.sys_permission_id as sys_option_value from sys_permission order by sys_permission_sort', NULL, NULL, NULL, NULL, 115);
INSERT INTO `sys_mapping` VALUES (1747771167280000001, 'sysGroupPermission', '组名称', 'sys_group_permission_gid', 120, 'select sys_group.sys_group_name as sys_option_title,sys_group.sys_group_id as sys_option_value from sys_group  order by sys_group_id', NULL, NULL, NULL, NULL, 116);
INSERT INTO `sys_mapping` VALUES (1747771167283000001, 'sysGroupPermission', '时间', 'sys_group_permission_time', 120, NULL, NULL, NULL, NULL, NULL, 117);
INSERT INTO `sys_mapping` VALUES (1747771167285000001, 'sysLog', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 28);
INSERT INTO `sys_mapping` VALUES (1747771167288000001, 'sysLog', 'ID', 'sys_log_id', 120, NULL, NULL, NULL, NULL, NULL, 119);
INSERT INTO `sys_mapping` VALUES (1747771167292000001, 'sysLog', '时间', 'sys_log_time', 120, NULL, NULL, NULL, NULL, NULL, 120);
INSERT INTO `sys_mapping` VALUES (1747771167295000001, 'sysLog', '耗时', 'sys_log_ms', 120, NULL, NULL, NULL, NULL, NULL, 121);
INSERT INTO `sys_mapping` VALUES (1747771167298000001, 'sysLog', 'SQL', 'sys_log_sql', 120, NULL, NULL, NULL, NULL, NULL, 122);
INSERT INTO `sys_mapping` VALUES (1747771167300000001, 'sysMapping', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 10);
INSERT INTO `sys_mapping` VALUES (1747771167303000001, 'sysMapping', '索引', 'sys_mapping_id', 120, NULL, NULL, NULL, NULL, NULL, 302);
INSERT INTO `sys_mapping` VALUES (1747771167306000001, 'sysMapping', '查询', 'sys_mapping_key', 120, NULL, NULL, NULL, NULL, NULL, 301);
INSERT INTO `sys_mapping` VALUES (1747771167309000001, 'sysMapping', '标题', 'sys_mapping_title', 120, NULL, NULL, NULL, NULL, NULL, 100);
INSERT INTO `sys_mapping` VALUES (1747771167313000001, 'sysMapping', '字段', 'sys_mapping_val', 120, NULL, NULL, NULL, NULL, NULL, 110);
INSERT INTO `sys_mapping` VALUES (1747771167317000001, 'sysMapping', '宽度', 'sys_mapping_table_width', 120, NULL, NULL, NULL, NULL, NULL, 301);
INSERT INTO `sys_mapping` VALUES (1747771167342000001, 'sysMapping', '选择项', 'sys_mapping_select', 120, NULL, NULL, NULL, NULL, NULL, 190);
INSERT INTO `sys_mapping` VALUES (1747771167345000001, 'sysMapping', '图片模板', 'sys_mapping_image', 120, NULL, NULL, NULL, NULL, NULL, 191);
INSERT INTO `sys_mapping` VALUES (1747771167348000001, 'sysMapping', '文件模板', 'sys_mapping_file', 120, NULL, NULL, NULL, NULL, NULL, 192);
INSERT INTO `sys_mapping` VALUES (1747771167350000001, 'sysMapping', '时间模板', 'sys_mapping_date', 120, NULL, NULL, NULL, NULL, NULL, 193);
INSERT INTO `sys_mapping` VALUES (1747771167367000001, 'sysMapping', '脚本模板', 'sys_mapping_script', 120, NULL, NULL, NULL, NULL, NULL, 195);
INSERT INTO `sys_mapping` VALUES (1747771167386000001, 'sysMapping', '本列排序', 'sys_mapping_sort', 120, NULL, NULL, NULL, NULL, NULL, 301);
INSERT INTO `sys_mapping` VALUES (1747771167390000001, 'sysOption', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 60);
INSERT INTO `sys_mapping` VALUES (1747771167393000001, 'sysOption', 'ID', 'sys_option_id', 120, NULL, NULL, NULL, NULL, NULL, 151);
INSERT INTO `sys_mapping` VALUES (1747771167396000001, 'sysOption', '标识', 'sys_option_key', 120, NULL, NULL, NULL, NULL, NULL, 152);
INSERT INTO `sys_mapping` VALUES (1747771167400000001, 'sysOption', '标题', 'sys_option_title', 120, NULL, NULL, NULL, NULL, NULL, 153);
INSERT INTO `sys_mapping` VALUES (1747771167404000001, 'sysOption', '值', 'sys_option_value', 120, NULL, NULL, NULL, NULL, NULL, 154);
INSERT INTO `sys_mapping` VALUES (1747771167408000001, 'sysOption', '状态', 'sys_option_state', 120, 'key:def_state', NULL, NULL, NULL, NULL, 155);
INSERT INTO `sys_mapping` VALUES (1747771167411000001, 'sysPermission', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 66);
INSERT INTO `sys_mapping` VALUES (1747771167414000001, 'sysPermission', 'ID', 'sys_permission_id', 120, NULL, NULL, NULL, NULL, NULL, 157);
INSERT INTO `sys_mapping` VALUES (1747771167416000001, 'sysPermission', '权限名', 'sys_permission_name', 120, NULL, NULL, NULL, NULL, NULL, 158);
INSERT INTO `sys_mapping` VALUES (1747771167420000001, 'sysPermission', '权限数据', 'sys_permission_data', 120, NULL, NULL, NULL, NULL, NULL, 159);
INSERT INTO `sys_mapping` VALUES (1747771167424000001, 'sysPermission', '权限类型', 'sys_permission_type', 120, 'key:sys_permission_type', NULL, NULL, NULL, NULL, 160);
INSERT INTO `sys_mapping` VALUES (1747771167427000001, 'sysPermission', '备注信息', 'sys_permission_desc', 120, NULL, NULL, NULL, NULL, NULL, 161);
INSERT INTO `sys_mapping` VALUES (1747771167430000001, 'sysPermission', '上级', 'sys_permission_sup', 120, 'select sys_permission.sys_permission_name as sys_option_title,sys_permission.sys_permission_id as sys_option_value from sys_permission where sys_permission_sup = 0 order by sys_permission_sort', NULL, NULL, NULL, NULL, 162);
INSERT INTO `sys_mapping` VALUES (1747771167432000001, 'sysPermission', '排序依据', 'sys_permission_sort', 120, NULL, NULL, NULL, NULL, NULL, 163);
INSERT INTO `sys_mapping` VALUES (1747771167435000001, 'sysUsers', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 74);
INSERT INTO `sys_mapping` VALUES (1747771167439000001, 'sysUsers', '唯一ID', 'sys_users_id', 120, NULL, NULL, NULL, NULL, NULL, 165);
INSERT INTO `sys_mapping` VALUES (1747771167443000001, 'sysUsers', '账号', 'sys_users_acc', 120, NULL, NULL, NULL, NULL, NULL, 166);
INSERT INTO `sys_mapping` VALUES (1747771167446000001, 'sysUsers', '密码', 'sys_users_pwd', 120, NULL, NULL, NULL, NULL, NULL, 167);
INSERT INTO `sys_mapping` VALUES (1747771167450000001, 'sysUsers', '手机', 'sys_users_phone', 120, NULL, NULL, NULL, NULL, NULL, 168);
INSERT INTO `sys_mapping` VALUES (1747771167454000001, 'sysUsers', '公开ID', 'sys_users_open_id', 120, '', '/system/v1.0.0/read/file?sysFileMd5=${sysUsersOpenId}', NULL, NULL, NULL, 169);
INSERT INTO `sys_mapping` VALUES (1747771167459000001, 'sysUsers', '状态', 'sys_users_status', 120, 'key:def_state', NULL, NULL, NULL, NULL, 170);
INSERT INTO `sys_mapping` VALUES (1747771167462000001, 'sysUsers', '类型', 'sys_users_type', 120, 'key:sys_users_type', NULL, NULL, NULL, NULL, 171);
INSERT INTO `sys_mapping` VALUES (1747771167466000001, 'sysUsers', '注册时间', 'sys_users_reg_time', 120, NULL, NULL, NULL, 'datetime', NULL, 172);
INSERT INTO `sys_mapping` VALUES (1747771167469000001, 'sysUsers', '授权开始', 'sys_users_start_time', 120, NULL, NULL, NULL, 'datetime', NULL, 173);
INSERT INTO `sys_mapping` VALUES (1747771167473000001, 'sysUsers', '授权结束', 'sys_users_end_time', 120, NULL, NULL, NULL, 'datetime', NULL, 174);
INSERT INTO `sys_mapping` VALUES (1747771167476000001, 'sysUsers', '余额', 'sys_users_price', 120, NULL, NULL, NULL, NULL, NULL, 175);
INSERT INTO `sys_mapping` VALUES (1747771167479000001, 'sysUsers', '备注', 'sys_users_desc', 120, NULL, NULL, NULL, NULL, NULL, 176);
INSERT INTO `sys_mapping` VALUES (1747771167483000001, 'sysUsers', '上级', 'sys_users_sup', 120, NULL, NULL, NULL, NULL, NULL, 177);
INSERT INTO `sys_mapping` VALUES (1747771167486000001, 'sysUsers', '邮箱', 'sys_users_mail', 120, NULL, NULL, NULL, NULL, NULL, 178);
INSERT INTO `sys_mapping` VALUES (1747771167490000001, 'sysUsers', '权限组', 'sys_users_group', 120, 'select sys_group.sys_group_id as sys_option_value,sys_group.sys_group_name as sys_option_title from sys_group', NULL, NULL, NULL, NULL, 179);
INSERT INTO `sys_mapping` VALUES (1747771167493000001, 'sysUsersLoginLog', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 90);
INSERT INTO `sys_mapping` VALUES (1747771167496000001, 'sysUsersLoginLog', 'ID', 'sys_users_login_log_id', 120, NULL, NULL, NULL, NULL, NULL, 181);
INSERT INTO `sys_mapping` VALUES (1747771167499000001, 'sysUsersLoginLog', 'IP', 'sys_users_login_log_ip', 120, NULL, NULL, NULL, NULL, NULL, 182);
INSERT INTO `sys_mapping` VALUES (1747771167501000001, 'sysUsersLoginLog', '备注', 'sys_users_login_log_desc', 120, NULL, NULL, NULL, NULL, NULL, 183);
INSERT INTO `sys_mapping` VALUES (1747771167504000001, 'sysUsersLoginLog', '时间', 'sys_users_login_log_time', 120, NULL, NULL, NULL, NULL, NULL, 184);
INSERT INTO `sys_mapping` VALUES (1747771167508000001, 'sysUsersLoginLog', '用户', 'sys_users_login_log_uid', 120, 'select sys_users_acc as sys_option_title,sys_users_id as sys_option_value from sys_users', NULL, NULL, NULL, NULL, 185);
INSERT INTO `sys_mapping` VALUES (1747771167511000001, 'sysUsersLoginLog', '令牌', 'sys_users_login_log_token', 120, NULL, NULL, NULL, NULL, NULL, 186);
INSERT INTO `sys_mapping` VALUES (1747771167514000001, 'sysUsersLoginLog', '机器码', 'sys_users_login_log_mac', 120, NULL, NULL, NULL, NULL, NULL, 187);
INSERT INTO `sys_mapping` VALUES (1748216737103000001, 'sysGroupColumn', 'ID', 'sys_group_column_id', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737107000001, 'sysGroupColumn', '关键词', 'sys_group_column_key', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737111000001, 'sysGroupColumn', '列名', 'sys_group_column_name', 200, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737115000001, 'sysGroupColumn', '参与显示', 'sys_group_column_table', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737118000001, 'sysGroupColumn', '参与编辑', 'sys_group_column_edit', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737123000001, 'sysGroupColumn', '参与修改', 'sys_group_column_update', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737126000001, 'sysGroupColumn', '参与添加', 'sys_group_column_save', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737130000001, 'sysGroupColumn', '保存默认', 'sys_group_column_save_def', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737134000001, 'sysGroupColumn', '修改默认', 'sys_group_column_update_def', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737138000001, 'sysGroupColumn', '运算符', 'sys_group_column_query_symbol', 100, 'key:def_symbol', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737143000001, 'sysGroupColumn', '连接符', 'sys_group_column_query_montage', 100, 'key:def_montage', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737146000001, 'sysGroupColumn', '查询默认', 'sys_group_column_query_def', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737149001001, 'sysGroupColumn', '归属组', 'sys_group_column_gid', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737167000001, 'sysGroupTable', 'ID', 'sys_group_table_id', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737172000001, 'sysGroupTable', '关键词', 'sys_group_table_key', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737176000001, 'sysGroupTable', '归属组', 'sys_group_table_gid', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737180000001, 'sysGroupTable', '保存按钮', 'sys_group_table_but_save_open', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737184000001, 'sysGroupTable', '删除按钮', 'sys_group_table_but_delete_open', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737189000001, 'sysGroupTable', '表格修改', 'sys_group_table_table_update_open', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737192000001, 'sysGroupTable', '表格删除', 'sys_group_table_table_delete_open', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737196000001, 'sysGroupTable', '查询按钮', 'sys_group_table_but_query_open', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748216737200000001, 'sysGroupTable', '操作列宽', 'sys_group_table_table_but_width', 100, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_mapping` VALUES (1748218875494000001, 'sysGroupColumn', '参与查询', 'sys_group_column_query', 100, 'key:def_open_end', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_option
-- ----------------------------
DROP TABLE IF EXISTS `sys_option`;
CREATE TABLE `sys_option`  (
  `sys_option_id` bigint(20) NOT NULL COMMENT '选项id',
  `sys_option_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项名称',
  `sys_option_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项标题',
  `sys_option_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项值',
  `sys_option_state` bigint(255) NULL DEFAULT NULL COMMENT '选项状态',
  PRIMARY KEY (`sys_option_id`) USING BTREE,
  INDEX `suoyin_sys_option_key`(`sys_option_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_option
-- ----------------------------
INSERT INTO `sys_option` VALUES (1, 'def_state', '否定/禁止/反向', '0', 1);
INSERT INTO `sys_option` VALUES (2, 'def_state', '确定/正常/正向', '1', 1);
INSERT INTO `sys_option` VALUES (5, 'def_open_end', '开', '1', 1);
INSERT INTO `sys_option` VALUES (6, 'def_open_end', '关', '0', 1);
INSERT INTO `sys_option` VALUES (7, 'def_montage', '并且[1]', '1', 1);
INSERT INTO `sys_option` VALUES (8, 'def_montage', '或者[2]', '2', 1);
INSERT INTO `sys_option` VALUES (9, 'def_montage', '并且([3]', '3', 1);
INSERT INTO `sys_option` VALUES (10, 'def_montage', '或者([4]', '4', 1);
INSERT INTO `sys_option` VALUES (11, 'def_montage', ')并且[5]', '5', 1);
INSERT INTO `sys_option` VALUES (12, 'def_montage', ')或者[6]', '6', 1);
INSERT INTO `sys_option` VALUES (13, 'def_montage', '([7]', '7', 1);
INSERT INTO `sys_option` VALUES (14, 'def_montage', ')[8]', '8', 1);
INSERT INTO `sys_option` VALUES (15, 'def_symbol', '等于[1]', '1', 1);
INSERT INTO `sys_option` VALUES (16, 'def_symbol', '大于[2]', '2', 1);
INSERT INTO `sys_option` VALUES (17, 'def_symbol', '大于等于[3]', '3', 1);
INSERT INTO `sys_option` VALUES (18, 'def_symbol', '小于[4]', '4', 1);
INSERT INTO `sys_option` VALUES (19, 'def_symbol', '小于等于[5]', '5', 1);
INSERT INTO `sys_option` VALUES (20, 'def_symbol', '不等于[6]', '6', 1);
INSERT INTO `sys_option` VALUES (21, 'def_symbol', '%模糊%[7]', '7', 1);
INSERT INTO `sys_option` VALUES (22, 'def_symbol', '模糊%[8]', '8', 1);
INSERT INTO `sys_option` VALUES (23, 'def_symbol', '%模糊[9]', '9', 1);
INSERT INTO `sys_option` VALUES (24, 'sys_users_type', '临时用户', '1', 1);
INSERT INTO `sys_option` VALUES (25, 'sys_users_type', '普通用户', '2', 1);
INSERT INTO `sys_option` VALUES (26, 'sys_users_type', '管理员用户', '3', 1);
INSERT INTO `sys_option` VALUES (27, 'sys_users_type', '超级管理员', '4', 1);
INSERT INTO `sys_option` VALUES (28, 'sys_users_type', '限时授权用户', '5', 1);
INSERT INTO `sys_option` VALUES (29, 'sys_permission_type', '页面权限', '1', 1);
INSERT INTO `sys_option` VALUES (30, 'sys_permission_type', '接口权限', '2', 1);
INSERT INTO `sys_option` VALUES (31, 'def_sex', '男', '1', 1);
INSERT INTO `sys_option` VALUES (32, 'def_sex', '女', '2', 1);
INSERT INTO `sys_option` VALUES (33, 'def_start_end', '结束', '0', 1);
INSERT INTO `sys_option` VALUES (34, 'def_start_end', '开始', '1', 1);
INSERT INTO `sys_option` VALUES (35, 'def_yes_no', '是', '1', 1);
INSERT INTO `sys_option` VALUES (36, 'def_yes_no', '否', '0', 1);
INSERT INTO `sys_option` VALUES (40, 'application_type', '全动态代码项目', '1', 1);
INSERT INTO `sys_option` VALUES (41, 'application_type', '非动态代码项目', '2', 1);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `sys_permission_id` bigint(20) NOT NULL COMMENT '权限名称',
  `sys_permission_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `sys_permission_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限数据',
  `sys_permission_type` bigint(20) NULL DEFAULT NULL COMMENT '权限类型',
  `sys_permission_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限备注',
  `sys_permission_sup` bigint(20) NULL DEFAULT NULL COMMENT '权限上级',
  `sys_permission_sort` bigint(20) NULL DEFAULT NULL COMMENT '权限排序',
  PRIMARY KEY (`sys_permission_id`) USING BTREE,
  INDEX `suoyin_sys_permission_name`(`sys_permission_name`) USING BTREE,
  INDEX `suoyin_sys_permission_sup`(`sys_permission_sup`) USING BTREE,
  INDEX `suoyin_sys_permission_sort`(`sys_permission_sort`) USING BTREE,
  INDEX `suoyin_sys_permission_type`(`sys_permission_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '开发者配置', NULL, 1, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (2, '文件列表', 'list.html?config=sysFile', 1, NULL, 1, 0);
INSERT INTO `sys_permission` VALUES (3, '数据库映射', 'list.html?config=sysMapping', 1, NULL, 1, 0);
INSERT INTO `sys_permission` VALUES (4, '权限列表', 'list.html?config=sysPermission', 1, NULL, 1, 0);
INSERT INTO `sys_permission` VALUES (5, '权限组管理', 'list.html?config=sysGroup&edit=sysGroup-01', 1, NULL, 1, 0);
INSERT INTO `sys_permission` VALUES (6, '用户列表', 'list.html?config=sysUsers', 1, NULL, 1, 0);
INSERT INTO `sys_permission` VALUES (8, '用户日志', 'list.html?config=sysUsersLoginLog', 1, '', 1, 0);
INSERT INTO `sys_permission` VALUES (1748214311232000001, 'frame', NULL, 1, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214311240000001, '文件列表', 'list.html?config=sysFile&edit=sysFile', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311244000001, 'sys_group', 'list.html?config=sysGroup&edit=sysGroup', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311247000001, 'sys_group_column', 'list.html?config=sysGroupColumn&edit=sysGroupColumn', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311251000001, 'sys_group_permission', 'list.html?config=sysGroupPermission&edit=sysGroupPermission', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311254000001, 'sys_group_table', 'list.html?config=sysGroupTable&edit=sysGroupTable', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311257000001, 'sys_log', 'list.html?config=sysLog&edit=sysLog', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311260000001, 'sys_mapping', 'list.html?config=sysMapping&edit=sysMapping', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311263000001, 'sys_option', 'list.html?config=sysOption&edit=sysOption', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311265000001, 'sys_permission', 'list.html?config=sysPermission&edit=sysPermission', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311268000001, 'sys_users', 'list.html?config=sysUsers&edit=sysUsers', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214311271000001, 'sys_users_login_log', 'list.html?config=sysUsersLoginLog&edit=sysUsersLoginLog', 1, NULL, 1748214311232000001, 0);
INSERT INTO `sys_permission` VALUES (1748214313833000001, '/system/v1.0.0/update/mapping/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313837000001, '/system/v1.0.0/sysGroupColumn/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313839000001, '/system/v1.0.0/sysMapping/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313841000001, '/system/v1.0.0/read/user/info/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313843000001, '/system/v1.0.0/sysPermission/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313845000001, '/system/v1.0.0/sysOption/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313846000001, '/system/v1.0.0/sysUsersLoginLog/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313847000001, '/system/v1.0.0/sysLog/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313849000001, '/system/v1.0.0/reg5/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313850000001, '/system/v1.0.0/sysOption/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313852000001, '/system/v1.0.0/sysGroup/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313854000001, '/system/v1.0.0/sysGroupColumn/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313857000001, '/system/v1.0.0/sysGroup/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313859000001, '/system/v1.0.0/login/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313860000001, '/system/v1.0.0/sysPermission/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313862000001, '/system/v1.0.0/sysMapping/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313864000001, '/system/v1.0.0/sysOption/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313865000001, '/system/v1.0.0/sysUsers/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313867000001, '/system/v1.0.0/upload/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313869000001, '/system/v1.0.0/sysUsers/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313872000001, '/system/v1.0.0/sysGroupColumn/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313875000001, '/system/v1.0.0/sysLog/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313877000001, '/system/v1.0.0/sysLog/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313879000001, '/system/v1.0.0/sysOption/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313881000001, '/system/v1.0.0/sysFile/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313883000001, '/system/v1.0.0/sysFile/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313885000001, '/system/v1.0.0/read/permission/all/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313886000001, '/system/v1.0.0/image/code/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313888000001, '/system/v1.0.0/sysGroupPermission/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313890000001, '/system/v1.0.0/sysFile/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313892000001, '/system/v1.0.0/sysPermission/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313894000001, '/system/v1.0.0/read/permission/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313896000001, '/system/v1.0.0/sysGroupPermission/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313898000001, '/system/v1.0.0/sysGroupTable/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313899000001, '/system/v1.0.0/sysUsersLoginLog/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313901000001, '/system/v1.0.0/sysGroupPermission/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313902000001, '/system/v1.0.0/sysMapping/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313904000001, '/system/v1.0.0/sysMapping/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313906000001, '/system/v1.0.0/sysGroupTable/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313908000001, '/system/v1.0.0/sysGroup/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313911000001, '/system/v1.0.0/sysGroupColumn/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313912000001, '/system/v1.0.0/sysMapping/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313914000001, '/system/v1.0.0/sysMapping/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313916000001, '/system/v1.0.0/sysUsersLoginLog/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313917000001, '/system/v1.0.0/sysGroup/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313919000001, '/system/v1.0.0/sysGroupTable/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313922000001, '/system/v1.0.0/sysMapping/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313927000001, '/system/v1.0.0/sysUsersLoginLog/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313929000001, '/system/v1.0.0/sysGroupPermission/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313930000001, '/system/v1.0.0/sysLog/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313932000001, '/system/v1.0.0/sysGroupColumn/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313933000001, '/system/v1.0.0/sysGroupPermission/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313935000001, '/system/v1.0.0/sysOption/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313937000001, '/system/v1.0.0/sysLog/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313939000001, '/system/v1.0.0/sysGroupColumn/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313941000001, '/system/v1.0.0/update/updatePermissionAndMapping/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313943000001, '/system/v1.0.0/sysGroupTable/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313946000001, '/system/v1.0.0/sysGroup/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313948000001, '/system/v1.0.0/sysPermission/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313949000001, '/system/v1.0.0/sysGroupColumn/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313951000001, '/system/v1.0.0/authorize/permission/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313953000001, '/system/v1.0.0/sysGroupTable/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313955000001, '/system/v1.0.0/sysUsers/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313958000001, '/system/v1.0.0/sysFile/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313960000001, '/system/v1.0.0/sysUsersLoginLog/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313962000001, '/system/v1.0.0/sysLog/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313964000001, '/system/v1.0.0/sysUsersLoginLog/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313965000001, '/system/v1.0.0/sysFile/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313967000001, '/system/v1.0.0/read/file/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313968000001, '/system/v1.0.0/sysFile/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313970000001, '/system/v1.0.0/sysPermission/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313971000001, '/system/v1.0.0/sysFile/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313973000001, '/system/v1.0.0/sysGroup/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313976000001, '/system/v1.0.0/sysGroupTable/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313978000001, '/system/v1.0.0/sysUsersLoginLog/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313980000001, '/system/v1.0.0/sysUsers/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313982000001, '/system/v1.0.0/sysGroupPermission/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313983000001, '/system/v1.0.0/sysLog/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313985000001, '/system/v1.0.0/sysGroupTable/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313986000001, '/system/v1.0.0/sysUsers/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313989000001, '/system/v1.0.0/sysOption/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313991000001, '/system/v1.0.0/sysPermission/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313993000001, '/system/v1.0.0/sysUsers/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313995000001, '/system/v1.0.0/read/mapping/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313996000001, '/system/v1.0.0/sysGroupPermission/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214313998000001, '/system/v1.0.0/sysOption/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214314000000001, '/system/v1.0.0/exit/user/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214314001000001, '/system/v1.0.0/sysPermission/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214314003000001, '/system/v1.0.0/sysUsers/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214314005000001, '/system/v1.0.0/sysGroup/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758076467581004200, '/test/test1/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758235382057001800, '/test/test2/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180000000, '/test/sysUsers/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180000300, '/test/read/file/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180001100, '/test/upload/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180001600, '/test/read/mapping/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180001700, '/test/read/permission/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180002200, '/test/read/permission/all/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180002400, '/test/sysUsers/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180003500, '/test/read/user/info/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180004500, '/test/update/updatePermissionAndMapping/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180005100, '/test/sysUsers/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180005200, '/test/sysUsers/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180005400, '/test/authorize/permission/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180005900, '/test/exit/user/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180006000, '/test/reg5/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180007000, '/test/image/code/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180007600, '/test/sysUsers/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180007700, '/test/login/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180009000, '/test/sysUsers/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758240288180009100, '/test/sysUsers/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1758333555880000000, '选项中心', 'list.html?config=sysOption', 1, NULL, 1, 0);
INSERT INTO `sys_permission` VALUES (1758410304049000000, 'sys_file', 'list.html?config=sysFile&edit=sysFile', 1, NULL, 1748214311232000001, 0);

-- ----------------------------
-- Table structure for sys_users
-- ----------------------------
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users`  (
  `sys_users_id` bigint(20) NOT NULL COMMENT 'ID',
  `sys_users_acc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `sys_users_pwd` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `sys_users_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `sys_users_open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方',
  `sys_users_status` bigint(20) NULL DEFAULT NULL COMMENT '状态',
  `sys_users_type` bigint(19) NULL DEFAULT NULL COMMENT '类型',
  `sys_users_reg_time` datetime(0) NULL DEFAULT NULL COMMENT '注册',
  `sys_users_start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始',
  `sys_users_end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束',
  `sys_users_price` bigint(19) NULL DEFAULT NULL COMMENT '金额',
  `sys_users_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sys_users_sup` bigint(19) NULL DEFAULT NULL COMMENT '上级',
  `sys_users_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sys_users_group` bigint(255) NULL DEFAULT NULL COMMENT '权限组',
  PRIMARY KEY (`sys_users_id`) USING BTREE,
  INDEX `suoyin_sys_users_acc`(`sys_users_acc`) USING BTREE,
  INDEX `suoyin_sys_users_open_id`(`sys_users_open_id`) USING BTREE,
  INDEX `suoyin_sys_users_reg_time`(`sys_users_reg_time`) USING BTREE,
  INDEX `suoyin_sys_users_end_time`(`sys_users_end_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_users
-- ----------------------------
INSERT INTO `sys_users` VALUES (1, 'admin', 'admin', NULL, '', 1, 4, '2025-05-22 00:00:00', '2025-05-22 02:16:12', '2025-05-22 02:15:48', NULL, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for sys_users_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_users_login_log`;
CREATE TABLE `sys_users_login_log`  (
  `sys_users_login_log_id` bigint(20) NOT NULL,
  `sys_users_login_log_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sys_users_login_log_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sys_users_login_log_time` datetime(0) NULL DEFAULT NULL,
  `sys_users_login_log_uid` bigint(20) NULL DEFAULT NULL,
  `sys_users_login_log_token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sys_users_login_log_mac` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sys_users_login_log_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
