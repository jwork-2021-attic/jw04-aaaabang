package com.anish.Monsters;

import java.util.ArrayList;
import java.util.Arrays;

public class BubbleSorter<T extends Comparable<T>> implements Sorter<T> {

    
    private ArrayList<T> a;

    @Override
    public void load(T[] a) {
        this.a = new ArrayList<>();
        this.a.addAll(Arrays.asList(a));
    }

    @Override
    public void load(T[][] matrix) {
        this.a = new ArrayList<>();
        for(T[] line:matrix){
            this.a.addAll(Arrays.asList(line));
        }
    }

    private void swap(int i, int j) {
        T temp;
        temp = a.get(i);
        a.set(i, a.get(j)) ;
        a.set(j,temp);
        plan += "" + a.get(i) + "<->" + a.get(j) + "\n";
    }

    private String plan = "";

    @Override
    public void sort() {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < a.size() - 1; i++) {
                if (a.get(i).compareTo(a.get(i+1)) > 0) {
                    swap(i, i + 1);
                    sorted = false;
                }
            }
        }
    }

    @Override
    public String getPlan() {
        return this.plan;
    }

}