package PTF.Model;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    public static Connection connection() {
        Connection conn = null;
        try{
            String url="jdbc:sqlite:assignmentTeamProject.sqlite";

            conn= DriverManager.getConnection(url);

            return conn;
        } catch (SQLException t ){
            System.out.println(t.getMessage());
        }
return null;
    }
}
