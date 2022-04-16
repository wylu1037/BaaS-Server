--liquibase formatted sql

--changeset wylu:202204152312022 job_entity表增加字段groupName
alter table job_entity
    add column groupName varchar(50) null comment '分组名称' after name;

--changeset wylu:20220416121103732 job_entity表增加字段operatorId
alter table job_entity
    add column operatorId int null comment '操作人员id' after groupName;