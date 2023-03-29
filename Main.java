
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Interfaces.*;
import Parser.BIF2XMLBIF;

public class Main {

    public static void main(String[] args){

        BIF2XMLBIF parser = new BIF2XMLBIF();

        try{
            BayesianNetwork x = parser.getNetwork();

            //x.printNodes();

            //Distribution result = EnumerationInfer(x.getVariableByName("family-out"), new Classes.Assignment(), x);
            Distribution result = rejectionSampling(x.getVariableByName("family-out"), new Classes.Assignment(), x, 10);
            //System.out.println(result);
        }catch(Exception ex){}
        




    }

    public static Classes.Distribution EnumerationInfer(RandomVariable query, Classes.Assignment E, BayesianNetwork network){ //returns a distribution over x

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




    //rejection sampling produces samples from a hard to sample
    //distribution given an easier to sample one

    //generates samples from prior distribution specified by netowkr
    //then rejects those that do not match the evidence
    //the estimate P(X=x|e) is obtained by counting how often X=x occours in the remaining samples



    public static Distribution rejectionSampling(RandomVariable query, Assignment evidence, BayesianNetwork bn, int numSamples){

        
        //local vars: C, a vector of counts for each value of X, inditially 0
        Distribution counts = new Classes.Distribution(query);
        System.out.println(counts);
        //Distribution counts = new int[query.getDomain().size()];


        //for j 1->N do
        for(int i = 0; i<numSamples; i++){
            System.out.println("taking sample " + i);
            //x <- prior-sample(bn)
            Assignment iid = priorSample(bn);
            
            if(isConsistent(iid, evidence)){

                System.out.println(iid.get(query));


                System.out.println("debug 2");
                counts.set(iid.get(query), counts.get(iid.get(query)) + 1.0);
                System.out.println("debug 3");
            }
            //if x is consistent with e:
                //C[j]<-C[j]+1 where xj is the value of X in x

        }

        //normalize counts;
        counts.normalize();
        return counts;

        
    }

    public static Assignment priorSample(BayesianNetwork bn){
        Assignment x = new Classes.Assignment();

        //ArrayList<Classes.Node> nodes = bn.getNodes();
        List<RandomVariable> vars = bn.getVariablesSortedTopologically();

        Random rnj = new Random();
        for(int i = 0; i<vars.size(); i++){

            RandomVariable current = vars.get(i);
            //System.out.println("Current: " + current);
            Iterator<Value> currentDomain = current.getDomain().iterator();
            CPT distribution = bn.getDistribution(current);
            //System.out.println("debug");


            //get the value with the probability closest
            Value firstDomainValue = currentDomain.next();

            //System.out.println(distribution.get(firstDomainValue, x));

            double nextRandom = rnj.nextDouble();
            //System.out.println(nextRandom);
            if(nextRandom < distribution.get(firstDomainValue, x)){
                //System.out.println("picking true");
                x.put(current, firstDomainValue);
            }else{
                x.put(current, currentDomain.next());
                //System.out.println("picking false");
            }

            //System.out.println("Debug 2");
            //System.out.println(x);

        }

        //System.out.println("Sample generated succesfully");

        return x;

    }
    
    public static boolean isConsistent(Assignment a, Assignment evidence){

        //System.out.println("debug 1");
        for(RandomVariable x: a.keySet()){
            if(evidence.containsKey(x) && evidence.get(x) != a.get(x)){
                //System.out.println("not consistent");
                return false;
            }
        }

        //System.out.println("is consistent");
        return true;
    }
}

