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

    public static Category getDatabaseFromInputStream(InputStream jsonFileInputStream) {
        Category ct;
        try {
            ct = generateDatabaseFromInputStream(jsonFileInputStream);
        } catch (JSONException e) {
            e.printStackTrace();
            ct = new Category();
        } catch (IOException e) {
            e.printStackTrace();
            ct = new Category();
        }
        return ct;
    }

    private static Category generateDatabaseFromInputStream(InputStream jsonFileInputStream)
            throws JSONException, IOException {
        JSONObject dbJsonRawObject = new JSONObject(readInputStreamToString(jsonFileInputStream));

        JSONArray sizesJsonArr = dbJsonRawObject.getJSONArray("categories");
        Category ct = new Category();
        List<String> names = new LinkedList<>();
        List<String> images = new LinkedList<>();
        for (int i = 0; i < sizesJsonArr.length(); i++) {
            names.add(sizesJsonArr.getJSONObject(i).getString("name"));
            images.add(sizesJsonArr.getJSONObject(i).getString("image"));
            ct.setNames(names);
            ct.setImages(images);
        }
        return ct;
    }
}
