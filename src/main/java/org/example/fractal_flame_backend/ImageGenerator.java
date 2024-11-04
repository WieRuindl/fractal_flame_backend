package org.example.fractal_flame_backend;

import org.example.fractal_flame_backend.model.Coefficients;
import org.example.fractal_flame_backend.model.Pixel;
import org.example.fractal_flame_backend.model.Point;
import org.example.fractal_flame_backend.model.Size;

import java.util.List;
import java.util.Random;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class ImageGenerator {

    static Pixel[][] generateImage(String id, Size size) {

        Random random = new Random(id.hashCode());
        List<Coefficients> affineTransformations = new AffineTransformation().generate(20, random);

        Pixel[][] pixelMap = new Pixel[size.width()][size.height()];
        
        for (int y = 0; y < size.height(); y++) {
            for (int x = 0; x < size.width(); x++) {
                pixelMap[x][y] = new Pixel();
            }
        }

        double yMax = 1,                                    yMin = -yMax;
        double xMax = (double)size.height() / size.width(), xMin = -xMax;

        double newX = random.nextDouble() * (xMax - xMin) + xMin;
        double newY = random.nextDouble() * (yMax - yMin) + yMin;

        int countOfStepsScaleModifier = 10;

        var iterations = size.width() * size.height() * countOfStepsScaleModifier;
        for (int step = -20; step < iterations; step++) {

            int i = random.nextInt(affineTransformations.size());

            newX = affineTransformations.get(i).a() * newX + affineTransformations.get(i).b() * newY + affineTransformations.get(i).c();
            newY = affineTransformations.get(i).d() * newX + affineTransformations.get(i).e() * newY + affineTransformations.get(i).f();

            Point p = sphere(newX, newY);
            newX = p.x();
            newY = p.y();

            if (step >= 0
                    && xMin <= newX && newX <= xMax
                    && yMin <= newY && newY <= yMax
            ) {

                int x1 = (int)(size.width() * (1 - ((xMax - newX) / (xMax - xMin))));
                int y1 = (int)(size.height() * (1 - ((yMax - newY) / (yMax - yMin))));

                if (x1 < size.width() && y1 < size.height()) {
                    var pixel = pixelMap[x1][y1];
                    if (pixel.counter == 0) {
                        pixel.r = affineTransformations.get(i).color().r();
                        pixel.g = affineTransformations.get(i).color().g();
                        pixel.b = affineTransformations.get(i).color().b();
                    } else {
                        pixel.r = (pixel.r + affineTransformations.get(i).color().r()) / 2;
                        pixel.g = (pixel.g + affineTransformations.get(i).color().g()) / 2;
                        pixel.b = (pixel.b + affineTransformations.get(i).color().b()) / 2;
                    }
                    pixel.counter++;
                }
            }
        }

        double maxValue = 0.0;
        for (int y = 0; y < size.height(); y++) {
            for (int x = 0; x < size.width(); x++) {
                if (pixelMap[x][y].counter != 0) {
                    pixelMap[x][y].normal = log10(pixelMap[x][y].counter);
                    maxValue = max(maxValue, pixelMap[x][y].normal);
                }
            }
        }

    double gamma = 2.2;
    double correctionValue = 1.0 / gamma;
        for (int y = 0; y < size.height(); y++) {
            for (int x = 0; x < size.width(); x++) {
                Pixel pixel = pixelMap[x][y];

                pixel.normal = pixel.normal / maxValue;
                double multiplier = pow(pixel.normal, correctionValue);

                pixel.r = (int)(pixel.r * multiplier);
                pixel.g = (int)(pixel.g * multiplier);
                pixel.b = (int)(pixel.b * multiplier);
            }
        }

        return pixelMap;
    }

    static Point sphere(double x, double y) {
        double r2 = pow(x, 2) + pow(y, 2);
//        return new Point((x / r2), (y / r2));
//         return new Point(sin(x), sin(y));
//      return new Point(x*sin(r2)-y*cos(r2), x*cos(r2)*y*sin(r2));
//        return new Point(atan2(x, y) / Math.PI, sqrt(r2) - 1);
        var sqrt = atan2(x, y) * sqrt(r2);
        return new Point(sqrt(r2) * sin(sqrt), -sqrt(r2) * cos(sqrt));
    }
}
