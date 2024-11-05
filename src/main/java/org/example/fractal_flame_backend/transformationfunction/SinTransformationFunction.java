package org.example.fractal_flame_backend.transformationfunction;

import org.example.fractal_flame_backend.model.Point;
import org.springframework.stereotype.Component;

import static java.lang.Math.sin;

@Component("sin")
public class SinTransformationFunction implements TransformationFunction {
    @Override
    public Point apply(double x, double y) {
        return new Point(sin(x), sin(y));
    }
}
