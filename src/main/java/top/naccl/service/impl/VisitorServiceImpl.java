package top.naccl.service.impl;

import top.naccl.NotFoundException;
import top.naccl.dao.VisitorRepository;
import top.naccl.po.Visitor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.service.VisitorService;

import java.util.Date;

/**
 * @Description: 记录访客
 * @Author: Naccl
 * @Date: 2020-05-15
 */

@Service
public class VisitorServiceImpl implements VisitorService {
	@Autowired
	VisitorRepository visitorRepository;

	@Override
	public Visitor getVisitor(String ip) {
		return visitorRepository.findByIp(ip);
	}

	@Transactional
	@Override
	public void saveVisitor(Visitor visitor) {
		visitorRepository.save(visitor);
	}

	@Transactional
	@Override
	public Visitor updateVisitor(Long id) {
		Visitor v = visitorRepository.findById(id).orElse(null);
		if (v == null) {
			throw new NotFoundException("该访客不存在");
		}
		Visitor visitor = new Visitor();
		BeanUtils.copyProperties(v, visitor);
		visitor.setLastTime(new Date());
		visitor.setViews(v.getViews() + 1);
		return visitorRepository.save(visitor);
	}
}
