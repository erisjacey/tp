package seedu.address.model.attendance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.model.ReadOnlySessionList;
import seedu.address.model.attendance.exceptions.DuplicateSessionException;
import seedu.address.model.attendance.exceptions.SessionNotFoundException;
import seedu.address.model.person.Person;

import java.util.Iterator;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Represents a collection of all the sessions in the semester.
 */
public class SessionList implements Iterable<Session>, ReadOnlySessionList {

    //private final SortedSet<Session> sessions = new TreeSet<>();
    private final ObservableList<Session> sessions;
    private final ObservableList<Person> internalPersonList;


    public SessionList() {
        sessions = FXCollections.observableArrayList();
        internalPersonList = FXCollections.observableArrayList();
    }

    /**
     * Creates an SessionList using the sessions in the {@code list}
     */
    public SessionList(List<Person> list) {
        sessions = FXCollections.observableArrayList();
        internalPersonList = FXCollections.observableArrayList(list);
    }

    /**
     * Copies the session in {@code toBeCopied} in to a new list
     */
    public SessionList(ReadOnlySessionList toBeCopied) {
        sessions = FXCollections.observableArrayList();
        sessions.addAll(toBeCopied.getSessions());
        internalPersonList = toBeCopied.getInternalPersonList();
    }

    /**
     * Updates the person list of the session.
     */
    @Override
    public void updatePersonList(List<Person> list) {
        internalPersonList.clear();
        internalPersonList.addAll(list);
    }

    /**
     * Adds a session to the list.
     * The session must not already exist in the list.
     */
    public void addSession(Session session) {
        requireNonNull(session);
        if (contains(session)) {
            throw new DuplicateSessionException();
        }
/*        session = new Session(session.getSessionName(),
                session.getSessionDate(), session.getStudentList());*/
        session.initializeSession(internalPersonList);
        sessions.add(session);
    }

    /**
     * Deletes a session from the list.
     * The session must be already in the list.
     */
    public void deleteSession(Session target) {
        requireNonNull(target);
        if (!contains(target)) {
            throw new SessionNotFoundException();
        }
        sessions.removeIf(target::isSameSession);
        updateAllSessionsAfterDeleteSession(target.getSessionIndex());
    }

    /**
     * Deletes a session from the list.
     * The session must be already in the list.
     */
    public void setSession(Session oldSession, Session newSession) {
        requireNonNull(oldSession);
        requireNonNull(newSession);

        if (!contains(oldSession)) {
            throw new SessionNotFoundException();
        }

        sessions.removeIf(oldSession::isSameSession);
        sessions.add(newSession);
    }

    public Session getSessionBasedOnId(Index index) {
        return sessions.get(index.getZeroBased());
    }

    /**
     * Returns true if the collection contains an equivalent session as the given argument.
     */
    public boolean contains(Session toCheck) {
        requireNonNull(toCheck);
        return sessions.stream().anyMatch(toCheck::isSameSession);
    }

    /**
     * Updates all sessions after the deletion of a student (with a given student ID)
     */
    public void updateAllSessionsAfterDelete(Index studentId) {
        requireNonNull(studentId);
        for (Session s : sessions) {
            s.updateSessionAfterDelete(studentId, internalPersonList);
        }
    }

    /**
     * Updates all sessions after adding a student
     */
    public void updateAllSessionsAfterAdd() {
        for (Session s : sessions) {
            s.updateSessionAfterAdd(internalPersonList);
        }
    }

    /**
     * Updates all sessions after the deletion of a session (with a given session ID)
     */
    public void updateAllSessionsAfterDeleteSession(Index sessionId) {
        requireNonNull(sessionId);
        for (int i = sessionId.getZeroBased(); i < sessions.size(); i++) {
            sessions.get(i).getSessionIndex().decreaseZeroBasedIndexByOne();
        }
    }

    /**
     * Finds the session using the given {@code sessionName} and students' participation status
     * according to the {@code indexRange}
     */
    public void updateStudentParticipation(SessionName sessionName, IndexRange indexRange) {
        requireNonNull(sessionName);
        requireNonNull(indexRange);

        for (Session session: sessions) {
            if (session.getSessionName().equals(sessionName)) {
                session.updateParticipation(indexRange);
            }
        }
    }

    /**
     * Finds the session using the given {@code sessionName} and students' presence status
     * according to the {@code indexRange}
     */
    public void updateStudentPresence(SessionName sessionName, IndexRange indexRange) {
        requireNonNull(sessionName);
        requireNonNull(indexRange);

        for (Session session: sessions) {
            if (session.getSessionName().equals(sessionName)) {
                session.updatePresence(indexRange);
            }
        }
    }

    /**
     * Clears all the existing sessions in the session list.
     */
    public void clearSessions() {
        sessions.clear();
    }

    public int returnSize() {
        return sessions.size();
    }

    @Override
    public ObservableList<Session> getSessions() {
        return sessions;
    }

    @Override
    public ObservableList<Person> getInternalPersonList() {
        return internalPersonList;
    }

    @Override
    public Iterator<Session> iterator() {
        return sessions.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SessionList) {

            if (sessions.size() != ((SessionList) other).getSessions().size()) {
                return false;
            }

            Session[] testArray1 = new Session[sessions.size()];
            Session[] testArray2 = new Session[sessions.size()];

            int i = 0;
            int j = 0;
            for (Session s: sessions) {
                testArray1[i] = s;
                i++;
            }
            for (Session s: ((SessionList) other).getSessions()) {
                testArray2[j] = s;
                j++;
            }

            for (int k = 0; k < sessions.size(); k++) {
                if (!testArray1[k].isSameSession(testArray2[k])) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "SessionList{" + "sessions=" + sessions + ", internalPersonList=" + internalPersonList + '}';
    }
}