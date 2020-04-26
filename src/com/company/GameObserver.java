package com.company;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameObserver implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getOldValue()+"-->"+ evt.getNewValue());
    }
}
