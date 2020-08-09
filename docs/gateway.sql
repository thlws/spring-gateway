/*
 Navicat Premium Data Transfer

 Source Server         : docker_localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3300
 Source Schema         : gateway

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 09/08/2020 17:02:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_auth
-- ----------------------------
DROP TABLE IF EXISTS `api_auth`;
CREATE TABLE `api_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `path` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '支持REST动态路径表达式',
  `auth` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1放行; 2鉴权',
  `auth_http_method` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鉴权作用HTTP METHOD,全部为*',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_path` (`path`) COMMENT 'API路径唯一约束'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网关限流';

-- ----------------------------
-- Table structure for gateway_limit
-- ----------------------------
DROP TABLE IF EXISTS `gateway_limit`;
CREATE TABLE `gateway_limit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `limit_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1主机限流; 2API限流; 3用户限流',
  `limit_value` tinyint(4) NOT NULL DEFAULT '1' COMMENT '限流阀值',
  `limit_content` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '限流内容,可为 主机,API,用户',
  `limit_http_method` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '限流HTTP METHOD,多个逗号隔开，全部为 *',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0禁用; 1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_limit_content` (`limit_content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网关限流';

-- ----------------------------
-- Table structure for gateway_route
-- ----------------------------
DROP TABLE IF EXISTS `gateway_route`;
CREATE TABLE `gateway_route` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `route_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '路由编号,一般对应一个微服务',
  `route_uri` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务路径,内部微服务 lb://开头',
  `route_type` tinyint(4) NOT NULL COMMENT '1内部服务; 2外部地址',
  `predicate_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '匹配路径,eg:/api/user/**',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0禁用; 1启用',
  `strip_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '去除前缀类型 -1_N/A; 0_false 1_true',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_route_id` (`route_id`) COMMENT '路由编号唯一约束',
  UNIQUE KEY `idx_predicate_path` (`predicate_path`) COMMENT '断言路径唯一约束'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='路由配置';

SET FOREIGN_KEY_CHECKS = 1;
