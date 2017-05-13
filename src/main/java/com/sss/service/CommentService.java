package com.sss.service;

import com.sss.dao.CommentDAO;
import com.sss.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Shusheng Shi on 2017/5/13 12:50.
 */
@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    /**
     * 删除评论
     * @param entityId
     * @param entityType
     */
    public void deleteComment(int entityId, int entityType) {
        commentDAO.updateStatus(entityId, entityType, 1);
    }
}
