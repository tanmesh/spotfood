package com.tanmesh.splatter.wsResponseModel;

import com.tanmesh.splatter.enums.ImageType;

/**
 * Created by tanmesh
 * Date: 2019-09-07
 * Time: 00:22
 */
public class ImageResponse {
    private ImageType imageSize;
    private String imageURL;
    private int imageWidth;
    private int imageHeight;

    public ImageResponse() {
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
