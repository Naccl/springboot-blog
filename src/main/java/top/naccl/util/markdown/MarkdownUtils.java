package top.naccl.util.markdown;

import top.naccl.util.markdown.ext.gfm.heimu.HeimuExtension;
import top.naccl.util.markdown.ext.gfm.invert.InvertExtension;
import top.naccl.util.markdown.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @Description: Markdown转换
 * @Author: Naccl
 * @Date: 2020-04-29
 */
public class MarkdownUtils {
	private static Object TaskListItemHtmlNodeRenderer;

	/**
	 * markdown格式转换成HTML格式
	 */
	public static String markdownToHtml(String markdown) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		return renderer.render(document);
	}

	/**
	 * 增加扩展[标题锚点，表格生成]
	 */
	public static String markdownToHtmlExtensions(String markdown) {
		//h标题生成id
//		Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
		//转换table的HTML
		List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
		//任务列表
		Set<Extension> taskListExtension = Collections.singleton(TaskListItemsExtension.create());
		//删除线
		Set<Extension> delExtension = Collections.singleton(StrikethroughExtension.create());
		//黑幕
		Set<Extension> heimuExtension = Collections.singleton(HeimuExtension.create());
		//反转显示
		Set<Extension> invertExtension = Collections.singleton(InvertExtension.create());
		Parser parser = Parser.builder()
				.extensions(tableExtension)
				.extensions(taskListExtension)
				.extensions(delExtension)
				.extensions(heimuExtension)
				.extensions(invertExtension)
				.build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer = HtmlRenderer.builder()
//				.extensions(headingAnchorExtensions)
				.extensions(tableExtension)
				.extensions(taskListExtension)
				.extensions(delExtension)
				.extensions(heimuExtension)
				.extensions(invertExtension)
				.attributeProviderFactory(new AttributeProviderFactory() {
					public AttributeProvider create(AttributeProviderContext context) {
						return new CustomAttributeProvider();
					}
				})
				.build();
		return renderer.render(document);
	}

	/**
	 * 处理标签的属性
	 */
	static class CustomAttributeProvider implements AttributeProvider {
		@Override
		public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
			//改变a标签的target属性为_blank
			if (node instanceof Link) {
				attributes.put("target", "_blank");
			}
			if (node instanceof TableBlock) {
				attributes.put("class", "ui celled table");
			}
		}
	}


	public static void main(String[] args) {
		System.out.println(markdownToHtmlExtensions(""));
	}
}
