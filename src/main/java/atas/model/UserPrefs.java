package atas.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import atas.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path studentListFilePath = Paths.get("data" , "studentlist.json");
    private Path sessionListFilePath = Paths.get("data", "sessionlist.json");

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setStudentListFilePath(newUserPrefs.getStudentListFilePath());
        setSessionListFilePath(newUserPrefs.getSessionListFilePath());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getStudentListFilePath() {
        return studentListFilePath;
    }

    public void setStudentListFilePath(Path studentListFilePath) {
        requireNonNull(studentListFilePath);
        this.studentListFilePath = studentListFilePath;
    }

    public Path getSessionListFilePath() {
        return sessionListFilePath;
    }

    public void setSessionListFilePath(Path sessionListFilePath) {
        requireNonNull(sessionListFilePath);
        this.sessionListFilePath = sessionListFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
                && studentListFilePath.equals(o.studentListFilePath)
                && sessionListFilePath.equals(o.sessionListFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, studentListFilePath, sessionListFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + studentListFilePath);
        sb.append("\nLocal session data file location : " + sessionListFilePath);
        return sb.toString();
    }

}
