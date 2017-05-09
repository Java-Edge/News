package com.sss.dao;

import com.sss.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by Shusheng Shi on 2017/5/7-1
 */
@Mapper
@Component
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, name, password, salt, head_url";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    User selectById(int id);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

    //判断用户名是否已经被注册
    @Insert({"insert into ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);
}