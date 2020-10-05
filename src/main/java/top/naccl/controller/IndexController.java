package top.naccl.controller;

import com.alibaba.fastjson.JSONObject;
import top.naccl.po.Visitor;
import top.naccl.service.BlogService;
import top.naccl.service.TagService;
import top.naccl.service.TypeService;
import top.naccl.service.VisitorService;
import top.naccl.util.IpAddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Description: 首页
 * @Author: Naccl
 * @Date: 2020-04-23
 */

@Controller
public class IndexController {
	@Autowired
	private BlogService blogService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private TagService tagService;
	@Autowired
	private VisitorService visitorService;

	@GetMapping("/")
	public String index(@PageableDefault(size = 8, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                    Model model) {
		model.addAttribute("page", blogService.listBlog(pageable));
		model.addAttribute("types", typeService.listTypeTop(5));
		model.addAttribute("tags", tagService.listTagTop(10));
		model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(5));
		return "index";
	}

	@GetMapping("/search")
	public String search(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                     @NotBlank @RequestParam(required = false) String query, Model model) {
		model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
		model.addAttribute("query", query);
		return "search";
	}

	@GetMapping("/blog/{id}")
	public String blog(@PathVariable Long id, Model model) {
		model.addAttribute("blog", blogService.getAndConvert(id));
		return "blog";
	}

	@GetMapping("/footer/newblog")
	public String newblogs(Model model) {
		model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
		return "_fragments :: newBlogList";
	}

	@GetMapping("/ipinfo")
	public String cityInfo(HttpSession session, HttpServletRequest request, Model model) {
		JSONObject sessionIP = (JSONObject) session.getAttribute("ip");
		if (sessionIP == null) {//第一次建立会话 请求API 返回ipInfo 记录访客信息
			String ip = request.getRemoteAddr();
//			String ip = "110.87.40.150";
			JSONObject ipInfo = IpAddressUtils.getCityInfo(request);
			if (ipInfo == null) {
				model.addAttribute("ipInfo", null);
				return "_fragments :: ip-info";
			}

			logVisitor(ipInfo);//记录访客信息

			session.setAttribute("ip", ipInfo);
			model.addAttribute("ipInfo", ipInfo);
		} else {
			logVisitor(sessionIP);//记录访客信息
			model.addAttribute("ipInfo", sessionIP);//直接从会话中取出 ipInfo
		}
		return "_fragments :: ip-info";
	}

	private void logVisitor(JSONObject ipInfo) {
		JSONObject ad_info = ipInfo.getJSONObject("ad_info");
		Visitor v = visitorService.getVisitor(ipInfo.getString("ip"));
		if (v == null) {
			JSONObject location = ipInfo.getJSONObject("location");
			Visitor visitor = new Visitor();
			visitor.setIp(ipInfo.getString("ip"));
			visitor.setNation(ad_info.getString("nation"));
			visitor.setProvince(ad_info.getString("province"));
			visitor.setCity(ad_info.getString("city"));
			visitor.setDistrict(ad_info.getString("district"));
			visitor.setLng(location.getDouble("lng"));
			visitor.setLat(location.getDouble("lat"));
			visitor.setViews(1);
			Date date = new Date();
			visitor.setFirstTime(date);
			visitor.setLastTime(date);
			visitorService.saveVisitor(visitor);
		} else {
			visitorService.updateVisitor(v.getId());
		}
	}
}
