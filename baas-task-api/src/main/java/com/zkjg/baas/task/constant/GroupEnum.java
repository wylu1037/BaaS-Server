package com.zkjg.baas.task.constant;

/**
 * @author wylu
 * @date 2022/4/16 上午 11:11
 */
public enum GroupEnum {
    HELLO("TEST");

    GroupEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
