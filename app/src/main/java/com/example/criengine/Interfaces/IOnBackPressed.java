package com.example.criengine.Interfaces;

/**
 * Interface is used to determine when a back press should be taken into account.
 */
public interface IOnBackPressed {
    /**
     * If you return true the back press will not be taken into account,
     * otherwise the activity will act naturally
     * @return true if your processing has priority if not false
     */
    boolean onBackPressed();
}
