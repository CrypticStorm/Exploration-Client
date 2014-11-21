package com.legendzero.exploration.render;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.render.IMesh;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Mesh implements IMesh {

    private DoubleBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private DoubleBuffer normalBuffer;
    private DoubleBuffer texCoordBuffer;
    private int itemSize;
    private int beginMode;

    public Mesh(int itemSize, int beginMode) {
        this.vertexBuffer = null;
        this.colorBuffer = null;
        this.normalBuffer = null;
        this.texCoordBuffer = null;
        this.itemSize = itemSize;
        this.beginMode = beginMode;
    }

    @Override
    public DoubleBuffer getVertexBuffer() {
        return this.vertexBuffer;
    }

    @Override
    public void setVertexBuffer(DoubleBuffer buffer, int size) {
        if (buffer.limit() % this.itemSize == 0) {
            this.vertexBuffer = buffer;
        } else {
            throw new IllegalArgumentException("Invalid buffer dimensions.");
        }
    }

    @Override
    public FloatBuffer getColorBuffer() {
        return this.colorBuffer;
    }

    @Override
    public void setColorBuffer(FloatBuffer buffer, int size) {
        if (buffer.limit() % this.itemSize == 0) {
            this.colorBuffer = buffer;
        } else {
            throw new IllegalArgumentException("Invalid buffer dimensions.");
        }
    }

    @Override
    public DoubleBuffer getNormalBuffer() {
        return this.normalBuffer;
    }

    @Override
    public void setNormalBuffer(DoubleBuffer buffer, int size) {
        if (buffer.limit() % this.itemSize == 0) {
            this.normalBuffer = buffer;
        } else {
            throw new IllegalArgumentException("Invalid buffer dimensions.");
        }
    }

    @Override
    public DoubleBuffer getTexCoordBuffer() {
        return this.texCoordBuffer;
    }

    @Override
    public void setTexCoordBuffer(DoubleBuffer buffer, int size) {
        if (buffer.limit() % this.itemSize == 0) {
            this.texCoordBuffer = buffer;
        } else {
            throw new IllegalArgumentException("Invalid buffer dimensions.");
        }
    }

    @Override
    public int getItemSize() {
        return this.itemSize;
    }

    @Override
    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
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
        if (this.vertexBuffer != null) {
            glEnableClientState(GL_VERTEX_ARRAY);
        }
        if (this.colorBuffer != null) {
            glEnableClientState(GL_COLOR_ARRAY);
        }
        if (this.normalBuffer != null) {
            glEnableClientState(GL_NORMAL_ARRAY);
        }
        if (this.texCoordBuffer != null) {
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        }

        if (this.vertexBuffer != null) {
            glVertexPointer(this.vertexBuffer.limit() / this.itemSize, 0, this.vertexBuffer);
        }
        if (this.colorBuffer != null) {
            glColorPointer(this.colorBuffer.limit() / this.itemSize, 0, this.colorBuffer);
        }
        if (this.normalBuffer != null) {
            glNormalPointer(this.normalBuffer.limit() / this.itemSize, this.normalBuffer);
        }
        if (this.texCoordBuffer != null) {
            glTexCoordPointer(this.texCoordBuffer.limit() / this.itemSize, 0, this.texCoordBuffer);
        }

        glDrawArrays(this.beginMode, 0, this.itemSize);

        if (this.vertexBuffer != null) {
            glDisableClientState(GL_VERTEX_ARRAY);
        }
        if (this.colorBuffer != null) {
            glDisableClientState(GL_COLOR_ARRAY);
        }
        if (this.normalBuffer != null) {
            glDisableClientState(GL_NORMAL_ARRAY);
        }
        if (this.texCoordBuffer != null) {
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        }
    }
}
