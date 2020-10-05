package top.naccl.service.impl;

import top.naccl.dao.SiteRepository;
import top.naccl.po.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.service.SiteService;

/**
 * @Description: 记录URI访问情况
 * @Author: Naccl
 * @Date: 2020-05-15
 */

@Service
public class SiteServiceImpl implements SiteService {
	@Autowired
	SiteRepository siteRepository;

	@Override
	public Site getSitePage(String uri) {
		return siteRepository.findByUri(uri);
	}

	@Transactional
	@Override
	public void saveSitePage(Site site) {
		site.setViews(1);
		siteRepository.save(site);
	}

	@Transactional
	@Override
	public int updateViews(Long id) {
		return siteRepository.updateViews(id);
	}
}
