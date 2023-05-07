# 毕业设计课题管理系统
本项目用于数据库实验

部分视图创建SQL:
#### ```select_user```
```sql
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`%` SQL SECURITY DEFINER VIEW `graduation_project_topic_management_system`.`Untitled` AS select `user`.`id` AS `id`,`user`.`name` AS `name`,`user`.`status` AS `status`,`user`.`permission` AS `permission`,`user`.`major_id` AS `major_id`,`user`.`password` AS `password`,`user`.`age` AS `age`,`user`.`sex` AS `sex`,`user`.`phone` AS `phone`,`user`.`username` AS `username` from `user`;
```

#### ```select_topic```
```sql
CREATE VIEW `graduation_project_topic_management_system`.`Untitled` AS SELECT
	topic.*
FROM
	topic
```

#### ```select_college```
```sql
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`%` SQL SECURITY DEFINER VIEW `graduation_project_topic_management_system`.`Untitled` AS select `college`.`id` AS `id`,`college`.`name` AS `name` from `college`;
```

#### ```select_major```
```sql
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`%` SQL SECURITY DEFINER VIEW `graduation_project_topic_management_system`.`Untitled` AS select `major`.`id` AS `id`,`major`.`name` AS `name`,`major`.`college_id` AS `college_id` from `major`;
```

## 前端项目地址[内网]:
http://192.168.12.12:3000/abbhb/TopicManagementSystemFronted
http://10.15.245.153:3000/abbhb/TopicManagementSystemFronted