# 定时任务管理

## 1.技术栈

基于quartz实现

## 2.概念

http://www.quartz-scheduler.org/documentation/quartz-2.3.0/cookbook/

+ Scheduler – 与scheduler交互的主要API；
+ Job – 你通过scheduler执行任务，你的任务类需要实现的接口；
+ JobDetail – 定义Job的实例；
+ Trigger – 触发Job的执行；
+ JobBuilder – 定义和创建JobDetail实例的接口;
+ TriggerBuilder – 定义和创建Trigger实例的接口；

### 2.1 Scheduler

调度器Scheduler就相当于一个容器，装载着任务和触发器。Trigger 和 JobDetail 可以注册到
Scheduler 中， 两者在 Scheduler 中拥有各自的组及名称， 组及名称是 Scheduler
查找定位容器中某一对象的依据， Trigger 的组及名称必须唯一，JobDetail 的组和名称也必须唯一（但可以
和 Trigger 的组和名称相同，因为它们是不同类型 的）。Scheduler 定义了多个接口方法，
允许外部通过组及名称访问和控制容器中 Trigger 和 JobDetail。

Scheduler 可以将 Trigger 绑定到某一 JobDetail 中， 这样当 Trigger 触发时， 对应的 Job 就被执行。一个 Job 可以对应多个 Trigger， 但一个 Trigger 只能对应一个
Job。可以通过 SchedulerFactory 创建一个 Scheduler 实例。Scheduler 拥有一个 SchedulerContext，保存着 Scheduler 上下文信息，Job 和 Trigger 都可以访问
SchedulerContext 内的信息。SchedulerContext 内部通过一个 Map，以键值对的方式维护这些上下文数据，SchedulerContext 为保存和获取数据提供了个 put() 和 getXxx()
的方法。可以通过 Scheduler.getContext() 获取对应的 SchedulerContext 实例。

// Unschedule a particular trigger from the job (a job may have more than one trigger)

scheduler.unscheduleJob(triggerKey("trigger1", "group1"));

// Schedule the job with the trigger

scheduler.deleteJob(jobKey("job1", "group1"));