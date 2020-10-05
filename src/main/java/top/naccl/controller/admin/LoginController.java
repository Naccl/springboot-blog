package top.naccl.controller.admin;

import top.naccl.po.User;
import top.naccl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Description: 登录控制器
 * @Author: Naccl
 * @Date: 2020-04-24
 */

@Controller
@RequestMapping("/admin")
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping//不指定则默认为类前的"/admin"
	public String loginPage() {
		System.out.println("进入后台页面");
		return "admin/login";
	}

	/**
	 * 在登录失败时应选择重定向，因为要将登录页面刷新，使用转发将会把之前的错误数据保存在请求体中，导致不断刷新页面就会在后台重复验证错误数据。
	 * 而登录成功则需要请求转发，保证请求不会丢失，并可以把信息传递到转发页面。
	 */
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password,
	                    HttpSession session, RedirectAttributes redirectAttributes) {
		System.out.println("提交登录信息");
		User user = userService.checkUser(username, password);
		if (user != null) {
			System.out.println("验证正确");
			user.setPassword(null);
			session.setAttribute("user", user);
			return "redirect:/admin/blogs";
		} else {
			System.out.println("验证错误");
			//重定向用Model取不到Attributes
			redirectAttributes.addFlashAttribute("message", "用户名或密码错误");
			return "redirect:/admin";//通过上面的loginPage()方法重定向到登录页面
		}
	}

	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/admin";
	}
}
