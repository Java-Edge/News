package com.sss.dao;

import com.sss.model.News;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shusheng Shi on 2017/5/7-5
 */
@Mapper
@Repository
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

//    添加新闻
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

//    分页:通过xml配置文件读取,于是去创建此xml文件吧!
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

}