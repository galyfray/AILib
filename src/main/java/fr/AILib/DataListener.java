package fr.AILib;

/**
 * An interface that represent any objects that can receive data from a {@link DataSender}
 */
public interface DataListener<T> {

    /**
     * The methode used by a data sender in order to save the data to the data listener
     *
     * @param sender the object that send the Data
     * @param data   the data sent in order to be calculate or interpret
     */
    void receive(DataSender<T> sender, T data);
}
