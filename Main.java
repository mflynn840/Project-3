import Classes.Node;
import Classes.RandomVariable;
import java.util.ArrayList;
import java.util.Iterator;

import Classes.BayesianNetwork;

public class Main {

    public static void main(String[] args){




    }

    public Classes.Distribution Enumeration(RandomVariable query, Classes.Assignment E, BayesianNetwork network){ //returns a distribution over x

        //Q(X)<- a distribution over X, empty
        Classes.Distribution X = new Classes.Distribution(query);
        Iterator<Interfaces.Value> xDomain = query.getDomain().iterator();

        //for each xi in X do
        while(xDomain.hasNext()){
            //Q(xi) <- Enumerate-All(vars, exi)
            X.put(xDomain.next(), enumerateAll(vars, exi));
            //where exi is e extended with X=xi
        }
            
                
        X.normalize();
        return X;
    
    

    }

    public static float enumerateAll(vars, e){ //returns a float
        if(vars.isEmpty()){
            return 1.0;
        }
        Randomvariable V = vars.get(0);

        if V is an evidence variable with value v in e
            then return P(v | parents(V)) * ENUMERATE-ALL(rest(vars), e)
        else:
            return sum(v) P(v|parents(V)) * EnumerateA;;(rest((vars)), ev)
                where ev is e extended with V=v
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