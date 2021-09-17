package com.adobe.prj.auth;

import static com.adobe.prj.auth.UserPermission.COURSE_READ;
import static com.adobe.prj.auth.UserPermission.COURSE_WRITE;
import static com.adobe.prj.auth.UserPermission.STUDENT_READ;
import static com.adobe.prj.auth.UserPermission.STUDENT_WRITE;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
	STUDENT(Arrays.asList()),
    ADMIN(Arrays.asList(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    ADMINTRAINEE(Arrays.asList(COURSE_READ, STUDENT_READ));

    private final List<UserPermission> permissions;

    UserRole(List<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public List<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
