package top.naccl.util.markdown.ext.gfm.heimu.internal;

import top.naccl.util.markdown.ext.gfm.heimu.Heimu;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;

import java.util.Collections;
import java.util.Set;

abstract class HeimuNodeRenderer implements NodeRenderer {

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.<Class<? extends Node>>singleton(Heimu.class);
    }
}
