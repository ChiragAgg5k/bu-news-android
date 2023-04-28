package com.chiragagg5k.bu_news_android;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

@RunWith(JUnit4.class)
public class UtilityUnitTest {

    @Test
    public void testConvertToTitleCase() {
        String title = "hello world";
        String expected = "Hello World";
        String actual = UtilityClass.convertToTitleCase(title);
        assertEquals(expected, actual);
    }

    @Test
    public void testArrayListToString() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("hello");
        arrayList.add("world");
        String expected = "hello, world.";
        String actual = UtilityClass.arrayListToString(arrayList);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDate() {
        long time = 1577836800000L;
        String expected = "Wednesday, 1 January";
        String actual = UtilityClass.getDate(time);
        assertEquals(expected, actual);
    }
}
