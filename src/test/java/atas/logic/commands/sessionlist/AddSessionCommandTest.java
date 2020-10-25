package atas.logic.commands.sessionlist;

import static atas.testutil.Assert.assertThrows;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import atas.commons.core.GuiSettings;
import atas.commons.core.index.Index;
import atas.logic.commands.CommandResult;
import atas.logic.commands.exceptions.CommandException;
import atas.model.Model;
import atas.model.ReadOnlyUserPrefs;
import atas.model.memo.Memo;
import atas.model.session.Attributes;
import atas.model.session.IndexRange;
import atas.model.session.Session;
import atas.model.session.SessionList;
import atas.model.session.SessionName;
import atas.model.student.ReadOnlyStudentList;
import atas.model.student.Student;
import atas.model.student.StudentList;
import atas.testutil.SessionBuilder;
import javafx.collections.ObservableList;

public class AddSessionCommandTest {

    @Test
    public void constructor_nullSession_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddSessionCommand(null));
    }

    @Test
    public void execute_sessionAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingSessionAdded modelStub = new ModelStubAcceptingSessionAdded();
        Session validSession = new SessionBuilder().build();

        CommandResult commandResult = new AddSessionCommand(validSession).execute(modelStub);

        assertEquals(String.format(AddSessionCommand.MESSAGE_SUCCESS, validSession), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validSession), modelStub.sessionList);
    }

    @Test
    public void execute_duplicateSession_throwsCommandException() {
        Session validSession = new SessionBuilder().build();
        AddSessionCommand addSessionCommand = new AddSessionCommand(validSession);
        ModelStub modelStub = new ModelStubWithSession(validSession);

        assertThrows(CommandException.class,
                AddSessionCommand.MESSAGE_DUPLICATE_SESSION, () -> addSessionCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Session tut = new SessionBuilder().withSessionName("tut").build();
        Session lab = new SessionBuilder().withSessionName("lab").build();

        AddSessionCommand addTutCommand = new AddSessionCommand(tut);
        AddSessionCommand addLabCommand = new AddSessionCommand(lab);

        // same object -> returns true
        assertTrue(addTutCommand.equals(addTutCommand));

        // same session name -> returns true
        AddSessionCommand addTutCommandCopy = new AddSessionCommand(tut);
        assertTrue(addTutCommand.equals(addTutCommandCopy));

        // different types -> returns false
        assertFalse(addTutCommand.equals(1));

        // null -> returns false
        assertFalse(addTutCommand.equals(null));

        // different student -> returns false
        assertFalse(addTutCommand.equals(addLabCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getStudentListFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudentListFilePath(Path studentListFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addStudent(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudentList(ReadOnlyStudentList studentList) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyStudentList getStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasStudent(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteStudent(Student target, Index id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudent(Student target, Student editedStudent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getSessionListFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSessionListFilePath(Path sessionListFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetSessionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public SessionList getSessionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasSession(Session session) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteSession(Session target, Index id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addSession(Session session) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void enterSession(Index sessionIndex) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Attributes> getFilteredAttributesList() {
            return null;
        }

        @Override
        public void setSession(Session target, Session editedSession) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateParticipationBySessionName(SessionName sessionName, IndexRange indexRange) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePresenceBySessionName(SessionName sessionName, IndexRange indexRange) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Student> getFilteredStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Session> getFilteredSessionList() {
            return null;
        }

        @Override
        public void updateFilteredStudentList(Predicate<Student> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredSessionList(Predicate<Session> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Index getSessionId() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCurrentSessionFalse() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCurrentSessionTrue() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean returnCurrentSessionEnabledStatus() {
            return false;
        }

        @Override
        public Path getMemoFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMemoFilePath(Path memoFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Memo getMemo() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getMemoContent() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void saveMemoContent(String content) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addNoteToMemo(String note) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Index generateRandomStudentIndex() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getSessionDetails() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commit() { }

        @Override
        public void commitStudentList() { }

        @Override
        public boolean canUndoStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitSessionList() { }

        @Override
        public boolean canUndoSessionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoSessionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoSessionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoSessionList() {
            throw new AssertionError("This method should not be called.");
        }

    }

    /**
     * A Model stub that contains a single session.
     */
    private class ModelStubWithSession extends ModelStub {
        private final Session session;

        ModelStubWithSession(Session session) {
            requireNonNull(session);
            this.session = session;
        }

        @Override
        public boolean hasSession(Session session) {
            requireNonNull(session);
            return this.session.isSameSession(session);
        }
    }

    /**
     * A Model stub that always accept the session being added.
     */
    private class ModelStubAcceptingSessionAdded extends ModelStub {
        final ArrayList<Session> sessionList = new ArrayList<>();

        @Override
        public boolean hasSession(Session session) {
            requireNonNull(session);
            return sessionList.stream().anyMatch(session::isSameSession);
        }

        @Override
        public void addSession(Session session) {
            requireNonNull(session);
            sessionList.add(session);
        }

        @Override
        public ReadOnlyStudentList getStudentList() {
            return new StudentList();
        }
    }
}