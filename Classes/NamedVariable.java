package Classes;

import Interfaces.Domain;
import Interfaces.Named;
import Interfaces.RandomVariable;

/**
 * Base implementation of a RandomVariable that has a name as well
 * as its Range of Values.
 */
public class NamedVariable extends Classes.RandomVariable implements RandomVariable, Named {
	
	protected String name;
	protected Domain range;
	
	public NamedVariable(String name, Domain range) {
		super(name, range);
		this.name = name;
		this.range = range;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Domain getDomain() {
		return this.range;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/*
	 * Not sure this is needed since we don't actually compare RandomVariables
	 * to each other using equals(). So the default hashCode() implementation
	 * may be sufficient.
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	 */
	
	// Testing
	
	public static void main(String[] argv) {
		StringValue red = new StringValue("red");
		StringValue green = new StringValue("green");
		StringValue blue = new StringValue("blue");
		Classes.Domain range = new Classes.Domain();
		range.add(red);
		range.add(green);
		range.add(blue);
		NamedVariable v1 = new NamedVariable("Color", range);
		System.out.format("%s : %s\n", v1, v1.getDomain());
		Classes.Domain booleans = new Classes.Domain(BooleanValue.TRUE, BooleanValue.FALSE);
		NamedVariable v2 = new NamedVariable("IsFurry", booleans);
		System.out.format("%s : %s\n", v2, v2.getDomain());
	}

}
