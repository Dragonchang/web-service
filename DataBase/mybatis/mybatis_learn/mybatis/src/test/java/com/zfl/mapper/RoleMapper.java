package com.zfl.mapper;

import com.zfl.po.Role;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper {
    public Role getRole(Long id);
    public Role findRole(String roleName);
    public int deleteRole(Long id);
    public int insertRole(Role role);
}
