package org.example.fractal_flame_backend.transformationfunction;

import org.example.fractal_flame_backend.model.Point;
import org.springframework.stereotype.Component;

import static java.lang.Math.pow;

@Component("sphere")
public class SphereTransformationFunction implements TransformationFunction {
    @Override
    public Point apply(double x, double y) {
        double r2 = pow(x, 2) + pow(y, 2);
        return new Point((x / r2), (y / r2));
    }
}
