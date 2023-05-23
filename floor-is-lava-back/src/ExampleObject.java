import java.io.Serializable;

public class ExampleObject implements Serializable {

    int x;

    public ExampleObject(int x){
        this.x = x;
    }

    @Override
    public String toString() {
        return "ExampleObject{" +
                "x=" + x +
                '}';
    }
}