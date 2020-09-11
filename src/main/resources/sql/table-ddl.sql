

CREATE TABLE `puff_trans_log` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '自增id',
  `puff_biz_id` varchar(64) NOT NULL COMMENT '业务幂等ID' ,
  `biz_type` TINYINT NOT NULL DEFAULT 0 COMMENT '业务类型' ,
  `params` varchar(1000) NOT NULL DEFAULT '' COMMENT '调用方法参数(json)',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '执行状态',
  `retry_count` INT NOT NULL DEFAULT 0 COMMENT '重试次数',
  `next_retry_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '下次重试时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  UNIQUE KEY `uk_biz_id` (`puff_biz_id`,`biz_type`),
  KEY `idx_next_time` (`next_retry_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1