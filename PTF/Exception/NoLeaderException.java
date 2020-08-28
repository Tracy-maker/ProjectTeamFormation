package PTF.Exception;

public class NoLeaderException extends Exception{
    public NoLeaderException(){
        super("There is no leader");
    }
}
