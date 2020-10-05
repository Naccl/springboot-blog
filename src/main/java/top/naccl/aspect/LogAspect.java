package top.naccl.aspect;

import top.naccl.po.Site;
import top.naccl.service.SiteService;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 请求日志AOP
 * @Author: Naccl
 * @Date: 2020-04-23
 */

@Aspect
@Component
public class LogAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SiteService siteService;

	@Pointcut("execution(* top.naccl.web.*.*(..))")
	public void log() {
	}

	@Before("log()")
	public void doBefore(JoinPoint joinpoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		String ip = request.getRemoteAddr();
		String classMethod = joinpoint.getSignature().getDeclaringTypeName() + "." + joinpoint.getSignature().getName();
		Object[] args = joinpoint.getArgs();

		logSite(uri);//记录URI 访问次数

		RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
		logger.info("Request : {}", requestLog);//ip='0:0:0:0:0:0:0:1'是因为输入的URl为localhost，访问 127.0.0.1 则打印 127.0.0.1
	}

	@AfterReturning(pointcut = "log()", returning = "result")
	public void deAfterReturn(Object result) {
		logger.info("Result : {}", result);
	}

	@AllArgsConstructor
	@ToString
	private class RequestLog {
		private String url;
		private String ip;
		private String classMethod;
		private Object[] args;
	}

	private void logSite(String uri) {
		Site s = siteService.getSitePage(uri);
		if (s == null) {
			Site site = new Site();
			site.setUri(uri);
			siteService.saveSitePage(site);
		} else {
			siteService.updateViews(s.getId());
		}
	}
}
