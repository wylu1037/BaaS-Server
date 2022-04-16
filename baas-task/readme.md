# 定时任务管理
## 1.技术栈
基于quartz实现

## 2.概念
https://nkcoder.github.io/posts/quartz/quartz-tutorial-2-job-and-trigger/

+ Scheduler – 与scheduler交互的主要API；
+ Job – 你通过scheduler执行任务，你的任务类需要实现的接口；
+ JobDetail – 定义Job的实例；
+ Trigger – 触发Job的执行；
+ JobBuilder – 定义和创建JobDetail实例的接口;
+ TriggerBuilder – 定义和创建Trigger实例的接口；