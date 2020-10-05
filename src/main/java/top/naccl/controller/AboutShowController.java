package top.naccl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: 关于我页面
 * @Author: Naccl
 * @Date: 2020-05-01
 */

@Controller
public class AboutShowController {
	@GetMapping("/about")
	public String about() {
		return "about";
	}
}
