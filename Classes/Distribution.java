package Classes;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import Interfaces.RandomVariable;
import Interfaces.Value;

public class Distribution extends ArrayMap<Value, Double> implements Interfaces.Distribution {

    private RandomVariable var;

    public Distribution(RandomVariable e){
        super(e.getDomain().size());
        this.var = e;

    }


    public RandomVariable getVariable() {
        return this.var;
    }


    public void set(Value value, double probability) {
        put(value, Double.valueOf(probability));
    }



    public void normalize() {
        double sum = 0.0;
		for (Double value : values()) {
			sum += value.doubleValue();
		}
		// Avoid concurrent modification exceptions by modifying the entries directly
		for (Map.Entry<Value,Double> entry : this.entrySet()) {
			entry.setValue(entry.getValue()/sum); 
		}
        
    }


    public double get(Value value) {
        Double p = super.get(value);
		if (p == null) {
			throw new IllegalArgumentException(value.toString());
		} else {
			return p.doubleValue();
		}
    }

    public Distribution copy(){
        System.out.println("WARN: unimplemented method copy");
        return new Distribution(new Classes.RandomVariable(""));
    }


}
