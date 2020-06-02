package com.qulink.hxedu.entity;

public class IndexBannerBean {


    /**
     * id : 1
     * carouselIntro : 我们测试啦
     * carouselImage : http://spark-assets.haoyanzhen.com/null
     * carouselSort : 1
     */

    private int id;
    private String carouselIntro;
    private String carouselImage;
    private int carouselSort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarouselIntro() {
        return carouselIntro;
    }

    public void setCarouselIntro(String carouselIntro) {
        this.carouselIntro = carouselIntro;
    }

    public String getCarouselImage() {
        return carouselImage;
    }

    public void setCarouselImage(String carouselImage) {
        this.carouselImage = carouselImage;
    }

    public int getCarouselSort() {
        return carouselSort;
    }

    public void setCarouselSort(int carouselSort) {
        this.carouselSort = carouselSort;
    }

    @Override
    public String toString() {
        return carouselImage;
    }
}
