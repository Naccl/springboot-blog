package top.naccl.controller;

import top.naccl.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: 归档展示页面
 * @Author: Naccl
 * @Date: 2020-04-30
 */

@Controller
public class ArchiveShowController {

	@Autowired
	private BlogService blogService;

	@GetMapping("/archives")
	public String archives(Model model) {
		model.addAttribute("archiveMap", blogService.archiveBlog());
		model.addAttribute("blogCount", blogService.countBlog());
		return "archives";

	}
}
