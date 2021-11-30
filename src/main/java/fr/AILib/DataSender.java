package fr.AILib;

import java.util.ArrayList;

/**
 * Basic class for all object that can send data to {@link DataListener}
 */
public class DataSender<T> {

    protected ArrayList<DataListener<T>> listeners = new ArrayList<>();


    /**
     * Allow to register a new listener.
     *
     * @param listener Listener to register
     * @return True if the listener has been correctly registered false otherwise.
     */
    public boolean registerListener(DataListener<T> listener) {
        if (this.listeners.contains(listener)) {
            return false;
        }
        this.listeners.add(listener);
        return true;
    }

    protected void sendToAllListeners(T data) {
        this.listeners.forEach(l -> l.receive(this, data));
    }

}
