package rlib.util;

/**
 * Набор дополнительных математических методов.
 *
 * @author JavaSaBr
 */
public final class ExtMath {

    /**
     * Конвертированное в float число PI.
     */
    public static final float PI = (float) Math.PI;

    /**
     * The value PI/2 as a float. (90 degrees)
     */
    public static final float HALF_PI = 0.5f * PI;

    /**
     * Получение арккосинуса указанного значения. Если указанное значение меньше -1, то возвратиться
     * число ПИ, если же больше 1, то вернеться 0.
     *
     * @param value интересуемое значение.
     * @return угол в радианах.
     */
    public static float acos(final float value) {

        if (-1.0F < value) {

            if (value < 1.0F) {
                return (float) Math.acos(value);
            }

            return 0.0F;
        }

        return PI;
    }

    /**
     * Returns the arc sine of a value.<br> Special cases: <ul> <li>If fValue is smaller than -1,
     * then the result is -HALF_PI. <li>If the argument is greater than 1, then the result is
     * HALF_PI. </ul>
     *
     * @param fValue The value to arc sine.
     * @return the angle in radians.
     * @see java.lang.Math#asin(double)
     */
    public static float asin(float fValue) {
        if (-1.0f < fValue) {
            if (fValue < 1.0f) {
                return (float) Math.asin(fValue);
            }

            return HALF_PI;
        }

        return -HALF_PI;
    }

    public static float atan2(float fY, float fX) {
        return (float) Math.atan2(fY, fX);
    }

    /**
     * Возвращает косинус угла указанного значения. Прямой вызов {@link Math#cos(double)}
     *
     * @param value значение угла для вычисление косинуса.
     * @return косинус указанного угла.
     * @see Math#cos(double)
     */
    public static float cos(final float value) {
        return (float) Math.cos(value);
    }

    /**
     * Получение обратного корня указанного числа.
     *
     * @param value интересуемое число.
     * @return 1 / sqrt(value).
     */
    public static float invSqrt(final float value) {
        return 1.0F / sqrt(value);
    }

    /**
     * Получение синуса указанного угла, прямой вызов {@link Math#sin(double)}.
     *
     * @param value интересуемое значение угла.
     * @return синус указанного угла.
     * @see Math#sin(double)
     */
    public static float sin(final float value) {
        return (float) Math.sin(value);
    }

    /**
     * Получение корня указанного значения, прямой вызов {@link Math#sqrt(double)}.
     *
     * @param value интересуемое значение.
     * @return корень указанного значения.
     * @see Math#sqrt(double)
     */
    public static float sqrt(final float value) {
        return (float) Math.sqrt(value);
    }

    private ExtMath() {
        throw new RuntimeException();
    }
}
