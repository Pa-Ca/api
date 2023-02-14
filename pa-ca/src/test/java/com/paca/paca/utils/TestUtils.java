package com.paca.paca.utils;

import java.util.List;
import java.util.ArrayList;

public class TestUtils {
    public static <T> List<T> castList(Class<? extends T> clazz, List<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.size());

        for (int i = 0; i < rawCollection.size(); i++) {
            result.add(clazz.cast(rawCollection.get(i)));
        }

        return result;
    }
}
