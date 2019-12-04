package penso.stackhat.server.util;

public class ParamRunnable<T> implements Runnable {

    public T parameter;

    public ParamRunnable(T parameter) {
        // store parameter for later user
        this.parameter = parameter;
    }
 
    public void run() {
    }
 }