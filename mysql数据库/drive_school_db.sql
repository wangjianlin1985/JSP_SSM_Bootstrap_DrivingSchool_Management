/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : drive_school_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-06-27 17:01:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_answer`
-- ----------------------------
DROP TABLE IF EXISTS `t_answer`;
CREATE TABLE `t_answer` (
  `answerId` int(11) NOT NULL auto_increment COMMENT '答案id',
  `paperObj` int(11) NOT NULL COMMENT '答题试卷',
  `userObj` varchar(30) NOT NULL COMMENT '答题学员',
  `answerFile` varchar(60) NOT NULL COMMENT '作答文件',
  `addTime` varchar(20) default NULL COMMENT '提交时间',
  `score` varchar(20) default NULL COMMENT '学员成绩',
  `evaluate` varchar(20) NOT NULL COMMENT '评语',
  PRIMARY KEY  (`answerId`),
  KEY `paperObj` (`paperObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_answer_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`),
  CONSTRAINT `t_answer_ibfk_1` FOREIGN KEY (`paperObj`) REFERENCES `t_paper` (`paperId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_answer
-- ----------------------------
INSERT INTO `t_answer` VALUES ('1', '1', 'user1', 'upload/3fffd88e-1759-42be-aa5b-dfde138d1ee9.doc', '2018-01-14 22:16:11', '65', '不行');
INSERT INTO `t_answer` VALUES ('2', '2', 'user1', 'upload/bc88638a-32f7-47ff-92c7-93a8f8248001.doc', '2018-06-27 16:51:04', '待审阅', '--');

