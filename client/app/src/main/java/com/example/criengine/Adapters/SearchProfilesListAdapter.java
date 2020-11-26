package com.example.criengine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Activities.MyBookActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;

import java.util.ArrayList;

public class SearchProfilesListAdapter extends ArrayAdapter<Profile> {
    private ArrayList<Profile> items;
    private Context context;
    private DatabaseWrapper dbw;

    public SearchProfilesListAdapter(@NonNull Context context, @NonNull ArrayList<Profile> profiles) {
        super(context, 0, profiles);
        this.context = context;
        this.items = profiles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        dbw = DatabaseWrapper.getWrapper();
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.profile_list, parent, false);
        }

        TextView searchUsername = view.findViewById(R.id.search_profile_user);

        final Profile profile = items.get(position);

        searchUsername.setText(profile.getUsername());

        // Opens to the book information screen when you click on a specific book.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyBookActivity.class);
                intent.putExtra("Profile", profile);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
