package org.example.fractal_flame_backend.transformationfunction;

import org.example.fractal_flame_backend.model.Point;
import org.springframework.stereotype.Component;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

@Component("heart")
public class HeartTransformationFunction implements TransformationFunction {
    @Override
    public Point apply(double x, double y) {
        double r2 = pow(x, 2) + pow(y, 2);
        var sqrt = atan2(x, y) * sqrt(r2);
        return new Point(sqrt(r2) * sin(sqrt), -sqrt(r2) * cos(sqrt));
    }
}
