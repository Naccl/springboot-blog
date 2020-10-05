package top.naccl.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.naccl.po.Comment;
import top.naccl.po.User;
import top.naccl.service.BlogService;
import top.naccl.service.CommentService;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.MD5Utils;
import top.naccl.util.MailUtils;
import top.naccl.util.verifycode.VerifyCode;
import top.naccl.util.verifycode.VerifyCodeFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description: 评论处理
 * @Author: Naccl
 * @Date: 2020-04-30
 */

@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private MailUtils mailUtils;

	@Autowired
	private MailProperties mailProperties;

	//	@Value("${comment.avatar}")
	private String avatar;

	/**
	 * 获取评论列表
	 */
	@GetMapping("/comments/{blogId}")
	public String blogComments(@PathVariable Long blogId, Model model) {
		model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
		return "blog :: commentList";
	}

	/**
	 * 获取评论列表
	 */
	@GetMapping("/comments/about")
	public String aboutComments(Model model) {
		model.addAttribute("comments", commentService.listCommentByAboutMe(true));
		return "about :: commentList";
	}

	/**
	 * 获取评论验证码
	 */
	@GetMapping("/verifyCode")
	public void getVerifyCode(HttpSession session, HttpServletResponse response) {
		VerifyCodeFactory verifyCodeFactory = new VerifyCodeFactory();
		try {
			//设置长宽
			VerifyCode verifyCode = verifyCodeFactory.getMathVerifyCode(100, 30, '-');
			String answer = verifyCode.getCode();
			//将verifyCode绑定到session
			session.setAttribute("verifyCode", answer);
			//设置响应头信息，告诉浏览器不要缓存此内容
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			//设置HttpOnly属性，防止Xss攻击，加了这个，第一次获取的sessionID和之后的sessionID不一致
			//response.setHeader("Set-Cookie", "name=value; HttpOnly");
			//在代理服务器端防止缓冲
			response.setDateHeader("Expires", 0);
			//设置响应内容类型
			response.setContentType("image/jpeg");
			response.getOutputStream().write(verifyCode.getImgBytes());
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * POST评论
	 */
	@PostMapping("/comments")
	@ResponseBody
	public JSONObject judgeVerifyCode(HttpSession session, HttpServletRequest request, Comment comment) {
		String answer = (String) session.getAttribute("verifyCode");
		String inputVerifyCode = request.getParameter("verifyCode");
		JSONObject result = new JSONObject();
		if (answer == null) {
			result.put("success", false);
			result.put("errorInfo", 2);
			return result;
		}
		if (inputVerifyCode != null && !"".equals(inputVerifyCode) && answer.equalsIgnoreCase(inputVerifyCode)) {
			saveComment(comment, session, request);//保存评论
			result.put("success", true);
			session.setAttribute("verifyCode", null);

			sendMailToParentComment(comment);//发送邮件提醒回复对象
			sendMailToMe(comment);//发送邮件提醒我自己
		} else {
			result.put("success", false);
			result.put("errorInfo", 1);
		}
		return result;
	}

	/**
	 * 保存评论
	 */
	private void saveComment(Comment comment, HttpSession session, HttpServletRequest request) {
		if (comment.isAboutMe()) {
			comment.setAboutMe(true);
			comment.setBlog(null);
		} else {
			comment.setAboutMe(false);
			Long blogId = comment.getBlog().getId();
			comment.setBlog(blogService.getBlog(blogId));
		}

		User user = (User) session.getAttribute("user");
		if (user != null) {
			comment.setAdminComment(true);
			comment.setAvatar(user.getAvatar());
			comment.setNickname(user.getNickname());
			comment.setEmail(user.getEmail());
		} else {
			String nicknameMD5 = MD5Utils.getMD5(comment.getNickname());//根据评论昵称取MD5，保证每一个昵称对应一个头像
			char m = nicknameMD5.charAt(nicknameMD5.length() - 1);//取MD5最后一位
			int num = m % 6 + 1;//计算对应的头像
			avatar = "/images/comment-avatar/" + num + ".jpg";
			comment.setAvatar(avatar);
		}
		comment.setPublished(true);//默认发表时公开
		String ip = IpAddressUtils.getIpAddress(request);
		comment.setIp(ip);
		commentService.saveComment(comment);
	}

	/**
	 * 发送邮件提醒回复对象
	 */
	private void sendMailToParentComment(Comment comment) {
		if (comment.getParentComment() != null && comment.getParentComment().isReceiveEmail()) {
			String url;
			if (comment.isAboutMe()) {
				url = "https://naccl.top/about";
			} else {
				url = "https://naccl.top/blog/" + comment.getBlog().getId();
			}
			String toAccount = comment.getParentComment().getEmail();
			String subject = "Naccl's Blog评论回复";
			String content = "<body><h2>您的评论有新回复</h2><p><a href='" + url + "'>详情请看" + url + "</a></p><p>此邮件为自动发送，如不想再收到此类消息，请回复TD</p></body>";
			mailUtils.sendHTMLMail(toAccount, subject, content);
		}
	}

	/**
	 * 发送邮件提醒我自己
	 */
	private void sendMailToMe(Comment comment) {
		//如果有父评论 且父评论是我自己 或是我自己的回复 就不用再发一封了
		if (comment.getParentComment() == null || !mailProperties.getUsername().equals(comment.getParentComment().getEmail()) || !mailProperties.getUsername().equals(comment.getEmail())) {
			String url;
			if (comment.isAboutMe()) {
				url = "https://naccl.top/about";
			} else {
				url = "https://naccl.top/blog/" + comment.getBlog().getId();
			}
			String toAccount = mailProperties.getUsername();
			String subject = "Naccl's Blog新评论";
			String content = "<body><p><a href='" + url + "'>新评论" + url + "</a></p></body>";
			mailUtils.sendHTMLMail(toAccount, subject, content);
		}
	}
}