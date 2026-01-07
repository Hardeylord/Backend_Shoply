package com.shoply.Products.rolePermission;

import java.util.Set;

public enum ROLE {
    ADMIN(Set.of(
            Permission.CREATE,
            Permission.UPDATE,
            Permission.DELETE,
            Permission.READ
    )),
    USER(Set.of(
            Permission.READ
    )),
    EDITOR(Set.of(
            Permission.CREATE,
            Permission.UPDATE,
            Permission.READ));

    private Set<Permission> permissions;

    ROLE(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
