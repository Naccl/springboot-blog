package top.naccl.controller.admin;

import top.naccl.po.Tag;
import top.naccl.service.TagService;
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
 * @Description: 标签管理页面
 * @Author: Naccl
 * @Date: 2020-04-26
 */

@Controller
@RequestMapping("/admin")
public class TagController {

	@Autowired
	private TagService tagService;

	/**
	 * 获取标签页面
	 */
	@GetMapping("/tags")
	public String tags(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
		model.addAttribute("page", tagService.listTag(pageable));
		return "admin/tags";
	}

	/**
	 * 跳转添加标签页面
	 */
	@GetMapping("tags/input")
	public String input(Model model) {
		model.addAttribute("tag", new Tag());
		return "admin/tags-input";
	}

	/**
	 * 跳转编辑标签页面
	 */
	@GetMapping("/tags/{id}/input")
	public String edit(@PathVariable Long id, Model model) {
		model.addAttribute("tag", tagService.getTag(id));
		return "admin/tags-input";
	}

	/**
	 * POST提交 添加、编辑标签
	 */
	@PostMapping("/tags")
	public String post(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		//验证BindingResult是否存在错误，如 输入为空，存在错误则带着错误信息返回提交页面
		if (bindingResult.hasErrors()) {
			return "admin/tags-input";
		}

		//保存新增、编辑标签
		if (tag.getId() == null) {
			//验证标签是否已存在
			Tag tag1 = tagService.getTagByName(tag.getName());
			if (tag1 != null) {
				bindingResult.rejectValue("name", "nameError", "标签已存在");
			}

			Tag t = tagService.saveTag(tag);
			if (t == null) {//没保存成功
				redirectAttributes.addFlashAttribute("message", "添加失败");
			} else {//保存成功
				redirectAttributes.addFlashAttribute("message", "添加成功");
			}
		} else {
			Tag t = tagService.updateTag(tag.getId(), tag);
			if (t == null) {//没保存成功
				redirectAttributes.addFlashAttribute("message", "编辑失败");
			} else {//保存成功
				redirectAttributes.addFlashAttribute("message", "编辑成功");
			}
		}
		return "redirect:/admin/tags";//重定向到public String tags()查询数据
	}

	/**
	 * 删除标签
	 */
	@GetMapping("/tags/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		tagService.deleteTag(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/admin/tags";
	}
}
