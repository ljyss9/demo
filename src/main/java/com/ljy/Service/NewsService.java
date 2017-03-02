package com.ljy.Service;

import com.ljy.DAO.NewsDAO;
import com.ljy.Model.News;
import com.ljy.util.DemoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ljy on 2017/2/13.
 */
@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatesNews(int userId,int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId , offset, limit);
    }

    public String saveImage(MultipartFile file){
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!DemoUtil.isFileAllowed(fileExt)) {
            return null;
        }

        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "."+fileExt;
        try {
            Files.copy(file.getInputStream(), new File(DemoUtil.IMAGE_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return DemoUtil.DEMO_DOMAIN + "image?name=" +fileName;
        }
        catch (Exception e){
            return null;
        }
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }
}
