package top.naccl.service;

import top.naccl.po.Visitor;

public interface VisitorService {

	Visitor getVisitor(String ip);

	void saveVisitor(Visitor visitor);

	Visitor updateVisitor(Long id);
}
