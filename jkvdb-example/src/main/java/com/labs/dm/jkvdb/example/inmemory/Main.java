package com.labs.dm.jkvdb.example.inmemory;

import com.labs.dm.jkvdb.DBManager;
import com.labs.dm.jkvdb.core.IStorage;

/**
 * @author daniel
 */
public class Main
{

    public static void main(String[] args)
    {
        final int COUNT = 1000000;
        long time = System.nanoTime();
        IStorage storage = DBManager.createInMemoryDatabase("test1");

        for (int i = 0; i < COUNT; i++)
        {
            storage.put(i, i);
        }
        time = System.nanoTime() - time;

        System.out.println(storage.get(100));
        System.out.println(COUNT / (time / 10e9) + " inserts/sec");
    }
}
