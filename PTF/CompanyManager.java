package PTF;

import java.io.*;
import java.util.HashMap;


public class CompanyManager {
    private HashMap<String, Company> companies = new HashMap<String, Company>();

    public void loadCompaniesFromFile() throws IOException {
        try {
            //1.open file
            BufferedReader reader = new BufferedReader(new FileReader("companies.txt"));
            //2.read lines
            String line = null;

            while ((line = reader.readLine()) != null) {
                Company company = Company.fromString(line);
                this.companies.put(company.getCompanyID(), company);
            }
        } catch (FileNotFoundException e) {
            // Do nothing
        }
    }

    public void saveCompaniesToFile() throws IOException {
        //Creates a character buffer output stream object
        BufferedWriter bw = new BufferedWriter(new FileWriter("companies.txt"));

        for (Company company : this.companies.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(company.getCompanyID()).append(",").append(company.getCompanyName()).append(",")
                    .append(company.getABNNumber()).append(",").append(company.getURL()).append(",").append(company.getAddress());

            //Write information about company on the files
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    public Company findCompanyById(String id) {

        return companies.get(id);
    }

    public void addCompany(Company company) throws Exception {
        if (this.findCompanyById(company.getCompanyID()) != null) {
            //Duplicate detected
            throw new Exception("Duplicate company id");
        }
        this.companies.put(company.getCompanyID(),company);
    }
}
