package com.czipperz.controllerHandler;/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import net.java.games.input.ControllerEnvironment;

import java.util.ArrayList;

/**
 * The collection of controllers currently connected.
 *
 * @author Kevin Glass
 */
public class CControllers {
    /** The controllers available */
    private static ArrayList<CJInputController> controllers = new ArrayList<CJInputController>();
    /** The number of controllers */
    private static int controllerCount;

    /** The current list of events */
    private static ArrayList<CControllerEvent> events = new ArrayList<CControllerEvent>();
    /** The current event */
    private static CControllerEvent event;

    /** Whether controllers were created */
    private static boolean created;

    private static ArrayList<CControllerEventListener> listeners;

    /**
     * Initialise the controllers collection
     *
     * @throws java.lang.Exception Indicates a failure to initialise the controller library.
     */
    public static void create() throws Exception {
        if (created)
            return;

        ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();

        net.java.games.input.Controller[] found = env.getControllers();
        ArrayList<net.java.games.input.Controller> lollers = new ArrayList<net.java.games.input.Controller>();
        for(net.java.games.input.Controller c : found) {
            lollers.add(c);
        }
        lollers.stream().filter(c -> (!c.getType().equals(net.java.games.input.Controller.Type.KEYBOARD))
                && (!c.getType().equals(net.java.games.input.Controller.Type.MOUSE)))
                .forEachOrdered(CControllers::createController);

        created = true;
    }

    /**
     * Utility to create a controller based on its potential sub-controllers
     *
     * @param c The controller to add
     */
    private static void createController(net.java.games.input.Controller c) {
        net.java.games.input.Controller[] subControllers = c.getControllers();
        if (subControllers.length == 0) {
            CJInputController controller = new CJInputController(controllerCount,c);

            controllers.add(controller);
            controllerCount++;
        } else {
            for ( net.java.games.input.Controller sub : subControllers ) {
                createController(sub);
            }
        }
    }

    /**
     * Get a controller from the collection
     *
     * @param index The index of the controller to retrieve
     * @return The controller requested
     */
    public static CController getController(int index) {
        return (CController) controllers.get(index);
    }

    /**
     * Retrieve a count of the number of controllers
     *
     * @return The number of controllers available
     */
    public static int getControllerCount() {
        return controllers.size();
    }

    /**
     * Poll the controllers available. This will both update their state
     * and generate events that must be cleared.
     */
    public static void poll() {
        for (int i=0;i<controllers.size();i++) {
            getController(i).poll();
        }
    }

    /**
     * @return True if com.czipperz.controllerHandler.Controllers has been created
     */
    public static boolean isCreated() {
        return created;
    }

    /**
     * Destroys any resources used by the controllers
     */
    public static void destroy() {
// 		FIXME! not currently possible to destroy a controller

//		if (!created)
//			return;
//		created = false;
//
//		// nuke each controller
//		for (int i=0;i<controllers.size();i++) {
//			//
//		}
//
//		// cleanup
//		event = null;
//		events.clear();
//		controllers.clear();
//		controllerCount = 0;
    }

    public CControllerEvent getEvent() {
        return event;
    }

    /**
     * Clear any events stored for the controllers in this set
     */
    public static void clearEvents() {
        events.clear();
    }

    /**
     * Move to the next event that has been stored.
     *
     * @return True if there is still an event to process
     */
    public static boolean next() {
        if (events.size() == 0) {
            event = null;
            return false;
        }

        event = events.remove(0);

        return event != null;
    }


    public ArrayList<CControllerEvent> getEvents() {
        return events;
    }

    public ArrayList<CControllerEventListener> getListeners() {
        return listeners;
    }

    public void addListener(CControllerEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Add an event to the stack of events that have been caused
     *
     * @param event The event to add to the list
     */
    static void addEvent(CControllerEvent event) {
        if (event != null) {
            events.add(event);
            listeners.forEach(c -> c.controllerEvent(event));
            if (event.isAxis()) listeners.forEach(c -> c.controllerAxisEvent(event));
            if (event.isButton()) listeners.forEach(c -> c.controllerButtonEvent(event));
            if (event.isPovX()) listeners.forEach(c -> c.controllerPovXEvent(event));
            if (event.isPovY()) listeners.forEach(c -> c.controllerPovYEvent(event));
            if (event.isXAxis()) listeners.forEach(c -> c.controllerXAxisEvent(event));
            if (event.isYAxis()) listeners.forEach(c -> c.controllerYAxisEvent(event));
        }
    }
}
