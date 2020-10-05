package top.naccl.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 访客实体类，根据腾讯API返回的数据建立字段
 * @Author: Naccl
 * @Date: 2020-05-15
 */

@Entity(name = "t_visitor")
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Visitor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//默认使用Oracle的自增策略，设置MySQL的自增方式
	private Long id;
	private String ip;
	private Integer views;
	private String nation;
	private String province;
	private String city;
	private String district;
	private Double lng;//经度
	private Double lat;//纬度
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstTime;//首次访问时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastTime;//最后访问时间

}
