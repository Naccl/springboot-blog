package top.naccl.controller.admin;

import top.naccl.po.Comment;
import top.naccl.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @Description: 后台评论管理
 * @Author: Naccl
 * @Date: 2020-05-03
 */
@Controller
@RequestMapping("/admin")
public class CommentManagerController {
	@Autowired
	private CommentService commentService;

	/**
	 * 跳转评论列表页面
	 */
	@GetMapping("/comments")
	public String comments(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
		model.addAttribute("page", commentService.listComment(true, pageable));
		return "admin/comments";
	}

	/**
	 * 跳转评论回收站页面
	 */
	@GetMapping("/comments/trashes")
	public String trashComments(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
		model.addAttribute("page", commentService.listComment(false, pageable));
		return "admin/comments-trash";
	}

	/**
	 * 跳转编辑评论页面
	 */
	@GetMapping("/comments/{id}/edit")
	public String editComment(@PathVariable Long id, Model model) {
		model.addAttribute("comment", commentService.getComment(id));
		return "admin/comments-edit";
	}

	/**
	 * POST提交 编辑评论
	 */
	@PostMapping("/comments")
	public String post(@Valid Comment comment, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		//验证BindingResult是否存在错误，如 输入为空，存在错误则带着错误信息返回提交页面
		if (bindingResult.hasErrors()) {
			return "admin/comments-edit";
		}

		//保存编辑评论
		Comment c = commentService.updateComment(comment.getId(), comment);
		if (c == null) {//没保存成功
			redirectAttributes.addFlashAttribute("message", "编辑失败");
		} else {//保存成功
			redirectAttributes.addFlashAttribute("message", "编辑成功");
		}
		return "redirect:/admin/comments";//重定向到public String comments()查询数据
	}

	/**
	 * 评论移至回收站
	 */
	@GetMapping("/comments/{id}/trash")
	public String trashComment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		commentService.trashComment(id);
		redirectAttributes.addFlashAttribute("message", "评论已移动至回收站");
		return "redirect:/admin/comments";
	}

	/**
	 * 评论移出回收站
	 */
	@GetMapping("/comments/{id}/publish")
	public String publishComment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		commentService.publishComment(id);
		redirectAttributes.addFlashAttribute("message", "评论已还原");
		return "redirect:/admin/comments/trashes";
	}

	/**
	 * 删除评论
	 */
	@GetMapping("/comments/{id}/delete")
	public String deleteComment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		commentService.deleteComment(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/admin/comments/trashes";
	}

}
