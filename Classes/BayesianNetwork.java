package Classes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;



import Interfaces.Assignment;
import Interfaces.CPT;
import Interfaces.RandomVariable;

public class BayesianNetwork implements Interfaces.BayesianNetwork{

    //Adjacency matrix representation

    ArrayList<Node> nodes;

    public BayesianNetwork(){

        this.nodes = new ArrayList<Node>();

    }

    public BayesianNetwork(Set<RandomVariable> x){

        Iterator<RandomVariable> xi = x.iterator();
        while(xi.hasNext()){
            this.nodes.add(new Node(xi.next()));
        }
    }


    public void addVerticy(Node V){

        this.nodes.add(V);

    }

    /** Make v1 a parent of v2 */
    public void addEdge(Node v1, Node v2){

        if(!existsEdge(v1, v2) && !existsEdge(v2, v1)){
            v2.parents.add(v1);
        }else{
            System.out.println("Tried to add an invalid edge");
        }

    }

    /** True if exists edge  v1 -> v2 */
    public boolean existsEdge(Node v1, Node v2){

        return v2.parents.contains(v1);
        
    }


    public void add(RandomVariable var) {
        this.nodes.add(new Node((Classes.RandomVariable) var));
    }


    public void connect(RandomVariable var, Set<RandomVariable> parents, CPT cpt) {

		Node node = getNodeForVariable(var);
		node.parents = new ArraySet<Node>(parents.size());
		for (RandomVariable pvar : parents) {
			Node pnode = getNodeForVariable(pvar);
			node.parents.add(pnode);
			pnode.children.add(node);
		}

		node.cpt = cpt;
	}



    private Node getNodeForVariable(RandomVariable var) {
        for(int i = 0; i<this.nodes.size(); i++){
            if(this.nodes.get(i).getVar().equals(var)){
                return this.nodes.get(i);
            }
        }

        System.out.println("Error: node not found");
        return null;
    }


    public Set<RandomVariable> getVariables() {
        Set<RandomVariable> vars = new ArraySet<>();

        for(int i = 0; i<this.nodes.size(); i++){
            vars.add(this.nodes.get(i).getVar());
        }

        return vars;
    }

    public List<RandomVariable> getVariablesSortedTopologically() {
		// ``L <- Empty list that will contain the sorted nodes''
		List<RandomVariable> L = new ArrayList<RandomVariable>(nodes.size());
		// ``S <- Set of all nodes with no outgoing edges''
		Set<Node> S = new ArraySet<Node>(nodes.size());
		for (Node node : nodes) {
			if (node.children.isEmpty()) {
				S.add(node);
			}
		}
		// Can't mark nodes visited; instead keep as a set
		Set<Node> visited = new ArraySet<Node>(nodes.size());
		// ``for each node n in S do''
		for (Node n : S) {
			// ``visit(n)''
			visit(n, L, visited);
		}
		return L;
	}

    protected void visit(Node n, List<RandomVariable> L, Set<Node> visited) {
		// ``if n has not been visited yet''
		if (!visited.contains(n)) {
			// ``mark n as visited''
			visited.add(n);
			// ``for each node m with an edge from m to n do''
			for (Node m : nodes) {
				if (m.children.contains(n)) {
					// ``visit(m)''
					visit(m, L, visited);
				}
			}
			// ``add n to L''
			L.add(n.variable);
		}
	}


    public Set<RandomVariable> getParents(RandomVariable var) {

        Set<RandomVariable> parents = new ArraySet<>();

        //find the node that represents var
        for(int i = 0; i<this.nodes.size(); i++){

            if(nodes.get(i).getVar().equals(var)){

                //add its parents
                Iterator<Node> nParents = nodes.get(i).getParents().iterator();

                while(nParents.hasNext()){
                    parents.add(nParents.next().getVar());
                }
                
            }
        }

        if(parents.size() == 0) System.out.println("No parents found");
        return parents;
        

    }


    public Set<RandomVariable> getChildren(RandomVariable var) {

        Set<RandomVariable> children = new ArraySet<>();

        //find the node that represents var
        for(int i = 0; i<this.nodes.size(); i++){

            if(nodes.get(i).getVar().equals(var)){

                //add its parents
                Iterator<Node> nChildren = nodes.get(i).getChildren().iterator();

                while(nChildren.hasNext()){
                    children.add(nChildren.next().getVar());
                }
                
            }
        }

        if(children.size() == 0) System.out.println("No parents found");
        return children;
        
    }

    public void printNodes(){
        for(int i = 0; i<this.nodes.size(); i++){
            System.out.println(this.nodes.get(i));
        }
    }

    


    public double getProbability(RandomVariable X, Assignment e) {

        Node node = getNodeForVariable(X);
		Interfaces.Value value = e.get(X);
		double result = node.cpt.get(value, e);
		//trace("BayesianNetwork.getProb: result=" + result);
		return result;

    }

    public CPT getDistribution(RandomVariable i){
        return this.getNodeForVariable(i).getDistribution();
    }

    public ArrayList<Node> getNodes(){
        return this.nodes;
    }

    public void setProbability(RandomVariable X, Assignment e, double p) {

        Node node = getNodeForVariable(X);
		Interfaces.Value value = e.get(X);
		node.cpt.set(value, e, p);
    }


    public RandomVariable getVariableByName(String name) {
        for(int i = 0; i<this.nodes.size(); i++){
            if(this.nodes.get(i).getVar().getName().equals(name)){
                return this.nodes.get(i).getVar();
            }
        }

        System.out.println("Not found");
        return null;
    }

    @Override
	public BayesianNetwork copy() {
		Set<RandomVariable> variables = this.getVariables();
		// New network with the same variables
		BayesianNetwork newNetwork = new BayesianNetwork(variables);
		// Copy the topology
		for (RandomVariable var : variables) {
			Node node = this.getNodeForVariable(var);
			Set<Node> parents = node.parents;
			Set<RandomVariable> newParents = new ArraySet<RandomVariable>(parents.size());
			for (Node parentNode : parents) {
				RandomVariable parentVar = parentNode.variable;
				newParents.add(parentVar);
			}
			// Copy CPT
			CPT newCPT = node.cpt.copy();
			// Connect
			newNetwork.connect(var, newParents, newCPT);
		}
		return newNetwork;
	}

    

    
}


/*
What is a bayesian network?:

A directed graps with verticies (nodes) vi e V and directed edges
A node = ->a random variable
        ->a probability distribution

Root nodes store the prior probability distribution of their random variable
Non root nodes store the conditional probability distribution given 
values of of its parents: P(Xi | parents(Xi))

V = a set of random variable nodes (the features of the world)
    E is a subset of V = set of evidence variables
        e in E is an evidence variable:

    what are the elements of V?
        ->Domain: possible values it can take
        ->Name
        ->Probability of truth (a distribution) = :
            if(is a root node):
                the prior probability distribution over their variable
            else:
                conditional probability distribution of their random variable given
                the values of its parents:
                P(Xi | parents(Xi))
            

What is the infrerence problem:

compute P(X| e)
X = query variable 
e is an assignment of values to evidence variables

The solution is a posterior distribution
for the query variable given its evidence

    what does that mean?

        for each possible value of the query variable
        its the probability that the query variable takes on that value given the evidence

    
Representing Distributions:

    ->a mapping from values of a variable to probabilities
    ->each probability is the probability that it takes on that value

    its a function that takes a random variable, and a value from its domain
    and returns the probability that the variable will take on that value

*/