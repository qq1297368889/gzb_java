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

 Date: 29/08/2025 00:06:28
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
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1748290817378000001, 'resources/0f/5d/3c/0f5d3c556d09dad7e2a24cd284f64058/20240806180806.jpg', '0f5d3c556d09dad7e2a24cd284f64058', '2025-05-27 04:20:17', 'image/jpeg');

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
INSERT INTO `sys_group` VALUES (-100, '默认权限_注册_限时用户', 1, NULL, 0);
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
INSERT INTO `sys_group_column` VALUES (1748214314014000001, 'sysFile', 'sys_file_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314019000001, 'sysFile', 'sys_file_path', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314024000001, 'sysFile', 'sys_file_md5', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314029000001, 'sysFile', 'sys_file_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314032000001, 'sysFile', 'sys_file_type', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314039000001, 'sysGroup', 'sys_group_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314043000001, 'sysGroup', 'sys_group_name', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314047000001, 'sysGroup', 'sys_group_type', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314051000001, 'sysGroup', 'sys_group_desc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314054000001, 'sysGroup', 'sys_group_sup', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314060000001, 'sysGroupColumn', 'sys_group_column_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314064000001, 'sysGroupColumn', 'sys_group_column_key', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314068000001, 'sysGroupColumn', 'sys_group_column_name', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314073000001, 'sysGroupColumn', 'sys_group_column_table', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314078000001, 'sysGroupColumn', 'sys_group_column_edit', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314082000001, 'sysGroupColumn', 'sys_group_column_update', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314086000001, 'sysGroupColumn', 'sys_group_column_save', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314092000001, 'sysGroupColumn', 'sys_group_column_save_def', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314096000001, 'sysGroupColumn', 'sys_group_column_update_def', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314100000001, 'sysGroupColumn', 'sys_group_column_query_symbol', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314105000001, 'sysGroupColumn', 'sys_group_column_query_montage', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314110000001, 'sysGroupColumn', 'sys_group_column_query_def', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314114000001, 'sysGroupColumn', 'sys_group_column_gid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314120000001, 'sysGroupPermission', 'sys_group_permission_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314126000001, 'sysGroupPermission', 'sys_group_permission_pid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314130000001, 'sysGroupPermission', 'sys_group_permission_gid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314135000001, 'sysGroupPermission', 'sys_group_permission_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314145000001, 'sysGroupTable', 'sys_group_table_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314149000001, 'sysGroupTable', 'sys_group_table_key', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314153000001, 'sysGroupTable', 'sys_group_table_gid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314158000001, 'sysGroupTable', 'sys_group_table_but_save_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314162000001, 'sysGroupTable', 'sys_group_table_but_delete_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314167000001, 'sysGroupTable', 'sys_group_table_table_update_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314172000001, 'sysGroupTable', 'sys_group_table_table_delete_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314177000001, 'sysGroupTable', 'sys_group_table_but_query_open', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314180000001, 'sysGroupTable', 'sys_group_table_table_but_width', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314187000001, 'sysLog', 'sys_log_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314192000001, 'sysLog', 'sys_log_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314197000001, 'sysLog', 'sys_log_ms', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314201000001, 'sysLog', 'sys_log_sql', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314207000001, 'sysMapping', 'sys_mapping_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314211000001, 'sysMapping', 'sys_mapping_key', 1, 1, 1, 1, 1, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314215000001, 'sysMapping', 'sys_mapping_title', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314218000001, 'sysMapping', 'sys_mapping_val', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314221000001, 'sysMapping', 'sys_mapping_table_width', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314226000001, 'sysMapping', 'sys_mapping_select', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314231000001, 'sysMapping', 'sys_mapping_image', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314235000001, 'sysMapping', 'sys_mapping_file', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314239000001, 'sysMapping', 'sys_mapping_date', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314244000001, 'sysMapping', 'sys_mapping_script', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314248000001, 'sysMapping', 'sys_mapping_sort', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314253000001, 'sysOption', 'sys_option_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314258000001, 'sysOption', 'sys_option_key', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314262000001, 'sysOption', 'sys_option_title', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314265000001, 'sysOption', 'sys_option_value', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314269000001, 'sysOption', 'sys_option_state', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314275000001, 'sysPermission', 'sys_permission_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314279000001, 'sysPermission', 'sys_permission_name', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314283000001, 'sysPermission', 'sys_permission_data', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314286000001, 'sysPermission', 'sys_permission_type', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314292000001, 'sysPermission', 'sys_permission_desc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314296000001, 'sysPermission', 'sys_permission_sup', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314299000001, 'sysPermission', 'sys_permission_sort', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314305000001, 'sysUsers', 'sys_users_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314310000001, 'sysUsers', 'sys_users_acc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314315000001, 'sysUsers', 'sys_users_pwd', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314319000001, 'sysUsers', 'sys_users_phone', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314324000001, 'sysUsers', 'sys_users_open_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314329000001, 'sysUsers', 'sys_users_status', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314333000001, 'sysUsers', 'sys_users_type', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314337000001, 'sysUsers', 'sys_users_reg_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314342000001, 'sysUsers', 'sys_users_start_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314346000001, 'sysUsers', 'sys_users_end_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314350000001, 'sysUsers', 'sys_users_price', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314354000001, 'sysUsers', 'sys_users_desc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314358000001, 'sysUsers', 'sys_users_sup', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314362000001, 'sysUsers', 'sys_users_mail', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314366000001, 'sysUsers', 'sys_users_group', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314372000001, 'sysUsersLoginLog', 'sys_users_login_log_id', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314378000001, 'sysUsersLoginLog', 'sys_users_login_log_ip', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314382000001, 'sysUsersLoginLog', 'sys_users_login_log_desc', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314386000001, 'sysUsersLoginLog', 'sys_users_login_log_time', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314390000001, 'sysUsersLoginLog', 'sys_users_login_log_uid', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314396000001, 'sysUsersLoginLog', 'sys_users_login_log_token', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748214314400000001, 'sysUsersLoginLog', 'sys_users_login_log_mac', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);
INSERT INTO `sys_group_column` VALUES (1748218875497000001, 'sysGroupColumn', 'sys_group_column_query', 1, 1, 1, 1, 0, NULL, NULL, 1, 1, NULL, 1);

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
INSERT INTO `sys_group_permission` VALUES (1, 1748214313941000001, 1, '2025-05-27 06:01:23');
INSERT INTO `sys_group_permission` VALUES (1748296904577000001, 1, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904583000001, 2, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904585000001, 3, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904587000001, 4, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904589000001, 5, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904591000001, 6, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904593000001, 7, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904594000001, 8, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904596000001, 1748214311232000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904597000001, 1748214311240000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904599000001, 1748214311244000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904600000001, 1748214311247000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904602000001, 1748214311251000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904604000001, 1748214311254000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904606000001, 1748214311257000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904608000001, 1748214311260000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904609000001, 1748214311263000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904611000001, 1748214311265000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904612000001, 1748214311268000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904614000001, 1748214311271000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904615000001, 1748214313833000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904616000001, 1748214313837000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904618000001, 1748214313839000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904621000001, 1748214313841000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904623000001, 1748214313843000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904625000001, 1748214313845000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904627000001, 1748214313846000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904628000001, 1748214313847000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904630000001, 1748214313849000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904631000001, 1748214313850000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904633000001, 1748214313852000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904635000001, 1748214313854000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904637000001, 1748214313857000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904637001001, 1748214313859000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904640000001, 1748214313860000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904642000001, 1748214313862000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904643000001, 1748214313864000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904645000001, 1748214313865000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904646000001, 1748214313867000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904648000001, 1748214313869000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904649000001, 1748214313872000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904651000001, 1748214313875000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904653000001, 1748214313877000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904655000001, 1748214313879000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904657000001, 1748214313881000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904659000001, 1748214313883000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904661000001, 1748214313885000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904663000001, 1748214313886000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904664000001, 1748214313888000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904666000001, 1748214313890000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904668000001, 1748214313892000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904670000001, 1748214313894000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904671000001, 1748214313896000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904673000001, 1748214313898000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904674000001, 1748214313899000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904676000001, 1748214313901000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904678000001, 1748214313902000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904679000001, 1748214313904000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904680000001, 1748214313906000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904682000001, 1748214313908000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904683000001, 1748214313911000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904685000001, 1748214313912000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904687000001, 1748214313914000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904689000001, 1748214313916000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904691000001, 1748214313917000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904692000001, 1748214313919000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904694000001, 1748214313922000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904696000001, 1748214313927000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904697000001, 1748214313929000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904699000001, 1748214313930000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904700000001, 1748214313932000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904702000001, 1748214313933000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904704000001, 1748214313935000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904706000001, 1748214313937000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904708000001, 1748214313939000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904711000001, 1748214313943000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904712000001, 1748214313946000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904714000001, 1748214313948000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904715000001, 1748214313949000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904717000001, 1748214313951000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904719000001, 1748214313953000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904721000001, 1748214313955000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904723000001, 1748214313958000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904725000001, 1748214313960000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904727000001, 1748214313962000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904728000001, 1748214313964000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904729000001, 1748214313965000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904731000001, 1748214313967000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904732000001, 1748214313968000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904734000001, 1748214313970000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904736000001, 1748214313971000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904737000001, 1748214313973000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904739000001, 1748214313976000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904741000001, 1748214313978000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904742000001, 1748214313980000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904743000001, 1748214313982000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904744000001, 1748214313983000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904746000001, 1748214313985000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904747000001, 1748214313986000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904749000001, 1748214313989000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904751000001, 1748214313991000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904753000001, 1748214313993000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904755000001, 1748214313995000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904757000001, 1748214313996000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904759000001, 1748214313998000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904760000001, 1748214314000000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904762000001, 1748214314001000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904763000001, 1748214314003000001, 1, '2025-05-27 06:01:44');
INSERT INTO `sys_group_permission` VALUES (1748296904765000001, 1748214314005000001, 1, '2025-05-27 06:01:44');

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
INSERT INTO `sys_group_table` VALUES (1748214314010000001, 'sysFile', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314034000001, 'sysGroup', 1, 1, 1, 1, 1, 1, 300);
INSERT INTO `sys_group_table` VALUES (1748214314055000001, 'sysGroupColumn', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314116000001, 'sysGroupPermission', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314139000001, 'sysGroupTable', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314182000001, 'sysLog', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314202000001, 'sysMapping', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314249000001, 'sysOption', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314271000001, 'sysPermission', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314301000001, 'sysUsers', 1, 1, 1, 1, 1, 1, 120);
INSERT INTO `sys_group_table` VALUES (1748214314368000001, 'sysUsersLoginLog', 1, 1, 1, 1, 1, 1, 120);

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
INSERT INTO `sys_mapping` VALUES (1747771167508000001, 'sysUsersLoginLog', '用户', 'sys_users_login_log_uid', 120, NULL, NULL, NULL, NULL, NULL, 185);
INSERT INTO `sys_mapping` VALUES (1747771167511000001, 'sysUsersLoginLog', '令牌', 'sys_users_login_log_token', 120, NULL, NULL, NULL, NULL, NULL, 186);
INSERT INTO `sys_mapping` VALUES (1747771167514000001, 'sysUsersLoginLog', '机器码', 'sys_users_login_log_mac', 120, NULL, NULL, NULL, NULL, NULL, 187);
INSERT INTO `sys_mapping` VALUES (1747955790613000001, 'roomUser', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 11);
INSERT INTO `sys_mapping` VALUES (1747955790622000001, 'roomUser', 'ID', 'room_user_id', 120, NULL, NULL, NULL, NULL, NULL, 102);
INSERT INTO `sys_mapping` VALUES (1747955790624000001, 'roomUser', '用户性别', 'room_user_sex', 120, 'key:def_sex', NULL, NULL, NULL, NULL, 103);
INSERT INTO `sys_mapping` VALUES (1747955790627000001, 'roomUser', '房间ID', 'room_user_rid', 120, NULL, NULL, NULL, NULL, NULL, 104);
INSERT INTO `sys_mapping` VALUES (1747955790629000001, 'roomUser', '房间类型', 'room_user_type', 120, 'key:def_sex', NULL, NULL, NULL, NULL, 105);
INSERT INTO `sys_mapping` VALUES (1747955790632000001, 'roomUser', '财富', 'room_user_price', 120, NULL, NULL, NULL, NULL, NULL, 106);
INSERT INTO `sys_mapping` VALUES (1747955790634000001, 'roomUser', '搜索ID', 'room_user_ou_id', 120, NULL, NULL, NULL, NULL, NULL, 107);
INSERT INTO `sys_mapping` VALUES (1747955790636000001, 'roomUser', '用户ID', 'room_user_ou_id2', 120, NULL, NULL, NULL, NULL, NULL, 108);
INSERT INTO `sys_mapping` VALUES (1747955790639000001, 'roomUser', '其他ID', 'room_user_ou_id3', 120, NULL, NULL, NULL, NULL, NULL, 109);
INSERT INTO `sys_mapping` VALUES (1747955790641000001, 'roomUser', '昵称', 'room_user_ou_name', 120, NULL, NULL, NULL, NULL, NULL, 110);
INSERT INTO `sys_mapping` VALUES (1747955790644000001, 'roomUser', '状态', 'room_user_state', 120, 'key:room_user_state', NULL, NULL, NULL, NULL, 111);
INSERT INTO `sys_mapping` VALUES (1747955790647000001, 'roomUser', '时间', 'room_user_time', 170, NULL, NULL, NULL, NULL, NULL, 112);
INSERT INTO `sys_mapping` VALUES (1747955790649000001, 'roomUser', '其他数据', 'room_user_data', 120, NULL, NULL, NULL, NULL, NULL, 113);
INSERT INTO `sys_mapping` VALUES (1747955790651000001, 'rooms', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 24);
INSERT INTO `sys_mapping` VALUES (1747955790653000001, 'rooms', 'ID', 'rooms_id', 120, NULL, NULL, NULL, NULL, NULL, 115);
INSERT INTO `sys_mapping` VALUES (1747955790655000001, 'rooms', '搜索ID', 'rooms_oid', 120, NULL, NULL, NULL, NULL, NULL, 116);
INSERT INTO `sys_mapping` VALUES (1747955790657000001, 'rooms', '跳转ID', 'rooms_oid2', 120, NULL, NULL, NULL, NULL, NULL, 117);
INSERT INTO `sys_mapping` VALUES (1747955790660000001, 'rooms', '房主ID', 'rooms_oid3', 120, NULL, NULL, NULL, NULL, NULL, 118);
INSERT INTO `sys_mapping` VALUES (1747955790662000001, 'rooms', '热度', 'rooms_heat', 120, NULL, NULL, NULL, NULL, NULL, 119);
INSERT INTO `sys_mapping` VALUES (1747955790664000001, 'rooms', '房间名称', 'rooms_name', 120, NULL, NULL, NULL, NULL, NULL, 120);
INSERT INTO `sys_mapping` VALUES (1747955790666000001, 'rooms', '性别', 'rooms_sex', 120, 'key:def_sex', NULL, NULL, NULL, NULL, 121);
INSERT INTO `sys_mapping` VALUES (1747955790668000001, 'rooms', '类型', 'rooms_type', 120, NULL, NULL, NULL, NULL, NULL, 122);
INSERT INTO `sys_mapping` VALUES (1747955790670000001, 'rooms', '标签', 'rooms_tag', 120, NULL, NULL, NULL, NULL, NULL, 123);
INSERT INTO `sys_mapping` VALUES (1747955790673000001, 'rooms', '备注', 'rooms_desc', 120, NULL, NULL, NULL, NULL, NULL, 124);
INSERT INTO `sys_mapping` VALUES (1747955790676000001, 'rooms', '时间', 'rooms_time', 120, NULL, NULL, NULL, NULL, NULL, 125);
INSERT INTO `sys_mapping` VALUES (1747955790679000001, 'rooms', '状态', 'rooms_state', 120, 'key:def_state', NULL, NULL, NULL, NULL, 126);
INSERT INTO `sys_mapping` VALUES (1747955790681000001, 'rooms', '读取', 'rooms_read', 120, NULL, NULL, NULL, NULL, NULL, 127);
INSERT INTO `sys_mapping` VALUES (1747956274722000001, 'liveApp', '页面信息', NULL, 130, NULL, NULL, NULL, NULL, NULL, 11);
INSERT INTO `sys_mapping` VALUES (1747956274728000001, 'liveApp', 'ID', 'live_app_id', 120, NULL, NULL, NULL, NULL, NULL, 102);
INSERT INTO `sys_mapping` VALUES (1747956274731000001, 'liveApp', '名称', 'live_app_name', 120, NULL, NULL, NULL, NULL, NULL, 103);
INSERT INTO `sys_mapping` VALUES (1747956274734000001, 'liveApp', '状态', 'live_app_state', 120, 'key:def_state', NULL, NULL, NULL, NULL, 104);
INSERT INTO `sys_mapping` VALUES (1747956274743000001, 'roomUser', '应用', 'room_user_aid', 120, NULL, NULL, NULL, NULL, NULL, 105);
INSERT INTO `sys_mapping` VALUES (1747956274754000001, 'rooms', '应用', 'rooms_aid', 120, NULL, NULL, NULL, NULL, NULL, 106);
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
INSERT INTO `sys_permission` VALUES (7, '选项管理', 'list.html?config=sysOption', 1, NULL, NULL, 0);
INSERT INTO `sys_permission` VALUES (8, '用户日志', 'list.html?config=sysUsersLoginLog', 1, '', 1, 0);
INSERT INTO `sys_permission` VALUES (1748214311232000001, 'frame', NULL, 1, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1748214311240000001, 'sys_file', 'list.html?config=sysFile&edit=sysFile', 1, NULL, 1748214311232000001, 0);
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
INSERT INTO `sys_permission` VALUES (1756293994748000001, '/system/v1.0.0/sysLogDao/find/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994758000001, '/system/v1.0.0/sysLogDao/query/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994765000001, '/system/v1.0.0/sysLogDao/delete/-DELETE', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994777000001, '/system/v1.0.0/sysLogDao/deleteAll/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994783000001, '/test/test1/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994788000001, '/system/v1.0.0/sysLogDao/update/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994790000001, '/system/v1.0.0/sysLogDao/list/-GET', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994810000001, '/system/v1.0.0/sysLogDao/save/-POST', NULL, 2, NULL, 0, 0);
INSERT INTO `sys_permission` VALUES (1756293994832000001, '/test/image/code/-GET', NULL, 2, NULL, 0, 0);

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
  PRIMARY KEY (`sys_users_login_log_id`) USING BTREE,
  INDEX `suoyin_sys_users_login_log_desc`(`sys_users_login_log_desc`) USING BTREE,
  INDEX `suoyin_sys_users_login_log_uid`(`sys_users_login_log_uid`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
