package top.naccl.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体类
 * @Author: Naccl
 * @Date: 2020-04-24
 */

@Entity(name = "t_user")
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//默认使用Oracle的自增策略，设置MySQL的自增方式
	private Long id;
	private String nickname;
	private String username;
	private String password;
	private String email;
	private String avatar;//头像
	private Integer type;//用户类型
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	@OneToMany(mappedBy = "user")//一个用户对应多个博客，User作为被维护端，通过Blog的user建立关联
	private List<Blog> blogs = new ArrayList<>();
}