    /* 
    public static Distribution likeleyhoodWeighting(RandomVariable query, Assignment evidence, BayesianNetwork bn, int numSamples){


        //bn = bayesian network sepcifying joint distribution P(X1....Xn)
        //local vars: W is a vector of weighted ounts for each value of X initally 0
        int[] W = new int[query.getDomain().size()];

        //for j =1 to N
        for(int i = 0; i<numSamples; i++){
            //x, w <- Weighted-sample(bn,e)
            //W[j]<-W[j] + w where xj is the value of X in x

        //return Normalize(W)
    }

    public static EventWeight weigthedSample(BayesianNetwork bn, Assignment evidence){ //returns an event and weight:
        w <- 1;x an event with n elements, with values fixed from e
        for i =1 to n do:
            if Xi is an evidence variable with value xij in e
                then w<- wx P(Xi=xij | parents(Xi))
                else x[i] <- a random sample from P(Xi | parents(Xi))
    
        return x,w
    }

    public class EventWeight{
        int weight;
        RandomVariable var;
        Value event;

        public EventWeight(int weight, RandomVariable var, Value e){

            this.weight = weight;
            this.var =var;
            this.event = e;

        }
    }




    
        
            
    









    
}

/*
 * 
 * 1. You must design and implement a framework for representing Bayesian Networks
for random variables with finite ranges, as seen in class and in the textbook (Chapter 13, 3rd. ed. Chapter 14).


Bayesian networks (overview):

-> Model:

X: query (a propositional/probabilistic statement)

    ->The query variable

E: a set of evidence variables

    ->A set of observed variables

Y: set of unobserved (hidden) variables


The network calculated P(X | E)

    ->Compute the posterior distribution of the
    query variable given the evidence

    We need to compute P(X|e)


    Lemma 1: 

        P(X|e) = a*P(X,e) = a * sum(over yeY) P(X, e y)

    Lemma 2:
        factor the joint distribution into a product of
        the conditional probability stored at the nodes of the network

        X e Nodes(network)
        P(x1,...xn) = proctuct from i=1 to n P(x1 | partents(Xi))


    Combining Lemma 1 & 2:

        a e R = normalizing number
        P(X|e) = a*sum(y e Y)* product(i=1->n) P(xi | parents(Xi))

        ->a query can be answered by computing
        ->sums of products of conditional probabilities from
        ->the network


    Goals:
        1) implement a representation of bayesian networks
        2) interface algorithms

    
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
    
    
    
    







2. You must implement the Inference by Enumeration algorithm for exact inference
described in AIMA Section 13.3/14.4.

    Fig. 13.11/14.9 and Sec. 13.3.2/14.4.2 suggests some speedups for you to consider.



    WARNING:
        ->variables need to be set (enumerated) in topological order
        ->so that by the time you need to lookup a probability in
        its conditional probability distribution table you have the values of its parents
    
    ->compare results to calculating it by hand
    alarm and wet grass networks

PSUDOCODE:

def Enumeration(X, e, bn) returns a distribution over x

    X = query variable
    e = overserved values for variables E
    bn = a bayesian network with variables vars

    Q(X)<- a distribution over X, empty

    for each xi in X do
        Q(xi) <- Enumerate-All(vars, exi)
            where exi is e extended with X=xi
    return normalize(Q(X))

def enumerateAll(vars, e) returns a float
    if vars is empty return 1.0
    V <- FIRST(vars)
    if V is an evidence variable with value v in e
        then return P(v | parents(V)) * ENUMERATE-ALL(rest(vars), e)
    else:
        return sum(v) P(v|parents(V)) * EnumerateA;;(rest((vars)), ev)
            where ev is e extended with V=v







3. You must implement the Rejection Sampling and Likelihood Weighting algorithms
for approximate inference described in AIMA Section 13.4/14.5.


def rejection-sampling(X,e,bn,N)
    X is query variable
    e is overserved values for e in E
    bn is a bayesean network
    N is the total number of samples generated

    local vars: C, a vector of counts for each value of X, inditially 0

    for j 1->N do
        x <- prior-sample(bn)
        if x is consistent with e:
            C[j]<-C[j]+1 where xj is the value of X in x
    return Normalize(C)


def likeleyhood-weighting(X,e,bn,N) return an estimate of P(X|e):

    inputs: X= query variable
            e = overserved values for variables E
            bn = bayesian network sepcifying joint distribution P(X1....Xn)
            N = number of samples to be generated
    
            local vars: W is a vector of weighted ounts for each value of X initally 0

            for j =1 to N
                x, w <- Weighted-sample(bn,e)
                W[j]<-W[j] + w where xj is the value of X in x

            return Normalize(W)

def weigthed sample(bn, e) returns an event and weight:
    w <- 1;x an event with n elements, with values fixed from e
    for i =1 to n do:
        if Xi is an evidence variable with value xij in e
            then w<- wx P(Xi=xij | parents(Xi))
            else x[i] <- a random sample from P(Xi | parents(Xi))
    
    return x,w






4. You must implement a method for reading networks from files, following a format
known as XMLBIF.




















 * 
 * 
 */