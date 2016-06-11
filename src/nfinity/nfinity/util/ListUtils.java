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
    public static <T> List<T> ordered_union(List<T> list1, List<T> list2) {
        List<T> union = new ArrayList<T>();

        for(int i = 0; i < Math.max(list1.size(), list2.size()); i++) {
            if(i < list1.size()) {
                T item = list1.get(i);

                if(!union.contains(item)) {
                    union.add(item);
                }
            }

            if(i < list2.size()) {
                T item = list2.get(i);

                if(!union.contains(item)) {
                    union.add(item);
                }
            }
        }

        return union;
    }

    public static <T> List<T> paired_union(List<T> list1, List<T> list2) {
        List<T> union = new ArrayList<T>();

        for(T item : list1) {
            union.add(item);
        }

        for(T item : list2) {
            if(!union.contains(item)) {
                union.add(item);
            }
        }

        return union;
    }

    public static <T> List<T> intersect(List<T> list1, List<T> list2) {
        List<T> intersect = new ArrayList<T>();

        for(T item : paired_union(list1, list2)) {
            if(list1.contains(item) && list2.contains(item)) {
                intersect.add(item);
            }
        }

        return intersect;
    }

    public static <T> List<T> copy(List<T> list) {
        List<T> duplicate = new ArrayList<T>();
        duplicate.addAll(list);
        return duplicate;
    }
}
