package com.example.manage.util;

/**
 * 经纬度VO
 *
 * @author Klay
 * @date 2023/2/8
 */
public class JqPoint {
    private Double x;
    private Double y;

    public JqPoint(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