-- ----------------------------
-- Table structure for `t_car`
-- ----------------------------
DROP TABLE IF EXISTS `t_car`;
CREATE TABLE `t_car` (
  `carNo` varchar(20) NOT NULL COMMENT 'carNo',
  `carName` varchar(20) NOT NULL COMMENT '车辆名称',
  `carPhoto` varchar(60) NOT NULL COMMENT '车辆照片',
  `shitWay` varchar(20) NOT NULL COMMENT '换挡方式',
  `onCardDate` varchar(20) default NULL COMMENT '上牌日期',
  `carDesc` varchar(5000) NOT NULL COMMENT '车辆介绍',
  `carState` int(11) NOT NULL COMMENT '车辆状态',
  `carPlace` varchar(30) NOT NULL COMMENT '训练场地',
  PRIMARY KEY  (`carNo`),
  KEY `carState` (`carState`),
  CONSTRAINT `t_car_ibfk_1` FOREIGN KEY (`carState`) REFERENCES `t_carstate` (`stateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_car
-- ----------------------------
INSERT INTO `t_car` VALUES ('川A-2934', '奔驰S-230', 'upload/c509efc5-122a-4cdb-8e35-90ed9a814458.jpg', '自动挡', '2018-01-02', '<p>好车</p>', '1', '三环内侧川陕立交桥');

-- ----------------------------
-- Table structure for `t_carstate`
-- ----------------------------
DROP TABLE IF EXISTS `t_carstate`;
CREATE TABLE `t_carstate` (
  `stateId` int(11) NOT NULL auto_increment COMMENT '状态id',
  `stateName` varchar(20) NOT NULL COMMENT '状态名称',
  PRIMARY KEY  (`stateId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_carstate
-- ----------------------------
INSERT INTO `t_carstate` VALUES ('1', '空闲中');
INSERT INTO `t_carstate` VALUES ('2', '已占用');

-- ----------------------------
-- Table structure for `t_coach`
-- ----------------------------
DROP TABLE IF EXISTS `t_coach`;
CREATE TABLE `t_coach` (
  `coachId` int(11) NOT NULL auto_increment COMMENT '教练id',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `sex` varchar(4) NOT NULL COMMENT '性别',
  `coachPhoto` varchar(60) NOT NULL COMMENT '教练照片',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `coachDesc` varchar(5000) NOT NULL COMMENT '教练介绍',
  PRIMARY KEY  (`coachId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_coach
-- ----------------------------
INSERT INTO `t_coach` VALUES ('1', '王强', '男', 'upload/c10a8931-bf4b-414b-bd6c-dfe8ca4cb3d9.jpg', '2018-01-03', '13980830423', 'wangq@163.com', '<p>技术好的教练</p>');

-- ----------------------------
-- Table structure for `t_leaveword`
-- ----------------------------
DROP TABLE IF EXISTS `t_leaveword`;
CREATE TABLE `t_leaveword` (
  `leaveWordId` int(11) NOT NULL auto_increment COMMENT '留言id',
  `leaveTitle` varchar(80) NOT NULL COMMENT '留言标题',
  `leaveContent` varchar(2000) NOT NULL COMMENT '留言内容',
  `userObj` varchar(30) NOT NULL COMMENT '留言人',
  `leaveTime` varchar(20) default NULL COMMENT '留言时间',
  `replyContent` varchar(1000) default NULL COMMENT '管理回复',
  `replyTime` varchar(20) default NULL COMMENT '回复时间',
  PRIMARY KEY  (`leaveWordId`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_leaveword_ibfk_1` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leaveword
-- ----------------------------
INSERT INTO `t_leaveword` VALUES ('1', '我想考C1小车驾照', '现在报名的话，多久可以拿到驾照？', 'user1', '2018-01-14 22:16:38', '只要你认真学习，一般3个月内', '2018-06-27 16:28:21');
INSERT INTO `t_leaveword` VALUES ('2', '科目一和科目2哪个好学？', '管理你好，你觉得科目几容易过？', 'user1', '2018-06-27 16:52:27', '--', '--');

-- ----------------------------
-- Table structure for `t_news`
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `newsId` int(11) NOT NULL auto_increment COMMENT '记录id',
  `newsType` varchar(20) NOT NULL COMMENT '信息类别',
  `title` varchar(80) NOT NULL COMMENT '信息标题',
  `content` varchar(5000) NOT NULL COMMENT '内容',
  `publishDate` varchar(20) default NULL COMMENT '发布时间',
  PRIMARY KEY  (`newsId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_news
-- ----------------------------
INSERT INTO `t_news` VALUES ('1', '111', '学车常识', '<p>222</p>', '2018-01-14 22:17:41');
INSERT INTO `t_news` VALUES ('2', 'aaa', '网站新闻', '<p>bbb</p>', '2018-01-14 22:18:00');

-- ----------------------------
-- Table structure for `t_paper`
-- ----------------------------
DROP TABLE IF EXISTS `t_paper`;
CREATE TABLE `t_paper` (
  `paperId` int(11) NOT NULL auto_increment COMMENT '试卷id',
  `subjectObj` int(11) NOT NULL COMMENT '考试科目',
  `examName` varchar(20) NOT NULL COMMENT '考试名称',
  `paperContent` varchar(8000) NOT NULL COMMENT '考题内容',
  `addDate` varchar(20) default NULL COMMENT '添加日期',
  PRIMARY KEY  (`paperId`),
  KEY `subjectObj` (`subjectObj`),
  CONSTRAINT `t_paper_ibfk_1` FOREIGN KEY (`subjectObj`) REFERENCES `t_subject` (`subjectId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_paper
-- ----------------------------
INSERT INTO `t_paper` VALUES ('1', '1', '2018小车科目一考试', '<p>1、驾驶机动车需要在路边停车时怎样选择停车地点？</p><p>&nbsp;A：在人行道上停放</p><p>&nbsp;B：在路边随意停放</p><p>&nbsp;C：在停车泊位内停放</p><p>&nbsp;D：靠左侧路边逆向停放</p><p>2、驾驶机动车在夜间超车时怎样使用灯光？</p><p>&nbsp;A：变换远、近光灯</p><p>&nbsp;B：开启雾灯</p><p>&nbsp;C：开启远光灯</p><p>&nbsp;D：关闭前大灯</p><p><br/></p>', '2018-01-14');
INSERT INTO `t_paper` VALUES ('2', '2', '2019小车科目四考试', '<p><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">1 驾驶机动车行经此路段多少米内不得停车？<br/><img src=\"/JavaWebProject/upload/20180627/1530087856025083800.jpg\" title=\"1530087856025083800.jpg\" alt=\"1.jpg\" width=\"285\" height=\"160\"/><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">A、30米</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">B、50米</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">C、80米</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">D、100米<br/><br/>2&nbsp;<span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">雾天驾驶机动车跟车行驶，以下做法错误的是什么？<br/></span><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">A、加大两车间的距离</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">B、时刻注意前车刹车灯的变化</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">C、降低行车速度</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">D、鸣喇叭提醒前车提高车速，避免后车追尾<br/><br/>3<span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">驾驶机动车遇到这种行人应该注意什么？</span><br/><img src=\"/JavaWebProject/upload/20180627/1530087928995031811.jpg\" title=\"1530087928995031811.jpg\" alt=\"2.jpg\" width=\"442\" height=\"226\"/><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">A、在路中心行驶</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">B、持续鸣喇叭</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">C、加速超越</span><br/><span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">D、注意观察动态<br/><br/>4&nbsp;<span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">怎样安全通过这种较窄的弯道？<br/><img src=\"/JavaWebProject/upload/20180627/1530087992863063031.jpg\" title=\"1530087992863063031.jpg\" alt=\"3.jpg\" width=\"490\" height=\"240\"/><br/>A、沿道路右侧行驶<br/>B、挂低速档减速通过<br/>C、沿道路左侧行驶<br/>D、挂高速档加速通过<br/><br/>5&nbsp;<span style=\"font-family: 宋体, 微软雅黑; font-size: 20px; font-weight: bold; background-color: rgb(242, 242, 242);\">驾驶汽车不系安全带在遇紧急制动或发生碰撞时可能会发生哪些危险？<br/>A、撞击风窗玻璃<br/>B、减少人员伤亡<br/>C、被甩出车外<br/>D、造成胸部损伤</span></span></span></span></span></span></p>', '2018-06-27');

-- ----------------------------
-- Table structure for `t_subject`
-- ----------------------------
DROP TABLE IF EXISTS `t_subject`;
CREATE TABLE `t_subject` (
  `subjectId` int(11) NOT NULL auto_increment COMMENT '科目id',
  `subjectName` varchar(20) NOT NULL COMMENT '科目名称',
  `carType` varchar(20) NOT NULL COMMENT '适用车辆',
  `driveLevel` varchar(20) NOT NULL COMMENT '驾照级别',
  PRIMARY KEY  (`subjectId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_subject
-- ----------------------------
INSERT INTO `t_subject` VALUES ('1', '小车科目一', '小轿车', 'C1,C2');
INSERT INTO `t_subject` VALUES ('2', '科目四', '小轿车', 'C1,C2');

-- ----------------------------
-- Table structure for `t_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo` (
  `user_name` varchar(30) NOT NULL COMMENT 'user_name',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '用户照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `regTime` varchar(20) default NULL COMMENT '注册时间',
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('user1', '123', '双鱼林', '男', '2018-01-02', 'upload/NoImage.jpg', '13589834234', 'dashen@163.com', '四川成都红星路', '2018-01-14 22:04:43');
