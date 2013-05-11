package pl.edu.agh.amber.navi.tool;

import java.util.HashMap;
import java.util.TreeSet;

public class ContinuumMap extends HashMap<Double, Double> {

    private final TreeSet<Double> keys = new TreeSet<Double>();

    @Override
    public Double put(Double key, Double value) {
        keys.add(key);
        return super.put(key, value);
    }

    @Override
    public Double get(Object key) {
        Double value = super.get(key);
        if (value == null && key instanceof Double) {
            Double angle = (Double) key;
            Double lower = keys.lower(angle), higher = keys.higher(angle);
            Double left = (lower != null ? super.get(lower) : 0.0);
            Double right = (higher != null ? super.get(higher) : 0.0);
            // paoolo FIXME average with weights
            value = (left + right) / 2;
        }
        return value;
    }

    @Override
    public Double remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }
}
