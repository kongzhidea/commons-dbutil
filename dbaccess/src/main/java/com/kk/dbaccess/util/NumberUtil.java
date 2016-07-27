
package com.kk.dbaccess.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;


public class NumberUtil {

    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);

    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass)
            throws IllegalArgumentException {
        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (Byte.class == targetClass) {
            long value = number.longValue();
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Byte(number.byteValue());
        } else if (Short.class == targetClass) {
            long value = number.longValue();
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Short(number.shortValue());
        } else if (Integer.class == targetClass) {
            long value = number.longValue();
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Integer(number.intValue());
        } else if (Long.class == targetClass) {
            BigInteger bigInt = null;
            if (number instanceof BigInteger) {
                bigInt = (BigInteger) number;
            } else if (number instanceof BigDecimal) {
                bigInt = ((BigDecimal) number).toBigInteger();
            }
            // Effectively analogous to JDK 8's BigInteger.longValueExact()
            if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Long(number.longValue());
        } else if (BigInteger.class == targetClass) {
            if (number instanceof BigDecimal) {
                // do not lose precision - use BigDecimal's own conversion
                return (T) ((BigDecimal) number).toBigInteger();
            } else {
                // original value is not a Big* number - use standard long conversion
                return (T) BigInteger.valueOf(number.longValue());
            }
        } else if (Float.class == targetClass) {
            return (T) new Float(number.floatValue());
        } else if (Double.class == targetClass) {
            return (T) new Double(number.doubleValue());
        } else if (BigDecimal.class == targetClass) {
            // always use BigDecimal(String) here to avoid unpredictability of BigDecimal(double)
            // (see BigDecimal javadoc for details)
            return (T) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                    number.getClass().getName() + "] to unknown target class [" + targetClass.getName() + "]");
        }
    }

    private static void raiseOverflowException(Number number, Class<?> targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }

    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        String trimmed = StringUtils.trim(text);

        if (Byte.class == targetClass) {
            return (T) Byte.valueOf(trimmed);
        } else if (Short.class == targetClass) {
            return (T) Short.valueOf(trimmed);
        } else if (Integer.class == targetClass) {
            return (T) Integer.valueOf(trimmed);
        } else if (Long.class == targetClass) {
            return (T) Long.valueOf(trimmed);
        } else if (BigInteger.class == targetClass) {
            return (T) new BigInteger(trimmed);
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(trimmed);
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(trimmed);
        } else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return (T) new BigDecimal(trimmed);
        } else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }

    public static int parseInt(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? 1 : 0;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            Long l = (Long) o;
            return l.intValue();
        }
        if (o instanceof Float) {
            return ((Float) o).intValue();
        }

        if (o instanceof Double) {
            return ((Double) o).intValue();
        }

        String s = (String) o;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long parseLong(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? 1 : 0;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            Long l = (Long) o;
            return l;
        }
        if (o instanceof Float) {
            return ((Float) o).longValue();
        }

        if (o instanceof Double) {
            return ((Double) o).longValue();
        }

        String s = (String) o;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double parseDouble(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? 1 : 0;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            Long l = (Long) o;
            return l;
        }
        if (o instanceof Float) {
            return ((Float) o).doubleValue();
        }

        if (o instanceof Double) {
            return (Double) o;
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).doubleValue();
        }

        String s = (String) o;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static BigDecimal parseBigDecimal(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? new BigDecimal(1) : new BigDecimal(0);
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }
        BigDecimal decimal = new BigDecimal(o.toString());
        return decimal;
    }

    public static Date parseDate(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof java.sql.Date) {
            return new Date(((java.sql.Date) o).getTime());
        }
        if (o instanceof java.sql.Timestamp) {
            return new Date(((java.sql.Timestamp) o).getTime());
        }
        if (o instanceof Date) {
            return (Date) o;
        }
        return null;
    }


}
