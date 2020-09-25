package PTF;

import PTF.Manager.*;
import PTF.Model.*;
import java.io.IOException;
import java.util.*;

public class MainMenu {


    public static void main(String[] args) throws IOException {

        //Loop back to the main interface again
        while (true) {
            //The information of the Menu
            System.out.println("---------------Menu---------------");
            System.out.println("A.  Add Company");
            System.out.println("B.  Add Project Owner");
            System.out.println("C.  Add Project");
            System.out.println("D.  Capture Student Personalities");
            System.out.println("E.  Add Student Preferences");
            System.out.println("F.  Shortlist Project");
            System.out.println("G.  Form Team");
            if (DataEntryPoint.getInstance().teamManager.areAllTeamFormed()) {
                System.out.println("H.  Print team metrics");
            }
            System.out.println("N.  Exit ");
            System.out.println("  Please input you choice: ");

            //Input data
            Scanner scanner = new Scanner(System.in);
            String word = scanner.nextLine();
            switch (word) {
                case "A":
                    addCompany();
                    break;
                case "B":
                    addProjectOwner();
                    break;
                case "C":
                    addProject();
                    break;
                case "D":
                    studentPersonalities();
                    break;
                case "E":
                    studentPreference();
                    break;
                case "F":
                    preferenceProject();
                    break;
                case "G":
                    formTeam();
                    break;
                case "H":
                    if (DataEntryPoint.getInstance().teamManager.areAllTeamFormed()) {
                        printMetrics();
                    }else{
                        System.out.println("please enter valid word for this menu.");
                    }
                    break;
                case "N":
                    System.out.println(" Thank you for using");
                    System.exit(0);
                default:
                    System.out.println("please enter valid word for this menu");
            }
        }
    }

