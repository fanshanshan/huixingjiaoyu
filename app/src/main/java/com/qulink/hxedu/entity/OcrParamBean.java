package com.qulink.hxedu.entity;

public class OcrParamBean {
    private String ImageUrl;
    private String ImageBase64;

    public String getImageBase64() {
        return ImageBase64;
    }

    public void setImageBase64(String imageBase64) {
        ImageBase64 = imageBase64;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCardSide() {
        return CardSide;
    }

    public void setCardSide(String cardSide) {
        CardSide = cardSide;
    }

    private String CardSide;
}
