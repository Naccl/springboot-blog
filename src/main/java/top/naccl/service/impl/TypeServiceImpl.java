package top.naccl.service.impl;

import top.naccl.NotFoundException;
import top.naccl.dao.TypeRepository;
import top.naccl.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.service.TypeService;

import java.util.List;

/**
 * @Description: 分类操作实现类
 * @Author: Naccl
 * @Date: 2020-04-25
 */

@Service
public class TypeServiceImpl implements TypeService {
	@Autowired
	private TypeRepository typeRepository;

	@Transactional
	@Override
	public Type saveType(Type type) {
		return typeRepository.save(type);
	}

	@Override
	public Type getType(Long id) {
		return typeRepository.getOne(id);
	}

	@Override
	public Type getTypeByName(String name) {
		return typeRepository.findByName(name);
	}

	@Override
	public Page<Type> listType(Pageable pageable) {
		return typeRepository.findAll(pageable);
	}

	@Override
	public List<Type> listType() {
		return typeRepository.findAll();
	}

	@Override
	public List<Type> listTypeTop(Integer size) {
		//SpringBoot2.2.1以上的，使用Sort.by获取Sort对象，PageRequest.of获取PageRequest对象
		//新版本Sort和PageRequest构造函数是私有的，PageRequest(0,8,Sort.by(Sort.Direction.DESC,"blog.size"))
		Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
		Pageable pageable = PageRequest.of(0, size, sort);
		return typeRepository.findTop(pageable);
	}

	@Transactional
	@Override
	public Type updateType(Long id, Type type) {
		Type t = typeRepository.findById(id).orElse(null);//找到将要修改的分类
		if (t == null) {
			throw new NotFoundException("该分类不存在");
		}
		BeanUtils.copyProperties(type, t);//将源对象的属性拷贝到目标对象
		return typeRepository.save(t);//保存修改后的分类
	}

	@Transactional
	@Override
	public void deleteType(Long id) {
		typeRepository.deleteById(id);
	}
}
