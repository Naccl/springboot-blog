package top.naccl.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description: 将博客列表的查询信息封装成对象
 * @Author: Naccl
 * @Date: 2020-04-26
 */

@NoArgsConstructor
@Getter
@Setter
public class BlogQuery {
	private String title;
	private Long TypeId;
	private boolean recommend;
}
