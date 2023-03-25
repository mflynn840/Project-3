package Classes;

public class Value<E> implements Interfaces.Value {

    E value;

    public Value(E value){
        this.value = value;
    }


    public E getValue(){
        return this.value;

    }

    public String printValueStr(){
        return this.value.toString();
    }
    
}
