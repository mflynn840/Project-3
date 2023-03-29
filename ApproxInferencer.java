
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Interfaces.*;
import Parser.*;

public class ApproxInferencer{

    public static void main(String[] args){

        if(args.length > 1){
            BIF2XMLBIF parser = new BIF2XMLBIF();
            try{

                int numSamples = Integer.valueOf(args[0]);
                BayesianNetwork bn = parser.getNetwork(args[1]);
                System.out.println(bn.getVariables());


                Assignment evidence = getEvidence(bn, args);
                //System.out.println(evidence);

                RandomVariable query = bn.getVariableByName(args[2]);
                System.out.println(rejectionSampling(query, evidence, bn, numSamples));

            }catch(Exception ex){}
        }
    }

    public static Assignment getEvidence(BayesianNetwork bn, String[] args){

        Assignment evidence = new Classes.Assignment();

        for(int i = 3; i<args.length; i++){
            
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

    public static Distribution rejectionSampling(RandomVariable query, Assignment evidence, BayesianNetwork bn, int numSamples){

        
        //local vars: C, a vector of counts for each value of X, inditially 0
        Distribution counts = new Classes.Distribution(query);
        //System.out.println(counts);
        //Distribution counts = new int[query.getDomain().size()];

        int goodSamples = 0;
        //for j 1->N do
        for(int i = 0; i<numSamples; i++){
            //System.out.println("taking sample " + i);
            //x <- prior-sample(bn)
            Assignment iid = priorSample(bn);
            
            if(isConsistent(iid, evidence)){
                goodSamples +=1;
                //System.out.println(iid.get(query));


                //System.out.println("debug 2");
                counts.set(iid.get(query), counts.get(iid.get(query)) + 1.0);
                //System.out.println("debug 3");
            }
            //if x is consistent with e:
                //C[j]<-C[j]+1 where xj is the value of X in x

        }

        System.out.println("Kept: " + Math.round(((double)goodSamples/numSamples)*100) + "% of generated samples");
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


    public static Distribution likeleyhoodWeighting(RandomVariable query, Assignment evidence, BayesianNetwork bn, int numSamples){


        //local vars: W is a vector of weighted ounts for each value of X initally 0
        Distribution W = new Classes.Distribution(query);
        
        
        //for j =1 to N
        for(int i = 0; i<numSamples; i++){
            //x, w <- Weighted-sample(bn,e)
            WeightedSample s = weightedSample(bn, e);
            //W[j]<-W[j] + w where xj is the value of X in x

        W.normalize();
        return W;
    }

    public static WeightedSample weigthedSample(BayesianNetwork bn, Assignment evidence){ //returns an event and weight:
        //w <- 1;x an event with n elements, with values fixed from e
        WeightedSample w = new WeightedSample();
        List<RandomVariable> vars = bn.getVariablesSortedTopologically();

        //for i =1 to n do:
        for(int i = 0; i<vars.size(); i++){
            //if Xi is an evidence variable with value xij in e
            if(evidence.containsKey(vars.get(i))){
                //then w<- wx P(Xi=xij | parents(Xi))
                w.sample.put(vars.get(i), evidence.get(vars.get(i)));
                w.weight *= bn.getProbability(vars.get(i), w.sample);
                
            }else{

                //x[i] <- a random sample from P(Xi | parents(Xi))
                
            } 
        }
    
        return w;
    }



}

class WeightedSample{
    double weight;
    Assignment sample;

    public WeightedSample(){

        this.weight = 1.0;
        this.sample = new Classes.Assignment();

    }


}