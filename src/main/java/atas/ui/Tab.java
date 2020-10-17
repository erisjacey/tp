package atas.ui;

import atas.commons.core.index.Index;

/**
 * Existing tabs with an Index associated to them.
 * Index numbering starts from 0, beginning with tabs on the left of the GUI.
 */
public enum Tab {
    STUDENTS(Index.fromZeroBased(0)),
    SESSIONS(Index.fromZeroBased(1)),
    CURRENT(Index.fromZeroBased(2));

    /** Index to represent the order of the tabs */
    private final Index index;
    /**
     * Class constructor.
     *
     * @param index Index of the tab.
     */
    Tab(Index index) {
        this.index = index;
    }

    /**
     * Retrieves the Index of the enum class.
     *
     * @return Index of the tab.
     */
    public Index getIndex() {
        return this.index;
    }
}
