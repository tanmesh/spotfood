package com.tanmesh.spotfood.scrachpad;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

public class FileToBase64StringConversionUnitTest {
    private void demo(String encodedImgFilePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(encodedImgFilePath));

        String encodedImg = Base64.getEncoder().encodeToString(fileContent);

        File outputFile = new File("/Users/tanmesh/Desktop/tmp/demo.jpg");

        byte[] decodedBytes = Base64.getDecoder().decode(encodedImg);

        FileUtils.writeByteArrayToFile(outputFile, decodedBytes);
    }

    public static void main(String[] args) {
        FileToBase64StringConversionUnitTest obj = new FileToBase64StringConversionUnitTest();
        Scanner sc = new Scanner(System.in);

        String encodedImgFilePath = sc.next();
        try {
            obj.demo(encodedImgFilePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

