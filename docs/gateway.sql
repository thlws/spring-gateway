
#spring cloud gateway 原生路由配置
CREATE TABLE `route` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `route_id` varchar(128) NOT NULL COMMENT '路由编号,一般对应一个微服务',
   `route_uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务路径,内部微服务 lb://开头',
   `route_order` int(10) NOT NULL COMMENT '顺序',
   `filters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '过滤 JSON',
   `predicates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '断言 JSON',
   `metadata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'metadata JSON',
   `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0禁用; 1启用',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '路由配置' ROW_FORMAT = Dynamic;


#API鉴权
CREATE TABLE `api_auth` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `path` tinyint(4) NOT NULL DEFAULT 1 COMMENT '支持REST动态路径表达式',
   `auth` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0放行; 1鉴权',
   `auth_http_method` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鉴权作用HTTP METHOD,全部为*',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '网关限流' ROW_FORMAT = Dynamic;

#网关限流
CREATE TABLE `gateway_limit` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `limit_type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1主机限流; 2API限流; 3用户限流',
     `limit_value` tinyint(4) NOT NULL DEFAULT 1 COMMENT '限流阀值',
     `limit_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '限流内容,可为 主机,API,用户',
     `limit_http_method` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '限流HTTP METHOD,多个逗号隔开，全部为 *',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0禁用; 1启用',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '网关限流' ROW_FORMAT = Dynamic;


#路由配置
CREATE TABLE `gateway_route` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `route_id` varchar(128) NOT NULL COMMENT '路由编号,一般对应一个微服务',
     `route_uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务路径,内部微服务 lb://开头',
     `route_type` tinyint(4) NOT NULL COMMENT '1内部服务; 2外部地址',
     `predicate_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '匹配路径,eg:/api/user/**',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0禁用; 1启用',
     `strip_type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '去除前缀类型 -1_N/A; 0_false 1_true',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '路由配置' ROW_FORMAT = Dynamic;
