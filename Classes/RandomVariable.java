package Classes;
import java.util.ArrayList;
import java.util.Iterator;

import Interfaces.Domain;

public class RandomVariable implements Interfaces.RandomVariable {

    public String name;
    public Domain domain; 

    public RandomVariable(String name, Domain domain){

        this.name = name;
        this.domain = domain;
        

    }

    /*public Interfaces.Value getDomainValue(String s){
        Iterator<Interfaces.Value> values = this.domain.iterator();
        while(values.hasNext()){
            Interfaces.Value value = values.next();
            if(value.toString().equals(s)){
                return value;
            }
        }
        System.out.println("Domain value not found");
        return null;
    }*/

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

    public Value foo(){
        System.out.println("I made it here");
        return new Classes.Value("f");
    }
    public Interfaces.Value getDomainValue(String value){

        //System.out.println("test");
        Iterator<Interfaces.Value> vals = this.domain.iterator();

        while(vals.hasNext()){

            Interfaces.Value next = vals.next();

            if(next.toString().equals(value)){
                return next;
            }
        }

        System.out.println("ERR: value not found");
        return null;

    }
    


    public Domain getDomain(){
        return this.domain;
    }


    public String getName() {
        return this.name;
    }



    
}
