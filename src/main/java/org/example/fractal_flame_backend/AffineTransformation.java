package org.example.fractal_flame_backend;

import org.example.fractal_flame_backend.model.Coefficients;
import org.example.fractal_flame_backend.model.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class AffineTransformation {

    List<Coefficients> generate(int count, Random random) {
        final List<Coefficients> coefficients = new LinkedList<>();

        for (int i = 0; i < count; i++) {
            double a, b, c, d, e, f;

            do {
                //a^2+d^2<1
                do {
                    a = random.nextDouble() * 2 - 1;
                    d = random.nextDouble() * 2 - 1;
                }
                while (!((pow(a, 2) + pow(d, 2)) < 1));

                //b^2+e^2<1
                do {
                    b = random.nextDouble() * 2 - 1;
                    e = random.nextDouble() * 2 - 1;
                }
                while (!((pow(b, 2) + pow(e, 2)) < 1));
            }
            while (!((pow(a, 2) + pow(b, 2) + pow(d, 2) + pow(e, 2)) < (1 + pow(a * e - b * d, 2))));

            c = random.nextDouble() * 2 - 1;
            f = random.nextDouble() * 2 - 1;

            int rc = 0, gc = 0, bc = 0, mid = 0;
            while (sqrt(pow(rc - mid, 2) + pow(gc - mid, 2) + pow(bc - mid, 2)) < 50) {
                rc = random.nextInt(255);
                gc = random.nextInt(255);
                bc = random.nextInt(255);
                mid = (rc + gc + bc) / 3;
            }

            Coefficients coefficient = new Coefficients(a, b, c, d, e, f, new Color(rc, gc, bc));
            coefficients.add(coefficient);
        }

        return coefficients;
    }

}
