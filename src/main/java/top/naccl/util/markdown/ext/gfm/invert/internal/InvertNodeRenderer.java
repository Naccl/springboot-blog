package top.naccl.util.markdown.ext.gfm.invert.internal;

import top.naccl.util.markdown.ext.gfm.invert.Invert;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;

import java.util.Collections;
import java.util.Set;

abstract class InvertNodeRenderer implements NodeRenderer {

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.<Class<? extends Node>>singleton(Invert.class);
    }
}
