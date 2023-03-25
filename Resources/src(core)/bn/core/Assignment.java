package bn.core;

import java.util.Map;
import java.util.Set;

/**
 * An ArrayMapAssignment is mapping (a set of assignments) from RandomVariables to
 * Values from their Ranges.
 */
public interface Assignment extends Map<RandomVariable,Value> {
	
	/**
	 * Return a Set view of the RandomVariables in this ArrayMapAssignment.
	 * @see Map.keySet()
	 */
	public Set<RandomVariable> variableSet();
	
	/**
	 * Return true if this ArrayMapAssignment contains all the assignments
	 * in the given other ArrayMapAssignment. That is, the other ArrayMapAssignment is
	 * a subset of this one (or they are equal).
	 */
	public boolean containsAll(Assignment other);
	
	/**
	 * Return a shallow copy of this ArrayMapAssignment (that is, an ArrayMapAssignment that
	 * contains the same assignments without copying the RandomVariables or
	 * Values).
	 */
	public Assignment copy();

}
