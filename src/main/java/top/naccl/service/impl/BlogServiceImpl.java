package top.naccl.service.impl;

import top.naccl.NotFoundException;
import top.naccl.dao.BlogRepository;
import top.naccl.po.Blog;
import top.naccl.po.Type;
import top.naccl.service.BlogService;
import top.naccl.util.markdown.MarkdownUtils;
import top.naccl.util.MyBeanUtils;
import top.naccl.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Description: 博客操作实现类
 * @Author: Naccl
 * @Date: 2020-04-26
 */

@Service
public class BlogServiceImpl implements BlogService {
	@Autowired
	private BlogRepository blogRepository;

	@Override
	public Blog getBlog(Long id) {
		return blogRepository.getOne(id);
	}

	@Transactional
	@Override
	public Blog getAndConvert(Long id) {
		Blog blog = blogRepository.findById(id).orElse(null);
		if (blog == null) {
			System.err.println("该博客不存在");
			throw new NotFoundException("该博客不存在");
		}
		Blog b = new Blog();
		BeanUtils.copyProperties(blog, b);
		String content = b.getContent();
		b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
		blogRepository.updateViews(id);
		return b;
	}

	@Override
	public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
		return blogRepository.findAll(new Specification<Blog>() {
			@Override
			public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				//模糊查找标题
				if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null) {
					predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blogQuery.getTitle() + "%"));
				}
				//查找分类
				if (blogQuery.getTypeId() != null) {//Long类型
					predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blogQuery.getTypeId()));
				}
				//查找推荐
				if (blogQuery.isRecommend()) {
					predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blogQuery.isRecommend()));
				}
				//拼接SQL
				criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
				return null;
			}
		}, pageable);
	}

	@Override
	public Page<Blog> listBlog(Pageable pageable) {
		return blogRepository.findAll(pageable);
	}

	@Override
	public Page<Blog> listBlog(Long tagId, Pageable pageable) {
		return blogRepository.findAll(new Specification<Blog>() {
			@Override
			public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Join join = root.join("tags");
				return criteriaBuilder.equal(join.get("id"), tagId);
			}
		}, pageable);
	}

	@Override
	public Page<Blog> listBlog(String query, Pageable pageable) {
		return blogRepository.findByQuery(query, pageable);
	}

	@Override
	public List<Blog> listRecommendBlogTop(Integer size) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(0, size, sort);
		return blogRepository.findTop(pageable);
	}

	@Override
	public Map<String, List<Blog>> archiveBlog() {
		List<String> years = blogRepository.findGroupYear();
		Map<String, List<Blog>> map = new LinkedHashMap<>();//LinkedHashMap，防止读取乱序
		for (String year : years) {
			map.put(year, blogRepository.findByYear(year));
		}
		return map;
	}

	@Override
	public Long countBlog() {
		return blogRepository.count();
	}

	@Transactional
	@Override
	public Blog saveBlog(Blog blog) {
		blog.setCreateTime(new Date());
		blog.setUpdateTime(new Date());
		blog.setViews(0);
		return blogRepository.save(blog);
	}

	@Transactional
	@Override
	public Blog updateBlog(Long id, Blog blog) {
		Blog b = blogRepository.findById(id).orElse(null);
		if (b == null) {
			throw new NotFoundException("该博客不存在");
		}
		BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));//ignoreProperties忽略blog中存在null的属性
		b.setUpdateTime(new Date());
		return blogRepository.save(b);
	}

	@Transactional
	@Override
	public void deleteBlog(Long id) {
		blogRepository.deleteById(id);
	}
}
