package PTF.Exception;

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String studentId) {
        super("The" + studentId + "is not existed");
    }
}


