package com.example.homeworkapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testJSON() throws JSONException {

        JSONObject jsonObject = new JSONObject("{\"user\":{\"name\":\"alex\",\"age\":\"18\",\"isMan\":true}}");
        jsonObject.put("t1", "23");
        jsonObject.put("t2", 1);
        jsonObject.put("t3", false);
        String s = jsonObject.toString();
        JSONObject demo = new JSONObject(s);
        System.out.println(demo);
    }
}