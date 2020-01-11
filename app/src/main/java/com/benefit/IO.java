package com.benefit;

import com.benefit.MacroFiles.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IO {
    public static String readInputStreamToString(InputStream is) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        return writer.toString();
    }

    public static List<Category> getDatabaseFromInputStream(InputStream jsonFileInputStream) {
        List<Category> ct;
        try {
            ct = generateDatabaseFromInputStream(jsonFileInputStream);
        } catch (JSONException e) {
            e.printStackTrace();
            ct = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            ct = new ArrayList<>();
        }
        return ct;
    }

    private static List<Category> generateDatabaseFromInputStream(InputStream jsonFileInputStream)
            throws JSONException, IOException {
        JSONObject dbJsonRawObject = new JSONObject(readInputStreamToString(jsonFileInputStream));

        JSONArray sizesJsonArr = dbJsonRawObject.getJSONArray("categories");
        List<Category> ct = new ArrayList<>();
        Category category = new Category();
        for (int i = 0; i < sizesJsonArr.length(); i++) {
            category.setName((sizesJsonArr.getJSONObject(i).getString("name")));
            category.setImages(sizesJsonArr.getJSONObject(i).getString("image"));
            ct.add(category);
//            ct.setImages(images);
        }
        return ct;
    }
}
