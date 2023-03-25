package Classes;
import java.util.ArrayList;

import Interfaces.Domain;

public class RandomVariable implements Interfaces.RandomVariable {

    public String name;
    public Domain domain; 

    public RandomVariable(String name, Domain domain){

        this.name = name;
        this.domain = domain;
        

    }

    public RandomVariable(String name){
        this.name = name;
    }

    public void addDomainValue(Value value){
        this.domain.add(value);
    }

    public String toString(){
        return this.name + ": {" + getDomain() + "}";
    }

    public int getDomainSize(){
        return this.domain.size();
    }


    public Domain getDomain(){
        return this.domain;
    }


    public String getName() {
        return this.name;
    }



    
}
