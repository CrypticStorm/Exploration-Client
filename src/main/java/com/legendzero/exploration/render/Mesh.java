package com.legendzero.exploration.render;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.render.IMesh;
import static org.lwjgl.opengl.GL11.*;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Mesh implements IMesh {

    private DoubleBuffer vertexBuffer;
    private int vertexSize;
    private FloatBuffer colorBuffer;
    private int colorSize;
    private int itemSize;
    private int beginMode;

    public Mesh(int itemSize, int beginMode) {
        this.itemSize = itemSize;
        this.beginMode = beginMode;
    }

    @Override
    public DoubleBuffer getVertexBuffer() {
        return this.vertexBuffer;
    }

    @Override
    public void setVertexBuffer(DoubleBuffer buffer, int size) {
        if (this.itemSize * size == buffer.limit()) {
            this.vertexBuffer = buffer;
            this.vertexSize = size;
        } else {
            throw new IllegalArgumentException("Invalid buffer dimensions.");
        }
    }

    @Override
    public int getVertexSize() {
        return this.vertexSize;
    }

    @Override
    public FloatBuffer getColorBuffer() {
        return this.colorBuffer;
    }

    @Override
    public void setColorBuffer(FloatBuffer buffer, int size) {
        if (this.itemSize * size == buffer.limit()) {
            this.colorBuffer = buffer;
            this.colorSize = size;
        } else {
            throw new IllegalArgumentException("Invalid buffer dimensions.");
        }
    }

    @Override
    public int getColorSize() {
        return this.colorSize;
    }

    @Override
    public int getItemSize() {
        return 0;
    }

    @Override
    public void setItemSize() {

    }

    @Override
    public int getBeginMode() {
        return this.beginMode;
    }

    @Override
    public void setBeginMode(int beginMode) {
        this.beginMode = beginMode;
    }

    @Override
    public void render(IExploration game) {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glVertexPointer(this.vertexSize, 0, this.vertexBuffer);
        glColorPointer(this.colorSize, 0, this.colorBuffer);

        glDrawArrays(this.beginMode, 0, this.itemSize);

        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);

    }
}
