package top.naccl.dao;

import top.naccl.po.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SiteRepository extends JpaRepository<Site, Long>{

	Site findByUri(String uri);

	@Transactional
	@Modifying
	@Query("update t_site s set s.views = s.views+1 where s.id = ?1")
	int updateViews(Long id);
}
