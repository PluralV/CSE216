package cse216hw3;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleUtils {
    /**
     * Find and return the least element from a collection of given elements that are comparable.
     *
     * @param items: the given collection of elements
     * @param from_start: a <code>boolean</code> flag that decides how ties are broken.
     * If <code>true</code>, the element encountered earlier in the
     * iteration is returned, otherwise the later element is returned.
     * @param <T>: the type parameter of the collection (i.e., the items are all of type T).
     * @return the least element in <code>items</code>, where ties are
     * broken based on <code>from_start</code>.
     */
    public static <T extends Comparable<T>> T least(Collection<T> items, boolean from_start){
        return items.stream().
                reduce((x,y)->((x.compareTo(y)<0) ? x :
                                ((x.compareTo(y))>0) ? y :
                                        (x.compareTo(y)==0&&from_start) ? x : y))
                .orElse(null);
    }

    /**
     * Flattens a map to a list of <code>String</code>s, where each element in the list is formatted
     * as "key -> value" (i.e., each key-value pair is converted to a string in this specific format).
     *
     * @param aMap the specified input map.
     * @param <K> the type parameter of keys in <code>aMap</code>.
     * @param <V> the type parameter of values in <code>aMap</code>.
     * @return the flattened list representation of <code>aMap</code>.
     */
    public static <K, V> List<String> flatten(Map<K, V> aMap){
        return aMap.entrySet().stream()
                .flatMap(x -> Stream.of(x.getKey().toString()+" -> "+x.getValue().toString()))
                .collect(Collectors.toList());
    }


}
