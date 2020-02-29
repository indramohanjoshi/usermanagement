package com.usermanagement.common;

public enum RoleEnum {
    ADMIN,
    USER;

    public String getRoleName() {
        return "ROLE_" + this;
    }
}
