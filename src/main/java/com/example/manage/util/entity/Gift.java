package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2022/4/28
 */
@Data
@ToString
public class Gift implements Serializable {
    public Integer id;
    public Integer activityId;
    public String name;
    public Integer aFewPlace;
    public Double prob;
    public Double price;
    public String source;
    public String img;
    public String encoding;
    public String periodOfValidity;
    public String orderNumber;
    public Gift(Integer id,String name, Double prob) {
        this.id = id;
        this.name = name;
        this.prob = prob;
    }

    public Gift(Integer id, Integer activityId, String name, Integer aFewPlace, Double prob) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.aFewPlace = aFewPlace;
        this.prob = prob;
    }

    public Gift(Integer id, Integer activityId, String name, Integer aFewPlace, Double prob, Double price) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.aFewPlace = aFewPlace;
        this.prob = prob;
        this.price = price;
    }

    public Gift(Integer id, Integer activityId, String name, Integer aFewPlace, Double prob, Double price, String source) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.aFewPlace = aFewPlace;
        this.prob = prob;
        this.price = price;
        this.source = source;
    }

    public Gift(Integer id, Integer activityId, String name, Integer aFewPlace, Double prob, Double price, String source, String img) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.aFewPlace = aFewPlace;
        this.prob = prob;
        this.price = price;
        this.source = source;
        this.img = img;
    }

    public Gift(Integer id, Integer activityId, String name, Integer aFewPlace, Double prob, Double price, String source, String img, String encoding) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.aFewPlace = aFewPlace;
        this.prob = prob;
        this.price = price;
        this.source = source;
        this.img = img;
        this.encoding = encoding;
    }

    public Gift(Integer id, Integer activityId, String name, Integer aFewPlace, Double prob, Double price, String source, String img, String encoding, String periodOfValidity) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.aFewPlace = aFewPlace;
        this.prob = prob;
        this.price = price;
        this.source = source;
        this.img = img;
        this.encoding = encoding;
        this.periodOfValidity = periodOfValidity;
    }
}
