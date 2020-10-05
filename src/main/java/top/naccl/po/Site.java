package top.naccl.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @Description: 网站统计
 * @Author: Naccl
 * @Date: 2020-05-15
 */

@Entity(name = "t_site")
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Site {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//默认使用Oracle的自增策略，设置MySQL的自增方式
	private Long id;
	private String uri;
	private Integer views;

}
