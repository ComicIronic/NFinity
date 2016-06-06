package nfinity.nfinity.util;

import java.util.regex.Pattern;

/**
 * Insert description here
 *
 * @author Comic
 * @since 06/06/2016 2016
 */
public class StringUtils {

    private static Pattern whitespace = Pattern.compile("[\\s]+");

    public static boolean isNullOrBlank(String s) {
        return s == null || s == "" || whitespace.matcher(s).matches();
    }
}
