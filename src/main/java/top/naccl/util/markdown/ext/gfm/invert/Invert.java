package top.naccl.util.markdown.ext.gfm.invert;

import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;

/**
 * @Description: A invert node containing text and other inline nodes nodes as children.
 * @Author: Naccl
 * @Date: 2020-05-13
 */
public class Invert extends CustomNode implements Delimited {

	private static final String DELIMITER = "%%";

	@Override
	public String getOpeningDelimiter() {
		return DELIMITER;
	}

	@Override
	public String getClosingDelimiter() {
		return DELIMITER;
	}
}
