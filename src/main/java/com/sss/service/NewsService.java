package com.sss.service;

import com.sss.dao.NewsDAO;
import com.sss.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Shusheng Shi on 2017/5/8.-2
 * 读取资讯
 */
@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    //    获取最新咨询
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }
}
