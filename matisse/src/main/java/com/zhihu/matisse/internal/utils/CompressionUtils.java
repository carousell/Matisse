package com.zhihu.matisse.internal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;

import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.SelectionSpec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class CompressionUtils {
    public static boolean compressImage(Context context, SelectionSpec spec, Item item) throws IOException {
        boolean isChange = false;
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), item.getContentUri());
        } catch (IOException e) {
        }

        if (bmp==null) {
            return false;
        }

        int width = item.cropWidth==null?bmp.getWidth():item.cropWidth.intValue();
        int height = item.cropHeight==null?bmp.getHeight():item.cropHeight.intValue();
        int x = (bmp.getWidth() - width) / 2;
        int y = (bmp.getHeight() - height) / 2;
        // Crop
        if (width!=bmp.getWidth() || height!=bmp.getHeight()) {
            isChange = true;
            bmp = Bitmap.createBitmap(bmp, x, y, width, height);
        }
        // Scale
        if (width>spec.maxWidth || height>spec.maxHeight) {
            double minRatio = Math.min(spec.maxWidth / width, spec.maxHeight / height);
            width = (int) (bmp.getWidth() * minRatio);
            height = (int) (bmp.getHeight() * minRatio);
            if (width != bmp.getWidth() || height != bmp.getHeight()) {
                isChange = true;
                bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
            }
        }

        if (isChange) {
            File file = createImageFile();
            item.cropUrl = file.getAbsolutePath();
            FileOutputStream fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        }

        return true;
    }

    private static File createImageFile() throws IOException {

        String imageFileName = "image-" + UUID.randomUUID().toString() + "-croped";
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        if (!path.exists() && !path.isDirectory()) {
            path.mkdirs();
        }

        return File.createTempFile(imageFileName, ".jpg", path);

    }
}
