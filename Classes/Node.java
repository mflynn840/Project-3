package Classes;
import java.util.Set;
import java.util.ArrayList;


public class Node{

    Interfaces.RandomVariable variable;
	Set<Node> parents;
	Set<Node> children;
	Interfaces.CPT cpt;



    public Node(Interfaces.RandomVariable var){

        this.variable = var;

        this.parents = new ArraySet<Node>();
        this.children = new ArraySet<Node>();

    }

    public Set<Node> getParents(){
        return this.parents;
    }

    public Set<Node> getChildren(){
        return this.children;
    }


    public Interfaces.CPT getDistribution(){

        if(this.cpt != null){
            return this.cpt;
        }else{
            System.out.println("error, distribution has not yet been found");
            return null;

        }

    }

    public Interfaces.RandomVariable getVar(){
        return this.variable;
    }

    
}
