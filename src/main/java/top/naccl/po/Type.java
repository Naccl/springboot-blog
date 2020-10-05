package top.naccl.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 博客分类实体类
 * @Author: Naccl
 * @Date: 2020-04-24
 */

@Entity(name = "t_type")
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Type {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//默认使用Oracle的自增策略，设置MySQL的自增方式
	private Long id;

	/**
	 * 关于org.hibernate.validator.constraints.NotBlank废弃：
	 * Deprecated. use the standard javax.validation.constraints.NotBlank constraint instead
	 */
	@NotBlank(message = "分类名称不能为空")
	private String name;

	@OneToMany(mappedBy = "type")//一个分类对应多个博客，Type作为被维护端，通过Blog的type建立关联
	private List<Blog> blogs = new ArrayList<>();
}
