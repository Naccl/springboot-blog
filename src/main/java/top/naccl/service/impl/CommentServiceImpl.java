package top.naccl.service.impl;

import top.naccl.NotFoundException;
import top.naccl.dao.CommentRepository;
import top.naccl.po.Comment;
import top.naccl.service.CommentService;
import top.naccl.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 评论操作实现类
 * @Author: Naccl
 * @Date: 2020-04-30
 */

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public Comment getComment(Long id) {
		return commentRepository.getOne(id);
	}

	@Override
	public Page<Comment> listComment(Boolean published, Pageable pageable) {
		return commentRepository.findByPublished(published, pageable);
	}

	@Override
	public List<Comment> listCommentByBlogId(Long blogId) {
		Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
		return commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
	}

	@Override
	public List<Comment> listCommentByAboutMe(Boolean aboutMe) {
		Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
		return commentRepository.findByAboutMeAndParentCommentNull(aboutMe, sort);
	}

	@Transactional
	@Override
	public Comment saveComment(Comment comment) {
		Long parentCommentId = comment.getParentComment().getId();
		if (parentCommentId != -1) {
			comment.setParentComment(commentRepository.getOne(parentCommentId));
		} else {
			comment.setParentComment(null);
		}
		comment.setCreateTime(new Date());
		return commentRepository.save(comment);
	}

	@Transactional
	@Override
	public Comment updateComment(Long id, Comment comment) {
		Comment c = commentRepository.findById(id).orElse(null);
		if (c == null) {
			throw new NotFoundException("该评论不存在");
		}
		//boolean类型的 published adminComment aboutMe 不会为空，要手动忽略掉，否则在提交修改后 true 会变 false
		BeanUtils.copyProperties(comment, c, MyBeanUtils.getNullAndIgnorePropertyNames(comment, new String[]{"published", "adminComment", "aboutMe"}));
		return commentRepository.save(c);
	}

	@Transactional
	@Override
	public void trashComment(Long id) {
		commentRepository.trashComment(id);
	}

	@Transactional
	@Override
	public void publishComment(Long id) {
		commentRepository.publishComment(id);
	}

	@Transactional
	@Override
	public void deleteComment(Long id) {
		Comment comment = commentRepository.getOne(id);
		if (comment.getReplyComments().size() != 0) {
			for (Comment c : comment.getReplyComments()) {
				deleteComment(c.getId());
			}
		}
		commentRepository.deleteById(id);
	}
}
