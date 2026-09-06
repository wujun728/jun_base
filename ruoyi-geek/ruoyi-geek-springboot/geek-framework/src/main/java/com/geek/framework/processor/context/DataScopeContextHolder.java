package com.geek.framework.processor.context;

import java.util.ArrayDeque;
import java.util.Deque;

public class DataScopeContextHolder {
    private static ThreadLocal<Deque<String>> lookup = ThreadLocal.withInitial(ArrayDeque::new);

    private DataScopeContextHolder() {
    }

    public static void use(String dataScope) {
        lookup.get().push(dataScope);
    }

    public static String get() {
        Deque<String> deque = lookup.get();
        return deque.peek();
    }

    public static void clear() {
        Deque<String> deque = lookup.get();
        if (!deque.isEmpty()) {
            deque.pop();
            if (deque.isEmpty()) {
                lookup.remove();
            }
        }
    }

    public static void forceClear() {
        lookup.remove();
    }
}
