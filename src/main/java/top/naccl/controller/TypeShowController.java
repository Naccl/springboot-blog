package top.naccl.controller;

import top.naccl.po.Type;
import top.naccl.service.BlogService;
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

import java.util.List;

/**
 * @Description: 分类展示页面
 * @Author: Naccl
 * @Date: 2020-04-30
 */

@Controller
public class TypeShowController {
	@Autowired
	private TypeService typeService;
	@Autowired
	private BlogService blogService;

	@GetMapping("/types")
	public String types(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                    Model model) {
		List<Type> types = typeService.listTypeTop(10000);
		Long id = types.get(0).getId();
		BlogQuery blogQuery = new BlogQuery();
		blogQuery.setTypeId(id);
		model.addAttribute("types", types);
		model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
		model.addAttribute("activeTypeId", id);
		return "types";
	}

	@GetMapping("/types/{id}")
	public String typesWithId(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                          @PathVariable Long id, Model model) {
		List<Type> types = typeService.listTypeTop(10000);
		if (id == -1) {
			id = types.get(0).getId();
		}
		BlogQuery blogQuery = new BlogQuery();
		blogQuery.setTypeId(id);
		model.addAttribute("types", types);
		model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
		model.addAttribute("activeTypeId", id);
		return "types";
	}
}
