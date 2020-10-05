package top.naccl.service;

import top.naccl.po.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
	Comment getComment(Long id);

	Page<Comment> listComment(Boolean published, Pageable pageable);

	List<Comment> listCommentByBlogId(Long blogId);

	List<Comment> listCommentByAboutMe(Boolean aboutMe);

	Comment saveComment(Comment comment);

	Comment updateComment(Long id, Comment comment);

	void trashComment(Long id);

	void publishComment(Long id);

	void deleteComment(Long id);
}
