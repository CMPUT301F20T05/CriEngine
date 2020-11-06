package com.example.criengine;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;

import com.example.criengine.Activities.MyBookActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;



@RunWith(RobolectricTestRunner.class)
public class BorrowerBooksListAdapterTest {
@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Context context = ApplicationProvider.getApplicationContext();

    @Mock
    DatabaseWrapper dbwMock;

    @Test
    public void borrowerBooksListAdapterConstructorTest() {
        ArrayList<Book> bookList = new ArrayList<>();
        BorrowerBooksListAdapter borrowerBooksListAdapter = new BorrowerBooksListAdapter(context, bookList);
        Assert.assertNotNull(borrowerBooksListAdapter);
    }

    @Test
    public void borrowerBooksListAdapterGetViewTest() {
        dbwMock.userId = "testUser";
        DatabaseWrapper dbw = new DatabaseWrapper(dbwMock);

        ArrayList<Book> bookList = new ArrayList<>();

        Book b = new Book();
        b.setTitle("Test");
        b.setBorrower("testUser");
        bookList.add(b);

        BorrowerBooksListAdapter borrowerBooksListAdapter = new BorrowerBooksListAdapter(context, bookList);

        View listView = View.inflate(context, R.layout.list_format, null);

        View adaptedListView = borrowerBooksListAdapter.getView(0, listView, null);

        Button actionButton = adaptedListView.findViewById(R.id.actionButton);
        Assert.assertEquals("Scan", actionButton.getText());
    }
}
