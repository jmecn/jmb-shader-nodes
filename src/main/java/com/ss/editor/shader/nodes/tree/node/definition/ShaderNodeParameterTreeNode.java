package com.ss.editor.shader.nodes.tree.node.definition;

import com.jme3.shader.ShaderNodeVariable;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.shader.nodes.ui.PluginIcons;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The node to present shader node parameter.
 *
 * @author JavaSaBr
 */
public class ShaderNodeParameterTreeNode extends TreeNode<ShaderNodeVariable> {

    public ShaderNodeParameterTreeNode(@NotNull final ShaderNodeVariable element, final long objectId) {
        super(element, objectId);
    }

    @Override
    @FXThread
    public @Nullable Image getIcon() {
        return PluginIcons.VARIABLE_16;
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return getElement().getName();
    }

    @Override
    @FXThread
    public void setName(@NotNull final String name) {
        getElement().setName(name);
    }

    @Override
    @FXThread
    public boolean canEditName() {
        return true;
    }
}
