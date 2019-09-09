package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.Image;
import com.tanmesh.splatter.wsResponseModel.ImageResponse;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-06
 * Time: 21:19
 */
public interface IImageService {
    List<ImageResponse> getImageResponseList(List<Image> imageList);

    List<Image> getImageList(String fileExtension, String postImageEncodedString);

    List<Image> getImageList(byte[] bytes, String fileExtension);

    Image getOriginalImage(BufferedImage bimg, String fileExtension);

    Image getThumbnail(Image original, BufferedImage bimg);

    Image getLowImage(Image original, BufferedImage bimg);

    Image getStdImage(Image original, BufferedImage bimg);

    int getHeight(Image original, int width);

    List<Image> getUserImageList(String profileImageUrl);
}
