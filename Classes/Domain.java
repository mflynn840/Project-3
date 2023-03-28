package Classes;

import java.util.Collection;

import Interfaces.Value;
//import Classes.ArraySet;

/**
 * Base implementation of a Range as an ArraySet of Values. 
 * We use a ArraySet here since iteration is the main use for Ranges
 * in Bayesian network algorithms.
 */
public class Domain extends ArraySet<Value> implements Interfaces.Domain {

	public static final long serialVersionUID = 1L;

	public Domain() {
		super();
	}

	public Domain(int size) {
		super(size);
	}

	public int indexOf(Value x){
		if(this.contains(x)){
			return this.indexOf(x);
		}

		else{
			System.out.println("Value not found");
			return -1;
		}
	}

	public Domain(Value... values) {
		this();
		for (Value value : values) {
			this.add(value);
		}
	}

	public Domain(Collection<Value> collection) {
		this();
		for (Value value : collection) {
			this.add(value);
		}
	}

	public static void main(String[] argv) {
		StringValue red = new StringValue("red");
		StringValue green = new StringValue("green");
		StringValue blue = new StringValue("blue");
		Domain range = new Domain();
		range.add(red);
		range.add(green);
		range.add(blue);
		System.out.println(range);
		Domain booleans = new Domain(BooleanValue.TRUE, BooleanValue.FALSE);
		System.out.println(booleans);
	}
}
