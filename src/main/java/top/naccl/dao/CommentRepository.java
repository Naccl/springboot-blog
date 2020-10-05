package top.naccl.dao;

import top.naccl.po.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByBlogIdAndParentCommentNull(Long BlogId, Sort sort);

	List<Comment> findByAboutMeAndParentCommentNull(Boolean aboutMe, Sort sort);

	@Query("select c from t_comment c where c.published = ?1")
	Page<Comment> findByPublished(Boolean published, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update t_comment c set c.published = false where c.id = ?1")
	int trashComment(Long id);

	@Transactional
	@Modifying
	@Query("update t_comment c set c.published = true where c.id = ?1")
	int publishComment(Long id);
}
