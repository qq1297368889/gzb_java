package gzb.frame.server.http;

import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.SessionIdGenerator;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class SessionManager implements Manager {
    /**
     * Get the Context with which this Manager is associated.
     *
     * @return The associated Context
     */
    @Override
    public Context getContext() {
        return null;
    }

    /**
     * Set the Context with which this Manager is associated. The Context must be set to a non-null value before the
     * Manager is first used. Multiple calls to this method before first use are permitted. Once the Manager has been
     * used, this method may not be used to change the Context (including setting a {@code null} value) that the Manager
     * is associated with.
     *
     * @param context The newly associated Context
     */
    @Override
    public void setContext(Context context) {

    }

    /**
     * @return the session id generator
     */
    @Override
    public SessionIdGenerator getSessionIdGenerator() {
        return null;
    }

    /**
     * Sets the session id generator
     *
     * @param sessionIdGenerator The session id generator
     */
    @Override
    public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {

    }

    /**
     * Returns the total number of sessions created by this manager, which is approximated as the number of active
     * sessions plus the number of expired sessions.
     *
     * @return Total number of sessions created by this manager.
     */
    @Override
    public long getSessionCounter() {
        return 0;
    }

    /**
     * Sets the total number of sessions created by this manager.
     *
     * @param sessionCounter Total number of sessions created by this manager.
     * @deprecated This will be removed in Tomcat 11
     */
    @Override
    public void setSessionCounter(long sessionCounter) {

    }

    /**
     * Gets the maximum number of sessions that have been active at the same time.
     *
     * @return Maximum number of sessions that have been active at the same time
     */
    @Override
    public int getMaxActive() {
        return 0;
    }

    /**
     * (Re)sets the maximum number of sessions that have been active at the same time.
     *
     * @param maxActive Maximum number of sessions that have been active at the same time.
     */
    @Override
    public void setMaxActive(int maxActive) {

    }

    /**
     * Gets the number of currently active sessions.
     *
     * @return Number of currently active sessions
     */
    @Override
    public int getActiveSessions() {
        return 0;
    }

    /**
     * Gets the number of sessions that have expired.
     *
     * @return Number of sessions that have expired
     */
    @Override
    public long getExpiredSessions() {
        return 0;
    }

    /**
     * Sets the number of sessions that have expired.
     *
     * @param expiredSessions Number of sessions that have expired
     */
    @Override
    public void setExpiredSessions(long expiredSessions) {

    }

    /**
     * Gets the number of sessions that were not created because the maximum number of active sessions was reached.
     *
     * @return Number of rejected sessions
     */
    @Override
    public int getRejectedSessions() {
        return 0;
    }

    /**
     * Gets the longest time (in seconds) that an expired session had been alive.
     *
     * @return Longest time (in seconds) that an expired session had been alive.
     */
    @Override
    public int getSessionMaxAliveTime() {
        return 0;
    }

    /**
     * Sets the longest time (in seconds) that an expired session had been alive.
     *
     * @param sessionMaxAliveTime Longest time (in seconds) that an expired session had been alive.
     */
    @Override
    public void setSessionMaxAliveTime(int sessionMaxAliveTime) {

    }

    /**
     * Gets the average time (in seconds) that expired sessions had been alive. This may be based on sample data.
     *
     * @return Average time (in seconds) that expired sessions had been alive.
     */
    @Override
    public int getSessionAverageAliveTime() {
        return 0;
    }

    /**
     * Gets the current rate of session creation (in session per minute). This may be based on sample data.
     *
     * @return The current rate (in sessions per minute) of session creation
     */
    @Override
    public int getSessionCreateRate() {
        return 0;
    }

    /**
     * Gets the current rate of session expiration (in session per minute). This may be based on sample data
     *
     * @return The current rate (in sessions per minute) of session expiration
     */
    @Override
    public int getSessionExpireRate() {
        return 0;
    }

    /**
     * Add this Session to the set of active Sessions for this Manager.
     *
     * @param session Session to be added
     */
    @Override
    public void add(Session session) {

    }

    /**
     * Add a property change listener to this component.
     *
     * @param listener The listener to add
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    /**
     * Change the session ID of the current session to a new randomly generated session ID.
     *
     * @param session The session to change the session ID for
     * @deprecated Use {@link #rotateSessionId(Session)}. Will be removed in Tomcat 10
     */
    @Override
    public void changeSessionId(Session session) {

    }

    /**
     * Change the session ID of the current session to a new randomly generated session ID.
     *
     * @param session The session to change the session ID for
     * @return The new session ID
     */
    @Override
    public String rotateSessionId(Session session) {
        return null;
    }

    /**
     * Change the session ID of the current session to a specified session ID.
     *
     * @param session The session to change the session ID for
     * @param newId   new session ID
     */
    @Override
    public void changeSessionId(Session session, String newId) {

    }

    /**
     * Get a session from the recycled ones or create a new empty one. The PersistentManager manager does not need to
     * create session data because it reads it from the Store.
     *
     * @return An empty Session object
     */
    @Override
    public Session createEmptySession() {
        return null;
    }

    /**
     * Construct and return a new session object, based on the default settings specified by this Manager's properties.
     * The session id specified will be used as the session id. If a new session cannot be created for any reason,
     * return <code>null</code>.
     *
     * @param sessionId The session id which should be used to create the new session; if <code>null</code>, the session
     *                  id will be assigned by this method, and available via the getId() method of the returned
     *                  session.
     * @return An empty Session object with the given ID or a newly created session ID if none was specified
     * @throws IllegalStateException if a new session cannot be instantiated for any reason
     */
    @Override
    public Session createSession(String sessionId) {
        return null;
    }

    /**
     * Return the active Session, associated with this Manager, with the specified session id (if any); otherwise return
     * <code>null</code>.
     *
     * @param id The session id for the session to be returned
     * @return the request session or {@code null} if a session with the requested ID could not be found
     * @throws IllegalStateException if a new session cannot be instantiated for any reason
     * @throws IOException           if an input/output error occurs while processing this request
     */
    @Override
    public Session findSession(String id) throws IOException {
        return null;
    }

    /**
     * Return the set of active Sessions associated with this Manager. If this Manager has no active Sessions, a
     * zero-length array is returned.
     *
     * @return All the currently active sessions managed by this manager
     */
    @Override
    public Session[] findSessions() {
        return new Session[0];
    }

    /**
     * Load any currently active sessions that were previously unloaded to the appropriate persistence mechanism, if
     * any. If persistence is not supported, this method returns without doing anything.
     *
     * @throws ClassNotFoundException if a serialized class cannot be found during the reload
     * @throws IOException            if an input/output error occurs
     */
    @Override
    public void load() throws ClassNotFoundException, IOException {

    }

    /**
     * Remove this Session from the active Sessions for this Manager.
     *
     * @param session Session to be removed
     */
    @Override
    public void remove(Session session) {

    }

    /**
     * Remove this Session from the active Sessions for this Manager.
     *
     * @param session Session to be removed
     * @param update  Should the expiration statistics be updated
     */
    @Override
    public void remove(Session session, boolean update) {

    }

    /**
     * Remove a property change listener from this component.
     *
     * @param listener The listener to remove
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

    /**
     * Save any currently active sessions in the appropriate persistence mechanism, if any. If persistence is not
     * supported, this method returns without doing anything.
     *
     * @throws IOException if an input/output error occurs
     */
    @Override
    public void unload() throws IOException {

    }

    /**
     * This method will be invoked by the context/container on a periodic basis and allows the manager to implement a
     * method that executes periodic tasks, such as expiring sessions etc.
     */
    @Override
    public void backgroundProcess() {

    }

    /**
     * Would the Manager distribute the given session attribute? Manager implementations may provide additional
     * configuration options to control which attributes are distributable.
     *
     * @param name  The attribute name
     * @param value The attribute value
     * @return {@code true} if the Manager would distribute the given attribute otherwise {@code false}
     */
    @Override
    public boolean willAttributeDistribute(String name, Object value) {
        return false;
    }

    /**
     * When an attribute that is already present in the session is added again under the same name and the attribute
     * implements {@link HttpSessionBindingListener}, should
     * {@link HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)} be
     * called followed by
     * {@link HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)}?
     * <p>
     * The default value is {@code false}.
     *
     * @return {@code true} if the listener will be notified, {@code false} if it will not
     */
    @Override
    public boolean getNotifyBindingListenerOnUnchangedValue() {
        return false;
    }

    /**
     * Configure if
     * {@link HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)} be
     * called followed by
     * {@link HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)} when
     * an attribute that is already present in the session is added again under the same name and the attribute
     * implements {@link HttpSessionBindingListener}.
     *
     * @param notifyBindingListenerOnUnchangedValue {@code true} the listener will be called, {@code
     *                                              false} it will not
     */
    @Override
    public void setNotifyBindingListenerOnUnchangedValue(boolean notifyBindingListenerOnUnchangedValue) {

    }

    /**
     * When an attribute that is already present in the session is added again under the same name and a
     * {@link HttpSessionAttributeListener} is configured for the session should
     * {@link HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)}
     * be called?
     * <p>
     * The default value is {@code true}.
     *
     * @return {@code true} if the listener will be notified, {@code false} if it will not
     */
    @Override
    public boolean getNotifyAttributeListenerOnUnchangedValue() {
        return false;
    }

    /**
     * Configure if
     * {@link HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)}
     * when an attribute that is already present in the session is added again under the same name and a
     * {@link HttpSessionAttributeListener} is configured for the session.
     *
     * @param notifyAttributeListenerOnUnchangedValue {@code true} the listener will be called, {@code
     *                                                false} it will not
     */
    @Override
    public void setNotifyAttributeListenerOnUnchangedValue(boolean notifyAttributeListenerOnUnchangedValue) {

    }
}
