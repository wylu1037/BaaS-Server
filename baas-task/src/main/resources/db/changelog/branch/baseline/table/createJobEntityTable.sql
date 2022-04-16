create table job_entity
(
    id         int(11) auto_increment comment '主键',
    name       varchar(50)                                                      null comment '定时任务名称',
    chainId    int                                                              null comment '链id',
    nodeId     int                                                              null comment '节点id',
    cron       varchar(100)                                                     null comment '执行的cron表达式',
    params     text                                                             null comment 'JobDetail执行的业务参数',
    enableFlag tinyint(1) default 1 comment '可用标记：0-不可用、1-可用',
    remark     text                                                             null comment '备注',
    createTime datetime   default CURRENT_TIMESTAMP                             null comment '创建时间',
    updateTime datetime   default current_timestamp on update current_timestamp null comment '更新时间',
    deleteFlag tinyint(1) default 0                                             null comment '删除标记， 0-未删除、1-已删除',
    primary key (id)
) engine = InnoDB
  character set = utf8
  collate = utf8_general_ci comment '定时任务实体信息'
  row_format = dynamic;