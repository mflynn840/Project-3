
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Interfaces.*;
import Parser.*;

public class RejectionSampler{

    public static void main(String[] args){

        if(args.length > 2){
            XMLBIFParser parser = new XMLBIFParser();
            try{

                int numSamples = Integer.valueOf(args[0]);
                System.out.println(args[1]);
                BayesianNetwork bn = parser.readNetworkFromFile(args[1]);

                System.out.println(bn.getVariables());


                Assignment evidence = getEvidence(bn, args);
                //System.out.println(evidence);

                RandomVariable query = bn.getVariableByName(args[2]);
                System.out.println("Rejection sampling " + rejectionSampling(query, evidence, bn, numSamples));
                //System.out.println("Likelyhood weighting" + likeleyhoodWeighting(query, evidence, bn, numSamples));



            }catch(Exception ex){System.out.println("Malformed query (bad file name or variable not in BN)");}
        }
    }

    public static Assignment getEvidence(BayesianNetwork bn, String[] args){

        Assignment evidence = new Classes.Assignment();

        for(int i = 3; i<args.length; i++){
            
            RandomVariable var = bn.getVariableByName(args[i]);
            i++;
            
            if(true){
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
            Iterator<Value> dVals = current.getDomain().iterator();
            CPT distribution = bn.getDistribution(current);

            double random = Math.random();

            int index = 0;
            while(dVals.hasNext()){

                //System.out.println(distribution);
                Value currentV = dVals.next();
                //System.out.println(dVal);

                //System.out.println("Index: " + index + "Domain size: " + current.getDomainSize());
                //System.out.println(currentV);
                if(index <= current.getDomainSize()-2){
                    if(distribution.get(currentV, x) >= random){
                        //System.out.println("sample");
                        x.put(current, currentV);
                        break;
                    }
                }else{
                    //System.out.println("sample 2");
                    x.put(current, currentV);
                    //System.out.println("debug");
                    break;
                }

                index++;

            }


            //System.out.println("debug");


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