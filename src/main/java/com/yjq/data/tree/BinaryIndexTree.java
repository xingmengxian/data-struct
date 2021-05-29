package com.yjq.data.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yjq
 * @Description 树状数组
 * @date 2021/5/29 13:59
 */
public class BinaryIndexTree<T> {
    private int size;
    private T defaultVal;
    private List<T> data;
    private Operator<T> addOperator;
    private Operator<T> subOperator;

    private int curTag;
    private int[] tag;


    public BinaryIndexTree(int size, T defaultVal, Operator<T> addOperator, Operator<T> subOperator) {
        this.size = size;
        this.defaultVal = defaultVal;
        this.addOperator = addOperator;
        this.subOperator = subOperator;

        data = new ArrayList<T>(size + 1);
        tag = new int[size + 1];
        curTag = -1;
        for (int i = 0; i < size +1; i++) {
            data.add(defaultVal);
        }
    }

    public void add(int index, T val) {
        assert index > 0 && index <= size;

        while (index < size) {
            if (tag[index] != curTag) {
                data.set(index, defaultVal);
                tag[index] = curTag;
            }

            T oldVal = data.get(index);
            T newVal = addOperator.operator(oldVal, val);
            data.set(index, newVal);
            index += lowBit(index);
        }
    }

    public T range(int l, int r) {
        assert l <= r && l > 0 && r <= size;
        return subOperator.operator(range(r), range(l - 1));
    }

    public T range(int n) {
        T result = defaultVal;
        while (n > 0) {
            if (result == null) {
                result = data.get(n);
            } else {
                result = addOperator.operator(result, data.get(n));
            }
            n -= lowBit(n);
        }
        return result;
    }

    public void reset() {
        curTag++;
    }

    @FunctionalInterface
    public interface Operator<T> {
        T operator(T t1, T t2);
    }

    private int lowBit(int index) {
        return index & -index;
    }
}
