package com.okayji.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pair {
    String low;
    String high;

    public static Pair canonical(String a, String b) throws IllegalArgumentException {
        if (a.equals(b))
            throw new IllegalArgumentException();
        return (a.compareTo(b) < 0) ? new Pair(a, b) : new Pair(b, a);
    }
}
