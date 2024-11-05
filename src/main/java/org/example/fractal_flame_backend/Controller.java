package org.example.fractal_flame_backend;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

@RestController
public class Controller {

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/generate/{width}/{height}/{scale}/{id}")
    @CrossOrigin(origins = "https://fractal-flame-ui.onrender.com")
//    @CrossOrigin(origins = "http://localhost:7083")
    public Map<String, String> generateImage(
            @PathVariable int width,
            @PathVariable int height,
            @PathVariable double scale,
            @PathVariable String id
    ) {
        var start = currentTimeMillis();
        System.out.println("start at " + start);
        System.out.println("generate " + width + "x" + height + " image");

        BufferedImage image = ImageGenerator.generateImage(id, (int)(width*scale), (int)(height*scale));
        BufferedImage blurImage = blurImage(image);
        BufferedImage scaledImage = scaleImage(blurImage, width, height);

        var end = currentTimeMillis();
        System.out.println((end - start)/1000 + "s");

        String base64 = convertBufferedImageToBase64(scaledImage);
        Map<String, String> response = new HashMap<>();
        response.put("image", base64);

        return response;
    }

    public static BufferedImage blurImage(BufferedImage image) {
        float[] matrix = {
                1/16f, 2/16f, 1/16f,
                2/16f, 4/16f, 2/16f,
                1/16f, 2/16f, 1/16f
        };
        ConvolveOp convolveOp = new ConvolveOp(new Kernel(3, 3, matrix), ConvolveOp.EDGE_NO_OP, null);
        BufferedImage blurredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        convolveOp.filter(image, blurredImage);

        return blurredImage;
    }

    public BufferedImage scaleImage(Image originalImage, int targetWidth, int targetHeight) {
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return scaledImage;
    }

    @SneakyThrows
    public String convertBufferedImageToBase64(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
