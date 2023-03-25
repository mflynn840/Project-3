package Classes;


import java.util.Set;

import Interfaces.RandomVariable;
import Interfaces.Value;
import Interfaces.Domain;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * An ArrayMapAssignment is mapping (a set of assignments) from RandomVariables to
 * Values from their Ranges.
 */


public class Assignment extends ArrayMap<RandomVariable,Value> implements Interfaces.Assignment{


    public Assignment() {
		super();
	}
	
	public Assignment(RandomVariable X1, Value x1) {
		this();
		this.put(X1, x1);
	}

    public Assignment(RandomVariable[] Xi, Value[] xi){
        for(int i = 0; i<xi.length; i++){
            this.put(Xi[i], xi[i]);
        }
    }

    public Set<RandomVariable> variableSet() {
        return this.keySet();
    }



    	/**
	 * Return a List of Assignments, one for each possible combination of the
	 * given Set of Variables.
	 */
	public static List<Assignment> allPossibleAssignments(Set<RandomVariable> variables) {
		ArrayList<RandomVariable> varlist = new ArrayList<RandomVariable>(variables);
		return allPossibleAssignments(varlist);
	}
	
	/**
	 * Return a List of Assignments, one for each possible combination of the
	 * given List of Variables.
    */

    public static List<Assignment> allPossibleAssignments(List<RandomVariable> variables) {
		if (variables.isEmpty()) {
			// Base case: empty assignment
			ArrayList<Assignment> result = new ArrayList<Assignment>(1);
			result.add(new Assignment());
			return result;
		} else {
			// RecursiveNoCycleCheck case
			RandomVariable X = variables.get(0);
			Domain XDomain = X.getDomain();
			ArrayList<Assignment> results = new ArrayList<Assignment>(XDomain.size());
			for (Value x : XDomain) {
				for (Assignment a : allPossibleAssignments(variables.subList(1, variables.size()))) {
					Assignment newa = a.copy();
					newa.put(X, x);
					results.add(newa);
				}
			}
			return results;
		}
	}


    public Assignment copy(){
        Assignment newA = new Assignment();


        Iterator<RandomVariable> keys = this.keySet().iterator();
        Iterator<Value> values = this.values().iterator();

        while(keys.hasNext()){

            newA.put(keys.next(), values.next());

        }

        return newA;

    }

    public boolean containsAll(Interfaces.Assignment other) {
		Set<Map.Entry<RandomVariable,Value>> ourEntries = this.entrySet();
		Set<Map.Entry<RandomVariable,Value>> theirEntries = other.entrySet();
		return ourEntries.containsAll(theirEntries);
	}


    
}


//Inherited methods:
/*size(), isEmpty(), containsKey(Object key), containsValue(Object value), get(Object key)
put(RandomVariable key, Value value), remove(Object key)
putAll(Map<? extends RandomVariable, ? extends Value> m), clear(), keySet(), values()
entrySet() 
variableSet() 
containsAll(), copy() */
