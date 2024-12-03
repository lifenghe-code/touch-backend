DROP TABLE IF EXISTS `chat_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_detail` (
                               `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
                               `user_id` int(11) NOT NULL COMMENT '消息发送者',
                               `another_id` int(11) NOT NULL COMMENT '消息接收者',
                               `content` varchar(500) NOT NULL COMMENT '消息内容',
                               `user_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发送者是否删除',
                               `another_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '接受者是否删除',
                               `withdraw` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否撤回',
                               `time` datetime NOT NULL COMMENT '消息发送时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='聊天记录表';

