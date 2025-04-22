package org.example.ooppr.Server;

import java.io.Serializable;

public class Data implements Serializable {
    private static final long serialVersionUID = 1L;

    private short[] colorRBG = new short[3];
    private short x;
    private short y;
    private short size;
    private byte layer;
    private Mouse mouse;


    //need pointer to Mouse.var -> Mouse *mouse
    public Data(short[] colors, byte layer, short[] positions, Mouse mouse){
        this.colorRBG = colors;
        this.x = positions[0];
        this.y = positions[1];
        this.size = positions[2];
        this.layer = layer;
        this.mouse = mouse;
    }

    public short[] getColorRBG() {
        return colorRBG;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getSize() {
        return size;
    }

    public byte getLayer() {
        return layer;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public short[][] getPackage(){
        return new short[][] {colorRBG, {x, y, size, layer}};
    }
}
