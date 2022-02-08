package myclasses;

import java.util.Arrays;


public class IntList {
    public int[] numbers = new int[0];
    public int size = 0;

    public void set(int pos, int value) {
        while (pos >= this.numbers.length) {
            this.numbers = Arrays.copyOf(this.numbers, Math.max(this.numbers.length * 2, 1));
        }
        this.numbers[pos] = value;
        this.size = Math.max(this.size, pos + 1);
    }

    public void add(int value) {
        set(this.size, value);
    }

    public int get(int pos) {
        return this.numbers[pos];
    }

    public int back() {
        return this.numbers[this.size - 1];
    }

    public int pop() {
        return this.numbers[--this.size];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void reverse() {
        int tmp;
        for (int i = 0; i < this.size / 2; i++) {
            tmp = this.numbers[i];
            this.numbers[i] = this.numbers[this.size - 1 - i];
            this.numbers[this.size - 1 - i] = tmp;
        }
    }

    // public String toString() {
    //     StringBuilder result = new StringBuilder();
    //     result.append('{');
    //     for (int i = 0; i < this.size - 1; i++) {
    //         result.append(this.numbers[i]).append(", ");
    //     }
    //     result.append(this.numbers[this.size - 1]).append('}');
    //     return result.toString();
    // }
}
