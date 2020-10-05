package top.naccl.service;

import top.naccl.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
	Tag saveTag(Tag tag);

	Tag getTag(Long id);

	Tag getTagByName(String name);

	Page<Tag> listTag(Pageable pageable);

	List<Tag> listTag();

	List<Tag> listTagTop(Integer size);

	List<Tag> listTag(String ids);

	Tag updateTag(Long id ,Tag tag);

	void deleteTag(Long id);
}
