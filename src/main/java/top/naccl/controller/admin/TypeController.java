package top.naccl.controller.admin;

import top.naccl.po.Type;
import top.naccl.service.TypeService;
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
 * @Description: 分类管理页面
 * @Author: Naccl
 * @Date: 2020-04-25
 */

@Controller
@RequestMapping("/admin")
public class TypeController {

	@Autowired
	private TypeService typeService;

	/**
	 * 获取分类页面
	 */
	@GetMapping("/types")
	public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
		model.addAttribute("page", typeService.listType(pageable));
		return "admin/types";
	}

	/**
	 * 跳转添加分类页面
	 */
	@GetMapping("types/input")
	public String input(Model model) {
		model.addAttribute("type", new Type());
		return "admin/types-input";
	}

	/**
	 * 跳转编辑分类页面
	 */
	@GetMapping("/types/{id}/input")
	public String edit(@PathVariable Long id, Model model) {
		model.addAttribute("type", typeService.getType(id));
		return "admin/types-input";
	}

	/**
	 * POST提交 添加、编辑分类
	 *
	 * @Valid 用于验证是否符合要求，直接加在变量之前，在变量中添加验证信息的要求，当不符合要求时就会在方法中返回message的错误提示信息
	 * @Valid 和 BindingResult 是一一对应的，如果有多个@Valid，那么每个@Valid后面跟着的BindingResult就是这个@Valid的验证结果，顺序不能乱
	 */
	@PostMapping("/types")
	public String post(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		//验证分类是否已存在
		Type type1 = typeService.getTypeByName(type.getName());
		if (type1 != null) {
			// name 和 POJO 中对应的属性名称一致
			bindingResult.rejectValue("name", "nameError", "分类已存在");
		}

		//验证BindingResult是否存在错误，如 输入为空 或 分类已存在，存在错误则带着错误信息返回提交页面
		if (bindingResult.hasErrors()) {
			return "admin/types-input";
		}

		//保存新增、编辑分类
		if (type.getId() == null) {
			Type t = typeService.saveType(type);
			if (t == null) {//没保存成功
				redirectAttributes.addFlashAttribute("message", "添加失败");
			} else {//保存成功
				redirectAttributes.addFlashAttribute("message", "添加成功");
			}
		} else {
			Type t = typeService.updateType(type.getId(), type);
			if (t == null) {//没保存成功
				redirectAttributes.addFlashAttribute("message", "编辑失败");
			} else {//保存成功
				redirectAttributes.addFlashAttribute("message", "编辑成功");
			}
		}


		return "redirect:/admin/types";//重定向到public String types()查询数据
	}

	/**
	 * 删除分类
	 */
	@GetMapping("/types/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		typeService.deleteType(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/admin/types";
	}
}
