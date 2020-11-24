package com.example.criengine.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Database.DatabaseWrapper;

/**
 * Generalized root fragment class.
 * Initializes the database wrapper that all rootFragments will require.
 */
public abstract class RootFragment extends Fragment {
    protected RootActivity root;
    protected DatabaseWrapper dbw;

    /**
     * Returns the fragment layout.
     * @return The fragment layout.
     */
    public abstract int getFragmentLayout();

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     * @return The view hierarchy associated with the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (RootActivity) getActivity();
        dbw = DatabaseWrapper.getWrapper();
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    /**
     * Default SwipeRefreshLayout listener.
     * Lasts 1.5 seconds and calls for a RootActivity refresh
     */
    protected class RefreshRootListener implements SwipeRefreshLayout.OnRefreshListener {
        SwipeRefreshLayout layout;
        RefreshRootListener(SwipeRefreshLayout layout) {
            this.layout = layout;
        }
        @Override
        public void onRefresh() {
            if(layout.isRefreshing()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dbw.notifyChanged();
                        root.refresh();
                        layout.setRefreshing(false);
                    }
                }, 1500);
            }
        }
    }
}
