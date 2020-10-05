package top.naccl.service;

import top.naccl.po.Site;

public interface SiteService {

	Site getSitePage(String uri);

	void saveSitePage(Site site);

	int updateViews(Long id);
}
