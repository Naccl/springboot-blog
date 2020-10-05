package top.naccl.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 博客评论实体类
 * @Author: Naccl
 * @Date: 2020-04-24
 */

@Entity(name = "t_comment")
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//默认使用Oracle的自增策略，设置MySQL的自增方式
	private Long id;
	@NotBlank(message = "昵称不能为空")
	private String nickname;
	@NotBlank(message = "邮箱不能为空")
	private String email;
	@NotBlank(message = "评论内容不能为空")
	private String content;//评论内容
	private String avatar;//头像
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;//评论时间
	private String ip;//评论者ip地址
	private boolean published;//公开或回收站
	private boolean adminComment;//博主回复
	private boolean aboutMe;//关于我页面评论
	private boolean receiveEmail;//接收邮件提醒

	@ManyToOne
	private Blog blog;//所属的文章

	@OneToMany(mappedBy = "parentComment")//一个父评论对应多个子评论，子评论作为被维护端，通过parentComment建立关联
	private List<Comment> replyComments = new ArrayList<>();

	@ManyToOne
	private Comment parentComment;//父评论
}
