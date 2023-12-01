package com.tanmesh.spotfood.service;

import com.google.common.collect.Lists;
import com.tanmesh.spotfood.entity.Image;
import com.tanmesh.spotfood.enums.ImageType;
import com.tanmesh.spotfood.wsResponseModel.ImageResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-06
 * Time: 21:19
 */
public class ImageService implements IImageService {

    @Override
    public List<ImageResponse> getImageResponseList(List<Image> imageList) {
        List<ImageResponse> imageResponseList = Lists.newArrayList();
        if (imageList == null) { return Lists.newArrayList(); }
        for (Image image : imageList) {
            ImageResponse imageResponse = new ImageResponse();
            imageResponse.setImageHeight(image.getImageHeight());
            imageResponse.setImageSize(image.getImageSize());
            imageResponse.setImageURL(image.getImageURL());
            imageResponse.setImageWidth(image.getImageWidth());

            imageResponseList.add(imageResponse);
        }
        return imageResponseList;
    }

    @Override
    public List<Image> getImageList(String fileExtension, String postImageEncodedString) {
        return null;
    }

    @Override
    public List<Image> getImageList(byte[] bytes, String fileExtension) {
        return null;
    }

    @Override
    public Image getOriginalImage(BufferedImage bimg, String fileExtension) {
        int width = bimg.getWidth();
        int height = bimg.getHeight();

        Image original = new Image();
        original.setImageSize(ImageType.ORIGINAL);
        original.setImageHeight(height);
        original.setImageWidth(width);
        original.setImageFileName("qwerty");
        original.setImageFileExtension(fileExtension);
        original.setImagePath("/Users/tanmesh/Downloads/");

        return original;
    }

    @Override
    // width : 150
    public Image getThumbnail(Image original, BufferedImage bimg) {
        int width = Math.min(150, original.getImageWidth());
        int height = getHeight(original, width);

        String imgFileName = original.getImageFileName() + "Thumbnail";
        String imgFilePath = original.getImagePath() + imgFileName + original.getImageFileExtension();

        File file = new File(imgFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            ImageIO.write(bimg, "jpeg", baos);
            byte[] imageBytes = baos.toByteArray();
            outputStream.write(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Image thumbnail = new Image();
        thumbnail.setImageSize(ImageType.THUMBNAIL);
        thumbnail.setImageFileName(imgFileName);
        thumbnail.setImageFileExtension(original.getImageFileExtension());
        thumbnail.setImageWidth(width);
        thumbnail.setImageHeight(height);

        return thumbnail;
    }

    @Override
    // width : 320
    public Image getLowImage(Image original, BufferedImage bimg) {
        return null;
    }

    @Override
    // width : 640
    public Image getStdImage(Image original, BufferedImage bimg) {
        return null;
    }

    @Override
    public int getHeight(Image original, int width) {
        float originalHeight = original.getImageHeight();
        float originalWidth = original.getImageWidth();
        float aspectRatio = originalHeight / originalWidth;
        int height = (int) (aspectRatio * width);
        return height;
    }

    @Override
    public List<Image> getUserImageList(String profileImageUrl) {
        return null;
    }
}
