package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 22.07.16.
 */
public class Article {
    protected long articleId;
    private String code;
    private String articleCode;
    private float quantityRemains;
    private float price;
    private String unitName;
    private String name;

    public Article(){
       clear();
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }


    public float getQuantityRemains() {
        return quantityRemains;
    }

    public void setQuantityRemains(float quantityRemains) {
        this.quantityRemains = quantityRemains;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public void clear(){
        articleId = 0;
        code = "";
        articleCode = "";
        quantityRemains = 0;
        price = 0;
        unitName = "";
        name = "";
    }
}
