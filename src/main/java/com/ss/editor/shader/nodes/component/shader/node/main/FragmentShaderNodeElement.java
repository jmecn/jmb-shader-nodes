package com.ss.editor.shader.nodes.component.shader.node.main;

import com.jme3.material.ShaderGenerationInfo;
import com.jme3.shader.ShaderNode;
import com.ss.editor.shader.nodes.component.shader.ShaderNodesContainer;
import com.ss.editor.shader.nodes.component.shader.node.ShaderNodeElement;
import com.ss.editor.shader.nodes.component.shader.node.global.InputGlobalShaderNodeElement;
import com.ss.editor.shader.nodes.component.shader.node.parameter.InputShaderNodeParameter;
import com.ss.editor.shader.nodes.component.shader.node.parameter.OutputShaderNodeParameter;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of shader element to present fragment shader nodes.
 *
 * @author JavaSaBr
 */
public class FragmentShaderNodeElement extends MainShaderNodeElement {

    public FragmentShaderNodeElement(@NotNull final ShaderNodesContainer container, @NotNull final ShaderNode object) {
        super(container, object);
    }

    @Override
    public boolean canAttach(@NotNull final InputShaderNodeParameter inputParameter,
                             @NotNull final OutputShaderNodeParameter outputParameter) {

        if(!super.canAttach(inputParameter, outputParameter)) {
            return false;
        }

        final ShaderNodeElement<?> sourceElement = outputParameter.getNodeElement();

        if (sourceElement instanceof InputGlobalShaderNodeElement) {
            final ShaderGenerationInfo info = ((InputGlobalShaderNodeElement) sourceElement).getObject();
            return outputParameter.getVariable() != info.getVertexGlobal();
        }

        return sourceElement instanceof MaterialShaderNodeElement ||
                sourceElement instanceof FragmentShaderNodeElement ||
                sourceElement instanceof VertexShaderNodeElement ||
                sourceElement instanceof WorldShaderNodeElement;
    }
}