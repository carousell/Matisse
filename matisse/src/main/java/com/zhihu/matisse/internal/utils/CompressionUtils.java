package com.zhihu.matisse.internal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.SelectionSpec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CompressionUtils {

    public static boolean compressImage(Context context, SelectionSpec spec, Item item) throws IOException {
        Bitmap bmp = null;
        try {
            bmp = Glide.with(context)
                    .load(item.getContentUri())
                    .asBitmap()
                    .atMost()
                    .fitCenter()
                    .override(spec.maxWidth.intValue(), spec.maxHeight.intValue())
                    .into(spec.maxWidth.intValue(), spec.maxHeight.intValue())
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (bmp == null) {
            return false;
        }

        // Todo: check change or not
        File file = createImageFile();
        item.cropUrl = file.getAbsolutePath();
        FileOutputStream fOut = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
        fOut.flush();
        fOut.close();

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
