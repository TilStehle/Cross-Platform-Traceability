package preferences;

public class MissingPropertyException extends Exception{

    public MissingPropertyException(String message){
        super(message);
    }

    public MissingPropertyException(Throwable throwable){
        super(throwable);
    }

    public MissingPropertyException(String message, Throwable throwable){
        super(message, throwable);
    }
}
