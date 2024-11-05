package org.example.fractal_flame_backend.transformationfunction;

import org.example.fractal_flame_backend.model.Point;

public interface TransformationFunction {
    Point apply(double x, double y);
}
