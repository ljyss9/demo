package com.ljy.Service;

import com.ljy.DAO.CommentDAO;
import com.ljy.Model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ljy on 2017/2/16.
 */
@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);



    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }
}
