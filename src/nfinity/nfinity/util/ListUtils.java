package nfinity.nfinity.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Insert description here
 *
 * @author Comic
 * @since 22/05/2016 2016
 */
public class ListUtils {
    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public static <T> List<T> copy(List<T> list) {
        List<T> duplicate = new ArrayList<T>();
        duplicate.addAll(list);
        return duplicate;
    }
}
