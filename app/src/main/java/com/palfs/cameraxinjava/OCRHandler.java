package com.palfs.cameraxinjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class OCRHandler {
    private static Bitmap _imageToBitmap(ImageProxy image) {
        Bitmap bitmap = null;
        try {
            bitmap = image.toBitmap(); // This needs 1.3.0 camera dependency
        } catch (Exception e) {
            Log.e("OCRHandler", "Error converting image to bitmap: " + e.getMessage());
        }
        return bitmap;
    }

    // Slightly complicated way of converting to BW bitmap
    // Alternative: Convert to BW using 0.5 threshold and evaluating each pixel with HSV values
    private static Bitmap _convertToBW(Bitmap bitmap) {
        Bitmap bmpMonochrome = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bmpMonochrome;
    }

    public static String extractTextFromUri(Context context, ImageProxy image) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Bitmap bitmap = _imageToBitmap(image);
        bitmap = _convertToBW(bitmap);
        InputImage source = InputImage.fromBitmap(bitmap, 0); // If necessary, change rotation degrees
        Task<Text> result = recognizer.process(source);
        result.addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text visionText) {
                // Task completed successfully
                Log.d("OCRHandler", "Text extracted successfully: " + visionText.getText());
            }
        });
        result.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Task failed with an exception
                Log.e("OCRHandler", "Error extracting text: " + e.getMessage());
            }
        });
        return result.getResult().getText();
    }
}