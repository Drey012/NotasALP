package com.faculdade.notas.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoUtils {
    public static double arredondar(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}