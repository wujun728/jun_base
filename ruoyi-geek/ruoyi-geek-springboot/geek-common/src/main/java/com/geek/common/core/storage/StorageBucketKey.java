package com.geek.common.core.storage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

import com.mybatisflex.core.exception.FlexAssert;

public class StorageBucketKey {

    private static ThreadLocal<Deque<String>> lookup = ThreadLocal.withInitial(ArrayDeque::new);

    private StorageBucketKey() {
    }

    public static void use(String storageBucketKey) {
        Deque<String> deque = lookup.get();
        if (deque == null) {
            deque = new ArrayDeque<>(1);
            lookup.set(deque);
        }
        deque.push(storageBucketKey);
    }

    public static String get() {
        Deque<String> deque = lookup.get();
        return deque != null ? deque.peek() : null;
    }

    public static void clear() {
        Deque<String> deque = lookup.get();
        if (deque != null) {
            deque.pop();
            if (deque.isEmpty()) {
                lookup.remove();
            }
        }
    }

    public static void forceClear() {
        lookup.remove();
    }

    public static void use(String storageBucketKey, Runnable runnable) {
        try {
            use(storageBucketKey);
            runnable.run();
        } finally {
            clear();
        }
    }

    public static <T> T use(String storageBucketKey, Supplier<T> supplier) {
        try {
            use(storageBucketKey);
            return supplier.get();
        } finally {
            clear();
        }
    }

    public static void setThreadLocal(ThreadLocal<Deque<String>> threadLocal) {
        FlexAssert.notNull(threadLocal, "threadLocal");
        if (threadLocal.get() == null) {
            threadLocal.set(lookup.get());
        }
        lookup = threadLocal;
    }
}