    // Add information about company
    public static void addCompany() throws IOException {
        boolean N = false;
        //Input information about the company
        System.out.println("---Welcome to add information about company---");
        System.out.println("Enter any key to begin adding information... Or press N to exit");
        while (true) {
            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine();
            if (choice.equals("N")) {
                System.out.println("Thank you for your using ! ");
                break;
            } else {
                System.out.println(" Please input company ID: ");
                String ID = sc.nextLine();


                if (DataEntryPoint.getInstance().companyManager.findCompanyById(ID) != null) {
                    System.out.println("Duplicate company ID, try again!");
                    System.out.println("Enter any key to begin adding information... Or press N to exit");
                    continue;
                }

                System.out.println(" Please input company name: ");
                String name = sc.nextLine();
                System.out.println(" Please input ABN number: ");
                String ABN = sc.nextLine();
                System.out.println(" Please input company URL: ");
                String URL = sc.nextLine();
                System.out.println(" Please input company address: ");
                String address = sc.nextLine();


                //Create the company object and assign the data to the company object
                Company c = new Company(ID, name, ABN, URL, address);

                //Put the information into the collection
                try {
                    DataEntryPoint.getInstance().companyManager.addCompany(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(" Add information successfully ");
                System.out.println(" If you don't have information to added,please enter word -> N ");

            }
        }
      //  DataEntryPoint.getInstance().companyManager.saveCompaniesToFile();
    }

    // Add information about Project Owner
    public static void addProjectOwner() throws IOException {

        //Input information about your choice
        System.out.println("----Welcome to add information about Project Owner----");

        System.out.println("Enter any key to begin adding information... Or press N to exit");

        while (true) {
            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine();
            if (choice.equals("N")) {
                System.out.println("Thank you for your using !");
                break;
            } else {
                System.out.println(" Please input Project owner ID: ");
                String ID = sc.nextLine();

                if (DataEntryPoint.getInstance().projectOwnerManager.findProjectOwnerById(ID) != null) {
                    System.out.println("Duplicate company ID, try again!");
                    System.out.println("Enter any key to begin adding information... Or press N to exit");
                    continue;
                }

                System.out.println(" Please input First name: ");
                String fN = sc.nextLine();
                System.out.println(" Please input Surname: ");
                String sN = sc.nextLine();
                System.out.println(" Please input role: ");
                String role = sc.nextLine();
                System.out.println(" Please input email ");
                String email = sc.nextLine();
                System.out.println("Please input Company ID");
                String CID = sc.nextLine();

                //Create the company object and assign the data to the company object
                ProjectOwner pO = new ProjectOwner(fN, sN, ID, role, email, CID);

                //Put the information into the collection
                try {
                    DataEntryPoint.getInstance().projectOwnerManager.addProjectOwner(pO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Add information successfully");
                System.out.println("If you don't have information to added,please enter word -> N ");
            }

        }
     //   DataEntryPoint.getInstance().projectOwnerManager.saveProjectOwnersToFile();

    }

    // Add information about Project Owner
    public static void addProject() throws IOException {

        //Input information about your choice
        System.out.println("---Welcome to add information about Project---");
        System.out.println("Enter any key to begin adding information... Or press N to exit");

        while (true) {
            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine();
            if (choice.equals("n")) {
                System.out.println(" Thank you for your using !");
                break;
            } else {
                System.out.println(" Please input Project ID: ");
                String PID = sc.nextLine();

                if (DataEntryPoint.getInstance().projectManager.findProjectById(PID) != null) {
                    System.out.println("Duplicate company ID, try again!");
                    System.out.println("Enter any key to begin adding information... Or press N to exit");
                    continue;
                }

                System.out.println(" Please input Project owner ID: ");
                String oID = sc.nextLine();
                System.out.println(" Please input title: ");
                String title = sc.nextLine();
                System.out.println(" Please input brief description: ");
                String bd = sc.nextLine();


                TechnicalSkillCategories t = null;
                while (true) {
                    try {
                        System.out.println(" Please input rank number ! (4 being the highest and 1 the lowest)");
                        System.out.println("(P): Programming & Software Engineering ");
                        int P = sc.nextInt();
                        if (P <= 0 || P > 4) {
                            throw new Exception();
                        }
                        System.out.println("(N): Networking and Security ");
                        int N = sc.nextInt();
                        if (N <= 0 || N > 4) {
                            throw new Exception();
                        }
                        System.out.println("(A): Analytics and Big Data ");
                        int A = sc.nextInt();
                        if (A <= 0 || A > 4) {
                            throw new Exception();
                        }
                        System.out.println("(W): Web & Mobile application");
                        int W = sc.nextInt();
                        if (W <= 0 || W > 4) {
                            throw new Exception();
                        }
                        t = new TechnicalSkillCategories();
                        t.setProgramming(P);
                        t.setNetworking(N);
                        t.setAnalytics(A);
                        t.setWeb(W);
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input, please try again");
                        // Eat the bad input
                        sc.nextLine();
                    }
                }

                //Create the project owner object and assign the data to the project owner object
                Project p = new Project(title, PID, bd, t, oID);

                //Put the information into the collection
                try {
                    DataEntryPoint.getInstance().projectManager.addProject(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Add information successfully");
                System.out.println("If you don't have information to added,please enter word -> n ");
            }
        }
      //  DataEntryPoint.getInstance().projectManager.saveProjectsToFile();
    }

    //Create method about capture student personalities
    public static void studentPersonalities() throws IOException {

        //Read the student file
        int countA = 0;
        int countB = 0;
        int countC = 0;
        int countD = 0;

        Scanner sc = new Scanner(System.in);
        for (Student s : DataEntryPoint.getInstance().studentManager.getAllStudent()) {
            System.out.println("We are collecting information for student " + s.getStudentID());
            System.out.println("Now put the student's personality.");
            System.out.println("There are four Types which include [A]---Director, [B]---Socialize, [C]---Thinker, [D]---Supporter," +
                    "please enter the word that match this student ability ");
            System.out.println("please input personality type for this student...");

            String personalityType = null;

            //Every projects need five personality type
            while (true) {
                personalityType = sc.nextLine();
                if (personalityType.equals("A") && countA <= 5) {
                    countA++;
                    break;
                } else if (personalityType.equals("B") && countB <= 5) {
                    countB++;
                    break;
                } else if (personalityType.equals("C") && countC <= 5) {
                    countC++;
                    break;
                } else if (personalityType.equals("D") && countD <= 5) {
                    countD++;
                    break;
                } else {
                    System.out.println("Your input is invalid, please input again! ");
                }
            }
            s.setCharacteristics(personalityType);
            String[] studentIDs = null;
            while (true) {
                System.out.println("please enter the student-numbers they cannot work together or press enter to skip!");
                String dislikePeople = sc.nextLine();
                if (dislikePeople.equals("")) {
                    break;
                }

                //s1 s2
                studentIDs = dislikePeople.split(" ");
                if (studentIDs.length > 2) {
                    System.out.println("only two disliked students are allowed,please input again.");
                    continue;
                }
                boolean isValid = true;
                for (String id : studentIDs) {
                    if (DataEntryPoint.getInstance().studentManager.getStudentById(id) == null) {
                        System.out.println("Student " + id + " does not exist! please input again.");
                        isValid = false;
                        break;
                    }
                }
                if (isValid == true) {
                    break;
                }
            }
            ArrayList<String> list1 = new ArrayList<>();
            if (studentIDs != null) {
                Collections.addAll(list1, studentIDs);
            }
            s.setNoMatchPeople(list1);
        }
     //   DataEntryPoint.getInstance().studentManager.saveStudentToFile();

    }

    //The information of student preference
    public static HashMap<String, Integer> studentPreference() throws IOException {


        //Create HashMap to collection the content of project preference
        HashMap<String, ArrayList<String>> projectPreference = new HashMap<String, ArrayList<String>>();
        HashMap<String, Integer> preferenceAnalyze = new HashMap<String, Integer>();

        //Input information about your choice
        System.out.println("Please input your preference project...");
        System.out.println("please input any key to start ");

        String studentID;
        String projectNumber;
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("please input your student ID");
            studentID = sc.nextLine();


            if (DataEntryPoint.getInstance().studentManager.getStudentById(studentID) == null) {
                System.out.println("This student is not exited.");
                continue;
            }

            while (true) {

                while (true) {
                    System.out.println("please input the project number");
                    projectNumber = sc.nextLine();
                    if (DataEntryPoint.getInstance().projectManager.findProjectById(projectNumber) == null) {
                        System.out.println("Project doesn't exist,please input agaim!");
                    } else {
                        break;
                    }
                }

                System.out.println("please choice the number to added the project: 1 -> Terrible 2 -> Fair 3 -> Good 4 ->Excellent");
                String studentPreferences = sc.nextLine();
                int sp = 0;
                try {
                    sp = Integer.parseInt(studentPreferences);
                } catch (Exception e) {
                    System.out.println("Your input is invalid, please input again!");
                    continue;
                }
                if (sp <= 4 && sp >= 1) {
                    System.out.println(" If you don't have information to added,please enter word -> n ");
                    System.out.println("Continue,please input any key to start......");
                } else {
                    System.out.println("Your input is invalid, please input again! ");
                }
                StudentPreference studentPreference = new StudentPreference();
                studentPreference.setProjectId(projectNumber);
                studentPreference.setGrade(sp);
                DataEntryPoint.getInstance().studentPreferenceManager.addPreference(studentID, studentPreference);

                //Analyze
                Integer proGradeSum = preferenceAnalyze.get(projectNumber);
                if (proGradeSum == null) {
                    proGradeSum = 0;
                }
                proGradeSum = proGradeSum + Integer.parseInt(studentPreferences);
                preferenceAnalyze.put(projectNumber, proGradeSum);

                System.out.println("Please enter any key to add other project, or enter N to finish");

                String choice = sc.nextLine();

                if (choice.toUpperCase().equals("N")) {
                    System.out.println(" Add information successfully, thank you for using.");
                    break;
                }
            }

            System.out.println("Enter any key to add information for other students or press N to finish");
            String choice = sc.nextLine();
            if (choice.toUpperCase().equals("N")) {
                break;
            }

        }
     //   DataEntryPoint.getInstance().studentPreferenceManager.saveToFile();
        return preferenceAnalyze;
    }

    //The information of preference project
    public static void preferenceProject() throws IOException {

        StudentPreferenceManager prefManager = DataEntryPoint.getInstance().studentPreferenceManager;
        HashMap<String, Integer> projectPreferenceAnalytics = prefManager.getProjectPreferenceStats();

        //Sort the appropriate content
        ArrayList<String> projectId = new ArrayList<String>(DataEntryPoint.getInstance().projectManager.getAllProjectId());
        projectId.sort(new Comparator<String>() {
            @Override
            public int compare(String k1, String k2) {
                Integer value1 = projectPreferenceAnalytics.get(k1);
                if (value1 == null) {
                    value1 = 0;
                }
                Integer value2 = projectPreferenceAnalytics.get(k2);
                if (value2 == null) {
                    value2 = 0;
                }

                return value1 - value2;
            }
        });

        //Find the fount of information to deleted
        for (int i = 0; i < Math.min(5, projectId.size()); i++) {
            String badProjectId = projectId.get(i);
            DataEntryPoint.getInstance().projectManager.deletedProjectId(badProjectId);
        }
      //  DataEntryPoint.getInstance().projectManager.saveProjectsToFile();
    }

    static void formTeam() throws IOException {
        Scanner sc = new Scanner(System.in);
        Project pro = null;
        while (true) {
            System.out.println("What project do want to form team for");
            String projectId = sc.nextLine();
            pro = DataEntryPoint.getInstance().projectManager.findProjectById(projectId);
            if (pro == null) {
                System.out.println("This " + pro + " is not exist.");
            } else {
                break;
            }
        }
        StringBuilder contentDisplay = new StringBuilder();
        contentDisplay.append(pro.getProjectID()).append(" ").append(pro.getTitle())
                .append(" ").append(pro.getTechnicalSkillCategories());


        System.out.println("please input the first student ID");
        String student1 = sc.nextLine();
        System.out.println("please input the second student ID");
        String student2 = sc.nextLine();
        System.out.println("please input the third student ID");
        String student3 = sc.nextLine();
        System.out.println("please input the fourth student ID");
        String student4 = sc.nextLine();

        try {
            DataEntryPoint.getInstance().teamManager.validateNoLeader(student1, student2, student3, student4);
            DataEntryPoint.getInstance().teamManager.validateRepeatedMember(student1, student2, student3, student4);
            DataEntryPoint.getInstance().teamManager.validatePersonalityImbalance(student1, student2, student3, student4);
            DataEntryPoint.getInstance().teamManager.validateConflict(student1, student2, student3, student4);
            DataEntryPoint.getInstance().teamManager.validateAvailability(student1);
            DataEntryPoint.getInstance().teamManager.validateAvailability(student2);
            DataEntryPoint.getInstance().teamManager.validateAvailability(student3);
            DataEntryPoint.getInstance().teamManager.validateAvailability(student4);
            DataEntryPoint.getInstance().teamManager.validateStudentId(student1, student2, student3, student4);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Team team = new Team(pro.getProjectID(), student1, student2, student3, student4);
        DataEntryPoint.getInstance().teamManager.addTeam(team);
       // DataEntryPoint.getInstance(). teamManager.saveTeamsToFile();
    }

    static void printMetrics() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter project ID:");
        String projectId = sc.nextLine();

        if (DataEntryPoint.getInstance().teamManager.getTeamByProjectId(projectId) == null) {
            System.out.println("Invaild project ID");
            return;
        }
      TechnicalSkillCategories skillCategories=DataEntryPoint.getInstance().teamManager.averageTechnicalSkill(projectId);
        System.out.println("Average skill competency");

        System.out.println((skillCategories.getAnalytics()+skillCategories.getWeb()+
                skillCategories.getProgramming()+skillCategories.getNetworking())/4);
        System.out.println("\n");

        System.out.println("Satisfactory percentage: ");
        System.out.println(DataEntryPoint.getInstance().teamManager.satisfactoryPercentage(projectId)*100+"%");
        System.out.println("\n");

        System.out.println("Skill shortfall: ");
        System.out.println(DataEntryPoint.getInstance().teamManager.skillShortfall(projectId));
    }
}



