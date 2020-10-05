package top.naccl.util.markdown.ext.gfm.invert;

import top.naccl.util.markdown.ext.gfm.invert.internal.InvertDelimiterProcessor;
import top.naccl.util.markdown.ext.gfm.invert.internal.InvertHtmlNodeRenderer;
import top.naccl.util.markdown.ext.gfm.invert.internal.InvertTextContentNodeRenderer;
import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentNodeRendererFactory;
import org.commonmark.renderer.text.TextContentRenderer;

/**
 * @Description: Extension for GFM invert using $$ (GitHub Flavored Markdown).
 * Create it with {@link #create()} and then configure it on the builders
 * ({@link Parser.Builder#extensions(Iterable)},
 * {@link HtmlRenderer.Builder#extensions(Iterable)}).
 * The parsed invert text regions are turned into {@link Invert} nodes.
 * @Author: Naccl
 * @Date: 2020-05-13
 */
public class InvertExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension, TextContentRenderer.TextContentRendererExtension {

	private InvertExtension() {
	}

	public static Extension create() {
		return new InvertExtension();
	}

	@Override
	public void extend(Parser.Builder parserBuilder) {
		parserBuilder.customDelimiterProcessor(new InvertDelimiterProcessor());
	}

	@Override
	public void extend(HtmlRenderer.Builder rendererBuilder) {
		rendererBuilder.nodeRendererFactory(new HtmlNodeRendererFactory() {
			@Override
			public NodeRenderer create(HtmlNodeRendererContext context) {
				return new InvertHtmlNodeRenderer(context);
			}
		});
	}

	@Override
	public void extend(TextContentRenderer.Builder rendererBuilder) {
		rendererBuilder.nodeRendererFactory(new TextContentNodeRendererFactory() {
			@Override
			public NodeRenderer create(TextContentNodeRendererContext context) {
				return new InvertTextContentNodeRenderer(context);
			}
		});
	}
}
