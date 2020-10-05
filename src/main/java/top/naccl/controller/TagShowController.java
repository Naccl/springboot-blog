package top.naccl.controller;

import top.naccl.po.Tag;
import top.naccl.service.BlogService;
import top.naccl.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Description: 标签展示页面
 * @Author: Naccl
 * @Date: 2020-04-30
 */

@Controller
public class TagShowController {
	@Autowired
	private TagService tagService;
	@Autowired
	private BlogService blogService;

	@GetMapping("/tags")
	public String tags(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                   Model model) {
		List<Tag> tags = tagService.listTagTop(10000);
		Long id = tags.get(0).getId();
		model.addAttribute("tags", tags);
		model.addAttribute("page", blogService.listBlog(id, pageable));
		model.addAttribute("activeTagId", id);
		return "tags";
	}

	@GetMapping("/tags/{id}")
	public String tagsWithId(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                         @PathVariable Long id, Model model) {
		List<Tag> tags = tagService.listTagTop(10000);
		model.addAttribute("tags", tags);
		model.addAttribute("page", blogService.listBlog(id, pageable));
		model.addAttribute("activeTagId", id);
		return "tags";
	}
}
