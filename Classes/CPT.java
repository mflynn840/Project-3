package Classes;


import Interfaces.Assignment;
import Interfaces.RandomVariable;
import Interfaces.Value;
import java.util.Map;

public class CPT implements Interfaces.CPT {

    protected class AssignmentMap extends ArrayMap<Assignment,Distribution> {}

    private RandomVariable var;
    protected AssignmentMap table = new AssignmentMap();

    public CPT(RandomVariable var){

        this.var = var;
    }


    public RandomVariable getVariable() {
        return this.var;
    }


    public void set(Value value, Assignment assignment, double p) {
        //System.out.format("CPT.set: %s %s=%s : %f\n", assignment, this.variable, value, p);
		Distribution row = getRowForAssignment(assignment);
		if (row == null) {
			row = addRowForAssignment(assignment);
		}
		row.put(value, p);
    }

    protected Distribution getRowForAssignment(Assignment a) {

		//System.out.println(a);
		// Can't just do this, unfortunately:
		//return this.table.get(a);
		// Instead iterate through the assignments in our table until we find
		// one that matches (that is, is a subset of) the given one.
		for (Map.Entry<Assignment,Distribution> entry : table.entrySet()) {
			Assignment thisAssignment = entry.getKey();
			if (a.containsAll(thisAssignment)) {
				return entry.getValue();
			}
		}
		return null;
	}

    protected Distribution addRowForAssignment(Assignment a) {
		Distribution row = new Distribution(this.var);
		this.table.put(a, row);
		return row;
	}

    @Override
    public double get(Value value, Assignment assignment) {
        Distribution row = getRowForAssignment(assignment);
		if (row == null) {
			System.out.println("ERRRRRR");
			throw new IllegalArgumentException(assignment.toString());
		} else {
			return row.get(value);
		}
    }

    public CPT copy() {
		CPT newCPT = new CPT(this.var);
		for (Map.Entry<Assignment,Distribution> entry : this.table.entrySet()) {
			Distribution oldDist = entry.getValue();
			Distribution newDist = oldDist.copy();
			newCPT.table.put(entry.getKey(), newDist);
		}
		return newCPT;
	}
    
    public String toString() {
		return this.table.toString();
	}
}
