package com.sss.service;

import com.sss.dao.NewsDAO;
import com.sss.model.News;
import com.sss.model.User;
import com.sss.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Shusheng Shi on 2017/5/8.-2
 * 读取资讯
 */
@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    /**
     * 添加咨询
     * @param news
     * @return
     */
    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

//  获取最新咨询
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    /**
     * 上传文件保存到服务器
     *
     * @param file
     * @return :本地保存地址
     * @throws IOException
     */
    public String saveImage(MultipartFile file) throws IOException {
//      直接定位文件名的.分隔符(如:xxx.yyy.jpg),若不存在则非图片文件
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
//      文件扩展名
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }

//      图片保存
//      用户文件名混乱,为方便管理,随机生成文件名并规范化
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
//      二进制流copy到目标地址
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;

    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

}
