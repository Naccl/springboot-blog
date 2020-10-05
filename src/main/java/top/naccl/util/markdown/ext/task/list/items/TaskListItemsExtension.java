package top.naccl.util.markdown.ext.task.list.items;

import top.naccl.util.markdown.ext.task.list.items.internal.TaskListItemHtmlNodeRenderer;
import top.naccl.util.markdown.ext.task.list.items.internal.TaskListItemPostProcessor;
import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Extension for adding task list items.
 * <p>
 * Create it with {@link #create()} and then configure it on the builders
 * ({@link Parser.Builder#extensions(Iterable)},
 * {@link HtmlRenderer.Builder#extensions(Iterable)}).
 * </p>
 */
public class TaskListItemsExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    private TaskListItemsExtension() {
    }

    public static Extension create() {
        return new TaskListItemsExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.postProcessor(new TaskListItemPostProcessor());
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(new HtmlNodeRendererFactory() {
            @Override
            public NodeRenderer create(HtmlNodeRendererContext context) {
                return new TaskListItemHtmlNodeRenderer(context);
            }
        });
    }
}
