package com.czipperz.controllerHandler;

import java.util.EventListener;

/**
 * Created by czipperz on 3/3/15.
 */
public interface CControllerEventListener extends EventListener {
    /**
     * This event will be called no matter what, whilst {@link com.czipperz.controllerHandler.CControllerEventListener#controllerAxisEvent(CControllerEvent),
     * for example, will only be called if the event is of the proper type.}
     * @param event
     */
    public void controllerEvent(CControllerEvent event);
    public void controllerButtonEvent(CControllerEvent event);
    public void controllerAxisEvent(CControllerEvent event);
    public void controllerXAxisEvent(CControllerEvent event);
    public void controllerYAxisEvent(CControllerEvent event);
    public void controllerPovXEvent(CControllerEvent event);
    public void controllerPovYEvent(CControllerEvent event);
}
