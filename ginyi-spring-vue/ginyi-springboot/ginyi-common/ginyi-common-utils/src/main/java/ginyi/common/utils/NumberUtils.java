package ginyi.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

    public static double round(double number, int decimalPlaces) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(number));
        bigDecimal = bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

}
