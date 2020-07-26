
#暴露服务
CREATE TABLE `export_svc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `service_name` varchar(512)  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务名称',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务说明',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0禁用; 1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '暴露服务' ROW_FORMAT = Dynamic;

#路由配置
CREATE TABLE `route` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `route_id` varchar(128) NOT NULL COMMENT '路由编号,一般对应一个微服务',
   `route_uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务路径,内部微服务 lb://开头',
   `route_order` int(10) NOT NULL COMMENT '顺序',
   `filters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '过滤 JSON',
   `predicates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '断言 JSON',
   `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0禁用; 1启用',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '路由配置' ROW_FORMAT = Dynamic;


#暴露API
CREATE TABLE `export_api` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `service_name` varchar(20) NOT NULL COMMENT '微服务名称',
   `service_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务路径',
   `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0禁用; 1启用',
   `auth` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0放行; 1鉴权',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '暴露API' ROW_FORMAT = Dynamic;


