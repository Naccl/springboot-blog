package top.naccl.dao;

import top.naccl.po.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

	Visitor findByIp(String ip);

}
