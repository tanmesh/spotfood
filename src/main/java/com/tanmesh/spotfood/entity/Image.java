package com.tanmesh.spotfood.entity;

import com.tanmesh.spotfood.enums.ImageType;

/**
 * Created by tanmesh
 * Date: 2019-09-06
 * Time: 21:21
 */
public class Image {
    private ImageType imageSize;
    private String imageURL;
    private String imageFileName;
    private String imageFileExtension;
    private int imageWidth;
    private int imageHeight;
    private String imagePath;

    public Image() {
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageFileExtension() {
        return imageFileExtension;
    }

    public void setImageFileExtension(String imageFileExtension) {
        this.imageFileExtension = imageFileExtension;
    }

    public ImageType getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageType imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

}
