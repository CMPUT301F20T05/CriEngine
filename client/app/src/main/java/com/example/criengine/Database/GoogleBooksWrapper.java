package com.example.criengine.Database;

import android.os.AsyncTask;
import com.example.criengine.Objects.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * A wrapper for the google API allowing the processing of ISBN codes to result in books.
 */
public class GoogleBooksWrapper {
    String API_KEY = "AIzaSyBtcP5fFkabwkm66RkqF3PnSToFNAw0cdY";


    /**
     * Instance of AsyncTask which retrieves a book object from an ISBN using the google books API.
     */
    class getBookTask extends AsyncTask<String, Integer, Book> {

        @Override
        protected Book doInBackground(String... isbns) {
            String isbn = isbns[0];
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);
                urlConnection= (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new Book();
            }
            finally {
                urlConnection.disconnect();
            }

            JSONObject jObject = null;
            try {
                jObject = new JSONObject(String.valueOf(result));

                JSONObject jBook = jObject.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");
                String title;
                String description;
                JSONArray authors;
                String authorsString = "";

                if (jBook.isNull("title")) {
                    title = null;
                } else {
                    title = jBook.getString("title");
                }
                if (jBook.isNull("description")) {
                    description = null;
                } else {
                    description = jBook.getString("description");
                }

                if (jBook.isNull("authors")) {
                    authors = null;
                } else {
                    authors = jBook.getJSONArray("authors");
                    for (int i = 0; i < authors.length(); i++) {
                        if (i == authors.length() - 1) {
                            authorsString += authors.getString(i);
                        } else {
                            authorsString += authors.getString(i) + ", ";
                        }
                    }
                }

                Book book = new Book(null, null, title, authorsString, description, null, "available");

                return book;

            } catch (JSONException e) {
                e.printStackTrace();
                return new Book();
            }
        }
    }

    /**
     * Synchonizes the request and returns a regular book.
     * @param isbn The isbn code.
     * @return A book retrieved from the code.
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Book getBook(String isbn) throws IOException, ExecutionException, InterruptedException {

        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:9780156012195");
        Book book = null;
        try {
            book = new getBookTask().execute("9780156012195").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return book;
    }


}
