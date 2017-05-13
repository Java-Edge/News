package com.sss.dao;

/**
 * Created by Shusheng Shi on 2017/5/13 12:35.
 */

import com.sss.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用来读取数据
 */
@Mapper
@Repository
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 添加评论
     * @param comment 评论
     * @return 状态,0表失败,非0表成功添加;成功之后,comment中id将被重新赋值
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    /**
     * 查得所有评论
     * @param entityId 资讯
     * @param entityType
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     * 某实体(在此项目即为资讯news)回复总数
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     * 更新status,达到删除评论效果
     * @param entityId
     * @param entityType
     * @param status
     */
    @Update({"update ", TABLE_NAME, " set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

}
