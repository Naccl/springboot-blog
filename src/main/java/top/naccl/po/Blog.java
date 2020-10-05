package top.naccl.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 博客实体类
 * @Author: Naccl
 * @Date: 2020-04-24
 */


@Entity(name = "t_blog")
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Blog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//默认使用Oracle的自增策略，设置MySQL的自增方式
	private Long id;
	private String title;

	@Basic(fetch = FetchType.LAZY)//懒加载
	@Lob//大字段longtext-4G
	private String content;
	private String firstPicture;//首图
	private String flag;//原创、转载、翻译
	private Integer views;//浏览次数
	private boolean appreciation;//赞赏开关
	private boolean shareStatement;//版权声明开关
	private boolean commentabled;//评论开关
	private boolean published;//发布或草稿
	private boolean recommend;//推荐开关
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;//更新时间
	private String description;//描述
	private Integer wordCount;//文章字数
	private Integer readTime;//阅读时长(分钟)

	@ManyToOne
	private Type type;//文章分类

	@ManyToMany(cascade = {CascadeType.PERSIST})//级联新增：新增博客时，如果需要新增tag则一同新增
	private List<Tag> tags = new ArrayList<>();//文章标签

	@ManyToOne
	private User user;//文章所属用户

	@OneToMany(mappedBy = "blog")//一个博客对应多个评论，Blog作为被维护端，通过Comment的blog建立关联
	private List<Comment> comments = new ArrayList<>();//文章评论

	@Transient//不存储进数据库
	private String tagIds;


	public void init() {
		this.tagIds = tagsToIds(this.getTags());
	}

	/**
	 * 将博客的标签List转换为页面可以读取的String : "1,2,3,4,5"
	 */
	private String tagsToIds(List<Tag> tags) {
		if (!tags.isEmpty()) {
			StringBuilder ids = new StringBuilder();
			boolean flag = false;
			for (Tag tag : tags) {
				if (flag) {
					ids.append(",");
				} else {
					flag = true;
				}
				ids.append(tag.getId());
			}
			return ids.toString();
		}
		return tagIds;
	}
}
