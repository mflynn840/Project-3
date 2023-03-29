import java.util.Iterator;
import java.util.List;

import Interfaces.*;

import Parser.*;

public class ExactInferencer {

    public static void main(String[] args){

        XMLBIFParser parser = new XMLBIFParser();
        try{
            BayesianNetwork bn = parser.readNetworkFromFile(args[0]);
            RandomVariable query = bn.getVariableByName(args[1]);
            Assignment evidence = getEvidence(bn, args);

            System.out.println(EnumerationInfer(query, evidence, bn));

            



        }catch(Exception ex){}


    }

    public static Assignment getEvidence(BayesianNetwork bn, String[] args){

        Assignment evidence = new Classes.Assignment();

        for(int i = 2; i<args.length; i++){
            
            RandomVariable var = bn.getVariableByName(args[i]);
            i++;
            
            if(args[i].equals("true") || args[i].equals("false")){
                Iterator<Value> domainValues = var.getDomain().iterator();
            
                while(domainValues.hasNext()){
                    Value next = domainValues.next();
                    //System.out.println(next);
                    if(next.toString().equals(args[i])){
                        evidence.put(var, next);
                    }
                }

            }else{
                System.out.println("Invalid value");
            }
        }

        return evidence;
    }

    public static Classes.Distribution EnumerationInfer(RandomVariable query, Assignment E, BayesianNetwork network){ //returns a distribution over x

        //Create a distribution over the query variable to be filled out
        Classes.Distribution D = new Classes.Distribution(query);
        //System.out.println(D);

        //System.out.println(query.getDomain().size());
        Iterator<Value> xDomain = query.getDomain().iterator();


        //for each value in queries domain
        while(xDomain.hasNext()){

            //add the query variable w/ that value from its domain in to the assignment with ONLY the observered variables
            Value nextDomain = xDomain.next();
            //System.out.println(nextDomain);
            Assignment copy = E.copy();
            copy.put(query, nextDomain);

            //find the probability of that possible world using the bayesian network and add it to the distribution
            D.set(nextDomain, enumerateAll(copy, network, 0));

        }
            
        
        
        D.normalize();
        return D;
    
    

    }


    //recursivly computes sums of products of nodes from bn for a possible world
    public static double enumerateAll(Assignment evidence, BayesianNetwork bn, int currIndex){ //returns a float

        List<RandomVariable> vars = bn.getVariablesSortedTopologically();

        //if there are "no variables left" to be enumerated
        if(currIndex >= vars.size()){
            return 1.0;
        }

        RandomVariable V = vars.get(currIndex);

        //if V is an evidence variable with value v in e
            //test if V is an evidence variable...means to test if v is in e


        //if the next node in the networks variable is an observed variable
        if(evidence.containsKey(V)){

            //not sure why we need to copy this yet
            evidence = evidence.copy();

            //we are right after a junction in the tree, multiply pr of the nodes below the junction
            return bn.getProbability(V, evidence) * enumerateAll(evidence, bn, currIndex+1 );
            //return P(v | parents(V)) * ENUMERATE-ALL(rest(vars), e);
        }else{
            //return sum(v) P(v|parents(V)) * EnumerateA;;(rest((vars)), ev)
            //    where ev is e extended with V=v

            //we are at a junction in the tree, product its childrens shoots recursivly and sum them

            
            Iterator<Interfaces.Value> VDomain = V.getDomain().iterator();

            //for each value in currect verticies (random variables) domain
            double sum = 0.0;
            while(VDomain.hasNext()){

                //consider the possible world where that variable takes on that value 
                //i.e add it as evidence with each value it can take on (speeratly of course)
                evidence.put(V, VDomain.next());
                Assignment ev = evidence.copy();

                //get the probability from the node with that assignment
                double pr = bn.getProbability(V, ev);

                //continue until that branch has no more nodes to explore
                double next = enumerateAll(ev, bn, currIndex+1);
                sum+= pr*next;
            }

            return sum;
        }
    }
    
}
