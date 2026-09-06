DROP TABLE IF EXISTS `chat_model_list`;

CREATE TABLE `chat_model_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天模型列表ID',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置URL',
  `api_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'apiKey',
  `model_version` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型版本',
  `model_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型名称',
  `num_gpu` int(5) DEFAULT NULL COMMENT 'GPU 数量',
  `temperature` float(10,2) DEFAULT NULL COMMENT '随机性',
  `top_k` int(50) DEFAULT NULL COMMENT '限制模型单词',
  `top_p` float(10,2) DEFAULT NULL COMMENT '核采样',
  `repeat_penalty` float(10,2) DEFAULT NULL COMMENT '防止重复',
  `presence_penalty` float(10,2) DEFAULT NULL COMMENT '惩罚已出现的词',
  `frequency_penalty` float(10,2) DEFAULT NULL COMMENT '惩罚频繁出现的词',
  `num_predict` int(5) DEFAULT NULL COMMENT '最大token 数',
  `num_thread` int(5) DEFAULT NULL COMMENT 'CPU线程数',
  `low_vram` tinyint(1) DEFAULT NULL COMMENT '是否启用低显存模式',
  `is_chose` tinyint(1) DEFAULT NULL COMMENT '是否选择',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `vector_database_list`;

CREATE TABLE `vector_database_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '向量数据库列表ID',
  `alias_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库别名',
  `host` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库地址',
  `port` int(10) DEFAULT NULL COMMENT '数据库端口',
  `database` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库名',
  `table` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库表名称',
  `user` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库用户名',
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库密码',
  `dimension` int(10) DEFAULT NULL COMMENT '向量纬度',
  `is_chose` tinyint(1) DEFAULT NULL COMMENT '是否选择',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session`
(
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天会话ID',
  `user_id`     bigint(20) NOT NULL COMMENT '用户ID',
  `title`       varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '聊天会话名',
  `memory`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '聊天会话记忆',
  `create_time` datetime                                DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='聊天会话表';

DROP TABLE IF EXISTS `chat_session_detail`;
CREATE TABLE `chat_session_detail`
(
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天会话详细ID',
  `session_id`  bigint(20) NOT NULL COMMENT '聊天会话ID',
  `role`        varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话角色 user:用户 ai:AI',
  `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '聊天内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY           `idx_session_id` (`session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='聊天会话详情表';