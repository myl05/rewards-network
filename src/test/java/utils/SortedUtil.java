package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedUtil {

    public static boolean isSorted(List<Integer> list, String orderType) {
        boolean isEqual = false;
        List<Integer> originalList = new ArrayList<Integer>(list);
        List<Integer> sortedList = new ArrayList<Integer>(list);
        if (orderType.toUpperCase().equals("ASC")) {
            Collections.sort(sortedList);
        } else {
            Collections.sort(sortedList, Collections.reverseOrder());
        }
        if (originalList.equals(sortedList)) {
            isEqual = true;
        }
        return isEqual;
    }

    //TODO make this generic
    public static boolean isDateSorted(List<String> list, String orderType) {
        boolean isEqual = false;
        List<String> originalList = new ArrayList<String>(list);
        List<String> sortedList = new ArrayList<String>(list);
        if (orderType.toUpperCase().equals("ASC")) {
            Collections.sort(sortedList);
        } else {
            Collections.sort(sortedList, Collections.reverseOrder());
        }
        if (originalList.equals(sortedList)) {
            isEqual = true;
        }
        return isEqual;
    }

}
