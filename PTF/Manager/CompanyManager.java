package PTF.Manager;

import PTF.Model.Company;
import PTF.Model.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class CompanyManager {
//    private HashMap<String, Company> companies = new HashMap<String, Company>();
//    public void loadCompaniesFromFile() throws IOException {
//        try {
//            //1.open file
//            BufferedReader reader = new BufferedReader(new FileReader("companies.txt"));
//            //2.read lines
//            String line = null;
//
//            while ((line = reader.readLine()) != null) {
//                Company company = Company.fromString(line);
//                this.companies.put(company.getCompanyID(), company);
//            }
//        } catch (FileNotFoundException e) {
//            // Do nothing
//        }
//    }
//
//    public void saveCompaniesToFile() throws IOException {
//        //Creates a character buffer output stream object
//        BufferedWriter bw = new BufferedWriter(new FileWriter("companies.txt"));
//
//        for (Company company : this.companies.values()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(company.getCompanyID()).append(",").append(company.getCompanyName()).append(",")
//                    .append(company.getABNNumber()).append(",").append(company.getURL()).append(",").append(company.getAddress());
//
//            //Write information about company on the files
//            bw.write(sb.toString());
//            bw.newLine();
//            bw.flush();
//        }
//        bw.close();
//    }

    public Company findCompanyById(String id) {
        try {
            Connection connection = DBHelper.connection();
            String sql = "SElECT * FROM companies WHERE id =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String abn = resultSet.getString("abn");
                String url = resultSet.getString("url");
                String address = resultSet.getString("address");
                connection.close();
                return new Company(id,name,abn,url,address);

            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return null;
    }

    public void addCompany(Company company) throws Exception {
        if (this.findCompanyById(company.getCompanyID()) != null) {
            //Duplicate detected
            throw new Exception("Duplicate company id");
        }
        try {
            Connection connection = DBHelper.connection();
            String sql = "INSERT INTO companies(id,name,abn,url,address)VALUES(?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, company.getCompanyID());
            statement.setString(2, company.getCompanyName());
            statement.setString(3, company.getABNNumber());
            statement.setString(4, company.getURL());
            statement.setString(5, company.getAddress());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Collection<Company> getAllCompanies() {
        try {
            ArrayList<Company> companies=new ArrayList<>();
            Connection connection = DBHelper.connection();
            String sql = "SElECT * FROM companies WHERE id =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
             while (result.next()) {
                String id = result.getString("id");
                String name = result.getString("name");
                String abn = result.getString("abn");
                String url = result.getString("url");
                String address = result.getString("address");
                Company company=new Company(id,name,abn,url,address);
                companies.add(company);

            }
            connection.close();
             return companies;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
