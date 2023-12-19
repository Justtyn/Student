package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper {

    /**
     * 登陆
     */
    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);
}
