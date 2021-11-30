package fr.AILib;

import java.util.HashMap;
import java.util.Map;

public class Neuron extends DataSender<Float> implements DataListener<Float> {

    protected Map<DataSender<Float>, float[]> weightMap = new HashMap<>(); // first value is the weight the second is the last recorded data
    protected Map<DataSender<Float>, Boolean> dirtyMap = new HashMap<>(); // map each sender to know if it has unprocessed data waiting

    public Neuron() {

    }

    /**
     * Register senders to the neuron
     *
     * @param sender the sender to register
     * @param weight the weight of the sender
     * @return true if the sender has been successfully registered false otherwise
     */
    public boolean registerSender(DataSender<Float> sender, float weight) {
        if (sender.registerListener(this)) {
            weightMap.put(sender, new float[2]);
            weightMap.get(sender)[0] = weight;
            dirtyMap.put(sender, false);
        }
        return false;
    }

    /**
     * Performs the necessary checks to ensure that the neuron can start his computation
     *
     * @return true if the neuron can start his computation false otherwise
     */
    public boolean canCompute() {
        return !dirtyMap.containsValue(false);
    }

    /**
     * Perform computation (if possible) according to internal state then send the computed data to registered listener
     *
     * @throws IllegalStateException when computation can't be done due to missing data
     */
    public void compute() throws IllegalStateException {
        if (canCompute()) {
            float sum = 0;
            for (float[] data : weightMap.values()) {
                sum += data[0] * data[1];
            }
            // TODO: apply a function before sending instead of sending the raw sum
            this.sendToAllListeners(sum);

            dirtyMap.replaceAll((s, v) -> false);
        } else {
            throw new IllegalStateException("The neuron can't data is missing from at least one sender !");
        }
    }

    @Override
    public void receive(DataSender<Float> sender, Float data) {
        if (dirtyMap.containsKey(sender)) {
            if (!dirtyMap.get(sender)) {
                weightMap.get(sender)[1] = data;
                dirtyMap.put(sender, true);
            } else {
                throw new IllegalStateException("Data from the sender " + sender.toString() + " is already queued to be processed");
            }
        } else {
            throw new IllegalStateException("The sender " + sender.toString() + " is unknown");
        }
    }

    public void setWeight(DataSender<Float> sender, float weight) {
        weightMap.get(sender)[0] = weight;
    }

    public float getWeight(DataSender<Float> sender) {
        return weightMap.get(sender)[0];
    }
}
