package PTF;

public class Company {
    private String companyID;
    private String companyName;
    private String ABNNumber;
    private String URL;
    private String address;


    public Company(String companyID, String companyName, String ABNNumber, String URL, String address) {
        this.companyID = companyID;
        this.companyName = companyName;
        this.ABNNumber = ABNNumber;
        this.URL = URL;
        this.address = address;
    }


    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getABNNumber() {
        return ABNNumber;
    }

    public void setABNNumber(String ABNNumber) {
        this.ABNNumber = ABNNumber;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Company fromString(String line) {
        String[] components = line.split(",");
        String id = components[0];
        String name = components[1];
        String abn = components[2];
        String url = components[3];
        String address = components[4];
        return new Company(id, name, abn, url, address);

    }
    }
