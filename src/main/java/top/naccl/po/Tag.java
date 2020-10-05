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
 * @Description: 博客标签实体类
 * @Author: Naccl
 * @Date: 2020-04-24
 */

@Entity(name = "t_tag")
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//默认使用Oracle的自增策略，设置MySQL的自增方式
	private Long id;
	@NotBlank(message = "标签名称不能为空")
	private String name;
	@NotBlank(message = "颜色不能为空")
	private String color;

	@ManyToMany(mappedBy = "tags")//每个博客对应多个标签，Tag作为被维护端，通过Blog的tags建立关联
	private List<Blog> blogs = new ArrayList<>();
}
