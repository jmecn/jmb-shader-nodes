package com.jme3.shader.glsl.parser.ast;

public class AssignASTNode extends ASTNode {

    private ASTNode value;

    public ASTNode getValue() {
        return value;
    }

    public void setValue(final ASTNode value) {
        this.value = value;
    }
}