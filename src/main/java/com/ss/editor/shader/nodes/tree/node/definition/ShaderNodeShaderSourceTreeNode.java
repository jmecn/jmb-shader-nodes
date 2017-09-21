package com.ss.editor.shader.nodes.tree.node.definition;

import com.ss.editor.annotation.FXThread;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.shader.nodes.model.shader.node.definition.ShaderNodeShaderSource;
import com.ss.editor.shader.nodes.ui.PluginIcons;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The node to present shader source of a shader node.
 *
 * @author JavaSaBr
 */
public class ShaderNodeShaderSourceTreeNode extends TreeNode<ShaderNodeShaderSource> {

    public ShaderNodeShaderSourceTreeNode(@NotNull final ShaderNodeShaderSource element, final long objectId) {
        super(element, objectId);
    }

    @Override
    @FXThread
    public @Nullable Image getIcon() {
        return PluginIcons.CODE_16;
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return "[" + getElement().getLanguage() + "] " + getElement().getShaderPath();
    }
}
