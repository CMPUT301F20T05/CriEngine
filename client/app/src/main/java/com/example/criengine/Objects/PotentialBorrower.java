package com.example.criengine.Objects;

/**
 * A person who has requested a book, been accepted but not yet performed the handoff.
 */
public class PotentialBorrower {
    private String borrower;
    private boolean handoffComplete;

    /**
     * Constructor.
     * @param name The name of the person who will receive the book.
     */
    public PotentialBorrower(String name) {
        borrower = name;
        handoffComplete = false;
    }

    /**
     * Returns the name of the borrower.
     * @return Returns the name of the borrower.
     */
    public String getBorrower() {
        return borrower;
    }

    /**
     * Returns whether the handoff has been compeleted or not
     * @return True if handoff has been completed. False otherwise.
     */
    public boolean getHandOffCompelte() {
        return handoffComplete;
    }

    /**
     * The handoff has been completed.
     */
    public void setHandoffComplete() {
        handoffComplete = true;
    }
}
