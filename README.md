### 项目简介
模拟轻聊版 QQ，实现局域网下多用户之间的通信，实现用户注册、登陆以及用户间 的私聊和群聊服务
### 项目来源
想要实现一个属于自己的聊天工具，虽然现在只是实现了一小步，相信在自己的不断完善下会越来越好。
### 项目意义

 - 对JavaSE基础知识，多线程，JDBC和数据库的练习
 - 满足自己的小兴趣

### 项目技术

 - JavaSE
 - Java多线程、线程池
 - JDBC编程
 - MySQL数据库
 - Socket编程

### 项目描述

 - 项目按照功能进行进行实施
 - 使用JDBC编程实现对数据库的操作
 - 使用 IDEA 中的 GUI 功能编写界面，实现界面
 - 通过 Socket 编程实现客户端和服务器连接，使用 IO 技术实现数据传输
 - 使用多线程技术实现多个客户端并发通信

### 实现过程
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190823232954298.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5MDMyMzEw,size_16,color_FFFFFF,t_70)
#### 模型

 - 定义服务器和客户端传递信息载体类：(type、content、to)
 - 准备数据源配置文件、端口号和IP配置文件
 - 准备json字符串和object对象之间的相互转换方法
 - 用户基本信息类(id、username、password、brief)

#### 数据库

 - 使用JDBC进行Java和数据库之间的交互
 - 使用DruidDataSource(数据源)对象获取数据库连接
 - 用户信息 --> 数据库中的记录
 - 登陆用户和数据库中记录信息匹配

#### 注册和登陆

 - 使用IDEA的GUI功能编写登陆界面和注册界面
 - 将用户注册信息存入数据库
 - 用户登陆信息和数据库记录进行匹配
 - 使用多线程+Socket编程建立当前用户和服务器之间的连接
 - 将当前所有在线用户存储到服务器端
#### 私聊
 - 使用IDEA的GUI功能编写用户界面和私聊界面
 - 使用点击事件方法触发当前用户和指定用户之间私聊事件
 - 使用IO技术进行数据传输

#### 群聊

 - 使用IDEA的GUI功能编写创建群聊和群聊界面
 - 使用Btn键触发创建群聊，使用JcheckBox展示当前所有可选成员，使用isSelect()判断选中的群成员
 - 使用IO技术进行数据传输