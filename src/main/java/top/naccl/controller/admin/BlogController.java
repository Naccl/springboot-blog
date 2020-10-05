package top.naccl.controller.admin;

import top.naccl.po.Blog;
import top.naccl.po.User;
import top.naccl.service.BlogService;
import top.naccl.service.TagService;
import top.naccl.service.TypeService;
import top.naccl.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Description: 博客管理页面
 * @Author: Naccl
 * @Date: 2020-04-24
 */

@Controller
@RequestMapping("/admin")
public class BlogController {
	@Autowired
	private BlogService blogService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private TagService tagService;

	/**
	 * 获取博客管理页面
	 */
	@GetMapping("/blogs")
	public String blogs(@PageableDefault(size = 10, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                    BlogQuery blogQuery, Model model) {
		model.addAttribute("types", typeService.listType());
		model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
		return "admin/blogs";
	}

	/**
	 * 博客查询
	 */
	@PostMapping("/blogs/search")
	public String search(@PageableDefault(size = 10, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                     BlogQuery blogQuery, Model model) {
		model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
		return "admin/blogs :: blogList";
	}

	/**
	 * 给博客新增、编辑页面的 model 添加分类、标签属性
	 */
	private void setTypeAndTag(Model model) {
		model.addAttribute("types", typeService.listType());
		model.addAttribute("tags", tagService.listTag());
	}

	/**
	 * 跳转博客新增页面
	 */
	@GetMapping("/blogs/input")
	public String input(Model model) {
		setTypeAndTag(model);
		model.addAttribute("blog", new Blog());
		return "admin/blogs-input";
	}

	/**
	 * 跳转博客编辑页面
	 */
	@GetMapping("/blogs/{id}/input")
	public String edit(@PathVariable Long id, Model model) {
		setTypeAndTag(model);
		Blog blog = blogService.getBlog(id);
		blog.init();
		model.addAttribute("blog", blog);
		return "admin/blogs-input";
	}

	/**
	 * POST提交 博客新增、编辑
	 */
	@PostMapping("/blogs")
	public String post(Blog blog, HttpSession session, RedirectAttributes redirectAttributes) {
		blog.setUser((User) session.getAttribute("user"));
		blog.setType(typeService.getType(blog.getType().getId()));
		blog.setTags(tagService.listTag(blog.getTagIds()));
		blog.setWordCount(blog.getWordCount());
		//Typora自带字数统计，鉴于代码的阅读速度不好估计，直接加个5 = =
		blog.setReadTime((int) Math.round(blog.getWordCount() / 200.0) + 5);

		if (blog.getId() == null) {
			Blog b = blogService.saveBlog(blog);
			if (b == null) {
				redirectAttributes.addFlashAttribute("message", "添加失败");
			} else {
				redirectAttributes.addFlashAttribute("message", "添加成功");
			}
		} else {
			Blog b = blogService.updateBlog(blog.getId(), blog);
			if (b == null) {
				redirectAttributes.addFlashAttribute("message", "编辑失败");
			} else {
				redirectAttributes.addFlashAttribute("message", "编辑成功");
			}
		}
		return "redirect:/admin/blogs";
	}

	/**
	 * 博客删除
	 */
	@GetMapping("/blogs/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		blogService.deleteBlog(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/admin/blogs";
	}
}
