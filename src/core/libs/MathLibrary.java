package core.libs;

import java.util.*;

public class MathLibrary implements AbstractLibrary {

    public static final int COS = -1954839524;//"Math.cos(F)F"
    public static final int SIN = -816075027;//"Math.sin(F)F"
    public static final int TAN = -157604554;//"Math.tan(F)F"
    public static final int ASIN = -628600776;//"Math.asin(F)F"
    public static final int ACOS = -1767365273;//"Math.acos(F)F"
    public static final int ATAN = 29869697;//"Math.atan(F)F"
    public static final int EXP = 75059634;//"Math.exp(F)F"
    public static final int LOG = 1726644057;//"Math.log(F)F"
    public static final int LOG10 = 1684878968;//"Math.log10(F)F"
    public static final int SQRT = -834589695;//"Math.sqrt(F)F"
    public static final int ATAN2 = -1100541095;//"Math.atan2(FF)F"
    public static final int POW = 825758419;//"Math.pow(FF)F"
    public static final int FLOOR = -942705247;//"Math.floor(F)F"
    public static final int RANDOM = -1753568278;//"Math.random(I)I"
    public static final int RANDOMIZE = 355969322;//"Math.randomize()V"
    public static final int GET_EXPONENT = -1688542787;//"Math.getExponent(F)I"
    public static final int RANDOM_MIN_MAX = 1473988063;//"Math.random(II)I"
    private static Random random;

    public void constructor() {
        random = new Random();
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case COS: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.cos(f)};
            }
            case SIN: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.sin(f)};
            }
            case TAN: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.tan(f)};
            }
            case ASIN: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.asin(f)};
            }
            case ACOS: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.acos(f)};
            }
            case ATAN: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.atan(f)};
            }
            case EXP: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.exp(f)};
            }
            case LOG: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.log(f)};
            }
            case LOG10: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.log10(f)};
            }
            case SQRT: {
                float f = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.sqrt(f)};
            }
            case ATAN2: {
                float x = ((float[]) args[0])[0];
                float y = ((float[]) args[1])[0];
                return new float[]{(float) StrictMath.atan2(y, x)};
            }
            case POW: {
                float a = ((float[]) args[0])[0];
                float b = ((float[]) args[1])[0];
                return new float[]{(float) StrictMath.pow(a, b)};
            }
            case FLOOR: {
                float a = ((float[]) args[0])[0];
                return new float[]{(float) StrictMath.floor(a)};
            }
            case RANDOM: {
                int a = ((int[]) args[0])[0];
                return new int[]{(random.nextInt() >>> 1) % a};
            }
            case RANDOMIZE: {
                random = new Random();
                return null;
            }
            case GET_EXPONENT: {
                float f = ((float[]) args[0])[0];
                return new int[]{StrictMath.getExponent(f)};
            }
            case RANDOM_MIN_MAX: {
                int min = ((int[]) args[0])[0];
                int max = ((int[]) args[1])[0];
                return new int[]{min + ((random.nextInt() >>> 1) % (max - min))};
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Math";
    }

    public void destructor() {
        random = null;
    }
}
