package PTF.Exception;

public class StudentConflictException extends Exception{
    public StudentConflictException(String student1,String student2){
        super("There is conflict between "+student1+"and"+student2);
    }
}
