-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 47.113.150.190    Database: teriteri
-- ------------------------------------------------------
-- Server version	5.7.43-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;







create database if not exists `touch`;

--
-- Table structure for table `user`
--
use touch;
DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `account` varchar(50) NOT NULL COMMENT '用户账号',
                        `password` varchar(255) NOT NULL COMMENT '用户密码',
                        `nickname` varchar(32) NOT NULL COMMENT '用户昵称',
                        `avatar` varchar(500) DEFAULT NULL COMMENT '用户头像url',
                        `background` varchar(500) DEFAULT NULL COMMENT '主页背景图url',
                        `gender` tinyint(4) NOT NULL DEFAULT '2' COMMENT '性别 0女 1男 2未知',
                        `description` varchar(100) DEFAULT NULL COMMENT '个性签名',
                        `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0正常 1封禁 2注销',
                        `role` tinyint(4) NOT NULL DEFAULT '0' COMMENT '角色类型 0普通用户 1管理员 2超级管理员',
                        `is_delete`     tinyint      default 0  not null comment '是否删除',
                        `create_time` datetime NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`uid`),
                        UNIQUE KEY `uid` (`uid`),
                        UNIQUE KEY `account` (`account`),
                        UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
                        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
                        `sender_uid` int(11) NOT NULL COMMENT '对象UID',
                        `receiver_uid` int(11) NOT NULL COMMENT '用户UID',
                        `type`         tinyint  default 0                 not null comment '0表示单聊，1表示群聊',
                        `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否移除聊天 0否 1是',
                        `unread` int(11) NOT NULL DEFAULT '0' COMMENT '消息未读数量',
                        `create_time` datetime NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '最近接收消息的时间或最近打开聊天窗口的时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `from_to` (`sender_uid`,`receiver_uid`),
                        UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='聊天表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chat_detailed`
--

DROP TABLE IF EXISTS `chat_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_detail` (
                               `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
                               `sender_uid` int(11) NOT NULL COMMENT '消息发送者',
                               `receiver_uid` int(11) NOT NULL COMMENT '消息接收者',
                               `content` varchar(500) NOT NULL COMMENT '消息内容',
                               `sender_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发送者是否删除',
                               `receiver_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '接受者是否删除',
                               `withdraw` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否撤回',
                               `is_delete`     tinyint      default 0  not null comment '是否删除',
                               `create_time` datetime NOT NULL default CURRENT_TIMESTAMP COMMENT '消息发送时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='聊天记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `friendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendship`
(
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
    `sender_id` int(11) NOT NULL COMMENT '请求发送者',
    `receiver_id` int(11) NOT NULL COMMENT '请求接收者',
    `status` tinyint default 0 NOT NULL COMMENT '好友关系的状态，比如请求中 0、已接受 1、已拒绝 2',
    `create_time` datetime NOT NULL default CURRENT_TIMESTAMP COMMENT '好友申请时间',
    `update_time` datetime NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '处理请求的时间',
    `is_delete`     tinyint      default 0  not null comment '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`sender_id`,`receiver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='好友申请表';