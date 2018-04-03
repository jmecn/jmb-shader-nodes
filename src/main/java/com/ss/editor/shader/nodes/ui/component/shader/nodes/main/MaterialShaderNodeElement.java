package com.ss.editor.shader.nodes.ui.component.shader.nodes.main;

import static com.ss.rlib.util.ClassUtils.unsafeCast;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.shader.ShaderNodeVariable;
import com.jme3.shader.VarType;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.JmeThread;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.shader.nodes.PluginMessages;
import com.ss.editor.shader.nodes.ui.component.editor.ShaderNodesChangeConsumer;
import com.ss.editor.shader.nodes.ui.component.shader.nodes.ShaderNodesContainer;
import com.ss.editor.shader.nodes.ui.component.shader.nodes.action.ShaderNodeAction;
import com.ss.editor.shader.nodes.ui.component.shader.nodes.action.remove.RemoveMaterialParamShaderNodeAction;
import com.ss.editor.shader.nodes.ui.component.shader.nodes.parameter.EditableMaterialShaderNodeParameter;
import com.ss.editor.shader.nodes.ui.component.shader.nodes.parameter.OutputShaderNodeParameter;
import com.ss.editor.ui.control.property.PropertyControl;
import com.ss.editor.ui.control.property.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of nodes element to present material parameters.
 *
 * @author JavaSaBr
 */
public class MaterialShaderNodeElement extends OutputVariableShaderNodeElement {

    @NotNull
    public static final String NAMESPACE = "MatParam";

    /**
     * Convert the mat param to shader nodes variable.
     *
     * @param matParam the mat param.
     * @return the shader nodes variable.
     */
    @FromAnyThread
    public static @NotNull ShaderNodeVariable toVariable(@NotNull MatParam matParam) {
        var type = matParam.getVarType();
        var glslType = type.getGlslType();
        var resultType = glslType.contains("|") ? glslType.split("[|]")[0] : glslType;
        return new ShaderNodeVariable(resultType, NAMESPACE, matParam.getName(),
                null, "m_");
    }

    public MaterialShaderNodeElement(@NotNull ShaderNodesContainer container, @NotNull ShaderNodeVariable variable) {
        super(container, variable);
    }

    @Override
    @FxThread
    protected @NotNull OutputShaderNodeParameter newParameter() {

        var variable = getObject();
        var container = getContainer();
        var materialDef = container.getMaterialDef();
        var materialParam = materialDef.getMaterialParam(variable.getName());

        var control = buildControl(container.getChangeConsumer(), materialParam);
        if (control == null) {
            return super.newParameter();
        }

        control.changeControlWidthPercent(0.6);
        control.setSyncHandler(this::getCurrentValue);
        control.setApplyHandler(this::applyValue);
        control.setEditObject(materialParam);

        switch (materialParam.getVarType()) {
            case Int:
            case Float: {
                control.changeControlWidthPercent(0.4);
                break;
            }
        }

        return new EditableMaterialShaderNodeParameter(this, getObject(), control);
    }

    /**
     * Notify about changed preview material.
     */
    @FxThread
    public void notifyChangedMaterial() {
        getParametersContainer().getChildren().stream()
                .filter(EditableMaterialShaderNodeParameter.class::isInstance)
                .map(EditableMaterialShaderNodeParameter.class::cast)
                .forEach(EditableMaterialShaderNodeParameter::requestLayout);
    }

    @FxThread
    private @Nullable PropertyControl<ChangeConsumer, MatParam, ?> buildControl(
        @NotNull ChangeConsumer consumer,
        @NotNull MatParam param
    ) {
        switch (param.getVarType()) {
            case Boolean: {

                final BooleanPropertyControl<ChangeConsumer, MatParam> control =
                        new BooleanPropertyControl<>(null, param.getName(), consumer);
                control.disableCheckboxOffset();

                return control;
            }
            case Float: {
                return new FloatPropertyControl<>(null, param.getName(), consumer);
            }
            case Int: {
                return new IntegerPropertyControl<>(null, param.getName(), consumer);
            }
            case Vector2: {
                return new Vector2FPropertyControl<>(null, param.getName(), consumer);
            }
            case Vector3: {
                return new Vector3FSingleRowPropertyControl<>(null, param.getName(), consumer);
            }
            case Vector4: {
                return new ColorPropertyControl<>(null, param.getName(), consumer);
            }
            case Texture2D: {
                return new Texture2DSingleRowPropertyControl<>(null, param.getName(), consumer);
            }
            case FloatArray: {
                return new FloatArrayPropertyControl<>(null, param.getName(), consumer);
            }
            case IntArray: {
                return new IntArrayPropertyControl<>(null, param.getName(), consumer);
            }
        }

        return null;
    }

    @FxThread
    private <T> T getCurrentValue(@NotNull MatParam matParam) {

        var changeConsumer = getContainer().getChangeConsumer();
        var material = changeConsumer.getPreviewMaterial();
        if (material == null) {
            return null;
        }

        var param = material.getParam(matParam.getName());
        T value = param == null ? null : unsafeCast(param.getValue());

        if (value instanceof Vector4f) {
            var vector4f = (Vector4f) value;
            return unsafeCast(new ColorRGBA(vector4f.getX(), vector4f.getY(),
                vector4f.getZ(), vector4f.getW()));
        }

        return value;
    }

    @JmeThread
    private <T> void applyValue(@NotNull MatParam matParam, @Nullable T value) {

        var changeConsumer = getContainer().getChangeConsumer();
        var material = changeConsumer.getPreviewMaterial();
        if (material == null) {
            return;
        }

        if (value == null) {
            material.clearParam(matParam.getName());
        } else {
            material.setParam(matParam.getName(), matParam.getVarType(), value);
        }
    }

    @Override
    @FxThread
    protected @NotNull String getTitleText() {
        return PluginMessages.NODE_ELEMENT_MATERIAL_PARAMETER;
    }

    @Override
    @FxThread
    protected @NotNull String getNameSpace() {
        return NAMESPACE;
    }

    @Override
    @FxThread
    public @Nullable ShaderNodeAction<?> getDeleteAction() {
        return new RemoveMaterialParamShaderNodeAction(getContainer(), getObject(),
                new Vector2f((float) getLayoutX(), (float) getLayoutY()));
    }
}
