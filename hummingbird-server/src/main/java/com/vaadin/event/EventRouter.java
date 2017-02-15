/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;

/**
 * <code>EventRouter</code> class implementing the inheritable event listening
 * model. For more information on the event model see the
 * {@link com.vaadin.event package documentation}.
 *
 * @author Vaadin Ltd.
 * @since 3.0
 */
public class EventRouter implements MethodEventSource {

    /**
     * List of registered listeners.
     */
    private LinkedHashSet<ListenerMethod> listenerList = null;

    /*
     * Registers a new listener with the specified activation method to listen
     * events generated by this component. Don't add a JavaDoc comment here, we
     * use the default documentation from implemented interface.
     */
    @Override
    public void addListener(Class<?> eventType, Object object, Method method) {
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        listenerList.add(new ListenerMethod(eventType, object, method));
    }

    /*
     * Removes all registered listeners matching the given parameters. Don't add
     * a JavaDoc comment here, we use the default documentation from implemented
     * interface.
     */
    @Override
    public void removeListener(Class<?> eventType, Object target) {
        if (listenerList != null) {
            final Iterator<ListenerMethod> i = listenerList.iterator();
            while (i.hasNext()) {
                final ListenerMethod lm = i.next();
                if (lm.matches(eventType, target)) {
                    i.remove();
                    return;
                }
            }
        }
    }

    /*
     * Removes the event listener methods matching the given given paramaters.
     * Don't add a JavaDoc comment here, we use the default documentation from
     * implemented interface.
     */
    @Override
    public void removeListener(Class<?> eventType, Object target,
            Method method) {
        if (listenerList != null) {
            final Iterator<ListenerMethod> i = listenerList.iterator();
            while (i.hasNext()) {
                final ListenerMethod lm = i.next();
                if (lm.matches(eventType, target, method)) {
                    i.remove();
                    return;
                }
            }
        }
    }

    /**
     * Removes all listeners from event router.
     */
    public void removeAllListeners() {
        listenerList = null;
    }

    /**
     * Sends an event to all registered listeners. The listeners will decide if
     * the activation method should be called or not.
     *
     * @param event
     *            the Event to be sent to all listeners.
     */
    public void fireEvent(EventObject event) {
        fireEvent(event, null);
    }

    /**
     * Sends an event to all registered listeners. The listeners will decide if
     * the activation method should be called or not.
     * <p>
     * If an error handler is set, the processing of other listeners will
     * continue after the error handler method call unless the error handler
     * itself throws an exception.
     *
     * @param event
     *            the Event to be sent to all listeners.
     * @param errorHandler
     *            error handler to use to handle any exceptions thrown by
     *            listeners or null to let the exception propagate to the
     *            caller, preventing further listener calls
     */
    public void fireEvent(EventObject event, ErrorHandler errorHandler) {
        // It is not necessary to send any events if there are no listeners
        if (listenerList != null) {

            // Make a copy of the listener list to allow listeners to be added
            // inside listener methods. Fixes #3605.

            // Send the event to all listeners. The listeners themselves
            // will filter out unwanted events.
            final Object[] listeners = listenerList.toArray();
            for (int i = 0; i < listeners.length; i++) {
                ListenerMethod listenerMethod = (ListenerMethod) listeners[i];
                if (null != errorHandler) {
                    try {
                        listenerMethod.receiveEvent(event);
                    } catch (Exception e) {
                        errorHandler.error(new ErrorEvent(e));
                    }
                } else {
                    listenerMethod.receiveEvent(event);
                }
            }

        }
    }

    /**
     * Checks if the given Event type is listened by a listener registered to
     * this router.
     *
     * @param eventType
     *            the event type to be checked
     * @return true if a listener is registered for the given event type
     */
    public boolean hasListeners(Class<?> eventType) {
        if (listenerList != null) {
            for (ListenerMethod lm : listenerList) {
                if (lm.isType(eventType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns all listeners that match or extend the given event type.
     *
     * @param eventType
     *            The type of event to return listeners for.
     * @return A collection with all registered listeners. Empty if no listeners
     *         are found.
     */
    public Collection<?> getListeners(Class<?> eventType) {
        List<Object> listeners = new ArrayList<>();
        if (listenerList != null) {
            for (ListenerMethod lm : listenerList) {
                if (lm.isOrExtendsType(eventType)) {
                    listeners.add(lm.getTarget());
                }
            }
        }
        return listeners;
    }

}
