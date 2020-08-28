package PTF.Exception;

public class InvalidMemberException extends Exception{
    public InvalidMemberException(String studentId){
        super("Student "+studentId+" is already in another team");
    }
}
