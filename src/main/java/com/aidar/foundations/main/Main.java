package com.aidar.foundations.main;

import com.aidar.foundations.hashmap.MyHashMap;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, String> myHashMap = new MyHashMap<>();
        myHashMap.put(1, "one");
        myHashMap.put(2, "two");
        myHashMap.put(3, "three");
        myHashMap.put(4, "four");
        myHashMap.put(5, "five");
        myHashMap.put(6, "six");

        System.out.println(myHashMap.get(3));
        System.out.println(myHashMap.get(4));
        System.out.println(myHashMap.get(7));
    }
}
