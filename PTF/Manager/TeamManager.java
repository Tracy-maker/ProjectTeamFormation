package PTF.Manager;

import PTF.Exception.*;
import PTF.Model.*;
import PTF.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class TeamManager {
    //  private final HashMap<String, Team> teams = new HashMap<>();

    private final StudentManager studentManager;
    private final ProjectManager projectManager;
    private final StudentPreferenceManager preferenceManager;

    public TeamManager(StudentManager studentManager, ProjectManager projectManager, StudentPreferenceManager preferenceManager) {
        this.studentManager = studentManager;
        this.projectManager = projectManager;
        this.preferenceManager = preferenceManager;
    }

//    public void loadTeamsFromFile() throws IOException {
//
//        try {
//            //1.open file
//            BufferedReader reader = new BufferedReader(new FileReader("selections.txt"));
//            //2.read lines
//            String line = null;
//
//            while ((line = reader.readLine()) != null) {
//                Team team = Team.fromString(line);
//                this.teams.put(team.getProjectId(), team);
//            }
//        } catch (FileNotFoundException e) {
//            // Do nothing
//        }
//    }
//
//
//    //pro1   ---key
//    //studentId ----value
//
//    public void saveTeamsToFile() throws IOException {
//
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("selections.txt"));
//        Collection<Team> allTeams = this.teams.values();
//        for (Team t : allTeams) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(t.getProjectId()).append(" ");//{12,12,21}
//
//            for (String s : t.getStudentIds()) {
//                sb.append(s).append(" ");
//            }
//           sb.append(String.join(" ",t.getStudentIds()));
//
//            //Write information about Project on the files
//            bufferedWriter.write(sb.toString());
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        }
//        bufferedWriter.close();
//    }


    public Team getTeamByProjectId(String projectId) {
        Connection connection = DBHelper.connection();
        ArrayList<String> studentIds = new ArrayList<>();
        try {
            String sql = "SELECT id FROM students WHERE teamId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projectId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String studentId = result.getString("id");
                studentIds.add(studentId);
            }

        } catch (SQLException err) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            err.printStackTrace();
        }
        return new Team(projectId, studentIds);
    }


    public void addTeam(Team team) {
        Connection connection = DBHelper.connection();
        try {
            String projId = team.getProjectId();
            String sql = "INSERT INTO teams(project_id)VALUES(?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projId);
            statement.executeUpdate();

            for (String studentId : team.getStudentIds()) {
                sql = "UPDATE students SET teamId= ? WHERE students.id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, projId);
                statement.setString(2, studentId);
                statement.executeUpdate();
            }

        } catch (SQLException throwables) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addStudentToTeam(String studentId, Team team) {
        team.addStudent(studentId);
        Connection connection = DBHelper.connection();
        try {
            String sql = "UPDATE students SET teamId= ? WHERE students.id= ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, team.getProjectId());
            statement.setString(2, studentId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void validateAvailability(String studentId) throws InvalidMemberException {
        Connection connection = DBHelper.connection();
        boolean available = false;
        try {
            String sql = "SELECT teamId FROM students WHERE student.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String teamId = result.getString("teamId");
                if (teamId == null) {
                    available = true;
                }
            }
        } catch (Exception e) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        if (!available) {
            throw new InvalidMemberException(studentId);
        }

    }

    public void validateConflict(String s1, String s2, String s3, String s4) throws StudentConflictException {

        Student student1 = studentManager.getStudentById(s1);
        Student student2 = studentManager.getStudentById(s2);
        Student student3 = studentManager.getStudentById(s3);
        Student student4 = studentManager.getStudentById(s4);

        Student[] students = {student1, student2, student3, student4};
        HashSet<String> allIds = new HashSet<>();
        allIds.add(s1);
        allIds.add(s2);
        allIds.add(s2);
        allIds.add(s4);
        for (Student student : students) {
            ArrayList<String> conflicts = student.getNoMatchPeople();
            if (conflicts == null) {
                continue;
            }
            for (String cId : conflicts) {
                if (allIds.contains(cId)) {
                    throw new StudentConflictException(student.getStudentID(), cId);
                }
            }
        }
    }

    public void validateConflict(Team team, String newStudentId) throws StudentConflictException {
        HashSet<String> allIds = new HashSet<>(team.getStudentIds());
        allIds.add(newStudentId);
        List<Student> allStudents = allIds.stream().map((id) -> studentManager.getStudentById(id)).
                collect(Collectors.toList());
        for (Student student : allStudents) {
            ArrayList<String> conflicts = student.getNoMatchPeople();
            if (conflicts == null) {
                continue;
            }
            for (String cId : conflicts) {
                if (allIds.contains(cId)) {
                    throw new StudentConflictException(student.getStudentID(), cId);
                }
            }

        }

    }

    public void validatePersonalityImbalance(String s1, String s2, String s3, String s4) throws
            PersonalityImbalanceException {

        Student student1 = studentManager.getStudentById(s1);
        Student student2 = studentManager.getStudentById(s2);
        Student student3 = studentManager.getStudentById(s3);
        Student student4 = studentManager.getStudentById(s4);

        HashSet<String> personalities = new HashSet<>();
        personalities.add(student1.getCharacteristics());
        personalities.add(student2.getCharacteristics());
        personalities.add(student3.getCharacteristics());
        personalities.add(student4.getCharacteristics());

        if (personalities.size() < 3) {
            throw new PersonalityImbalanceException();
        }

    }

    public void validateRepeatedMember(String s1, String s2, String s3, String s4) throws RepeatedMemberException {

        HashSet<String> studentId = new HashSet<>();
        studentId.add(s1);
        studentId.add(s2);
        studentId.add(s3);
        studentId.add(s4);

        if (studentId.size() < 4) {
            throw new RepeatedMemberException();

        }
    }

    public void validateRepeatedMember(Team team, String newStudentId) throws RepeatedMemberException {
        if (team.getStudentIds().contains(newStudentId)) {
            throw new RepeatedMemberException();
        }
    }

    public void validateNoLeader(String s1, String s2, String s3, String s4) throws NoLeaderException {

        boolean findLeader = false;
        HashSet<String> studentIds = new HashSet<>();
        studentIds.add(s1);
        studentIds.add(s2);
        studentIds.add(s3);
        studentIds.add(s4);

        for (String s : studentIds) {
            Student studentContent = studentManager.getStudentById(s);
            String studentPersonality = studentContent.getCharacteristics();

            if (studentPersonality.equals("A")) {
                findLeader = true;
                break;
            }
        }

        if (findLeader == false) {
            throw new NoLeaderException();
        }
    }

    public void validateStudentId(String s1, String s2, String s3, String s4) throws StudentNotFoundException {
        String[] studentIds = {s1, s2, s3, s4};
        for (int i = 0; i < studentIds.length; i++) {
            Student student = studentManager.getStudentById(studentIds[i]);
            if (student == null) {
                throw new StudentNotFoundException(studentIds[i]);
            }

        }
    }

    public void validateStudentId(String newStudentId) throws StudentNotFoundException {
        Student student = studentManager.getStudentById(newStudentId);
        if (student == null) {
            throw new StudentNotFoundException(newStudentId);
        }

    }

    public TechnicalSkillCategories averageTechnicalSkill(String projectId) {
        Team team = this.getTeamByProjectId(projectId);
        return averageTechnicalSkill(team);
    }

    public TechnicalSkillCategories averageTechnicalSkill(Team team) {

        Collection<String> studentIds = team.getStudentIds();

        double p = 0;
        double w = 0;
        double n = 0;
        double a = 0;

        for (String id : studentIds) {
            Student s = studentManager.getStudentById(id);
            TechnicalSkillCategories tech = s.getTechnicalSkillCategories();

            p = p + tech.getProgramming();
            w = w + tech.getWeb();
            n = n + tech.getNetworking();
            a = a + tech.getAnalytics();
        }
        p = p / 4;
        w = w / 4;
        n = n / 4;
        a = a / 4;

        TechnicalSkillCategories technicalSkillCategories = new TechnicalSkillCategories(p, n, a, w);
        return technicalSkillCategories;
    }


    public double skillShortfall(String projectId) {
        Team team = this.getTeamByProjectId(projectId);
        return skillShortFall(team);
    }

    public double skillShortFall(Team team) {
        String projectId = team.getProjectId();
        Project project = projectManager.findProjectById(projectId);
        TechnicalSkillCategories requireSkills = project.getTechnicalSkillCategories();
        TechnicalSkillCategories averageSkills = this.averageTechnicalSkill(projectId);

        double shortFall = 0;
        if (requireSkills.getProgramming() - averageSkills.getProgramming() > 0) {
            shortFall += requireSkills.getProgramming() - averageSkills.getProgramming();
        }
        if (requireSkills.getWeb() - averageSkills.getWeb() > 0) {
            shortFall += requireSkills.getWeb() - averageSkills.getWeb();
        }
        if (requireSkills.getNetworking() - averageSkills.getNetworking() > 0) {
            shortFall += requireSkills.getNetworking() - averageSkills.getNetworking();
        }
        if (requireSkills.getAnalytics() - averageSkills.getAnalytics() > 0) {
            shortFall += requireSkills.getAnalytics() - averageSkills.getAnalytics();
        }
        return shortFall;
    }

    public double satisfactoryPercentage(String projectId) {
        Team team = this.getTeamByProjectId(projectId);
        return satisfactoryPercentage(team);
    }

    public double satisfactoryPercentage(Team team) {
        Collection<String> studentIds = team.getStudentIds();

        int satisfactorySum = 0;
        for (String studentId : studentIds) {
            Collection<StudentPreference> preferences = this.preferenceManager.getPreferencesByStudentId(studentId);
            if (preferences == null) {
                continue;
            }
            Iterator<StudentPreference> preferenceIterator = preferences.iterator();

            for (int i = 0; i < 2; i++) {
                if (preferenceIterator.hasNext()) {
                    StudentPreference preference = preferenceIterator.next();
                    if (preference.getProjectId().equals(team.getProjectId())) {
                        satisfactorySum++;
                        break;
                    }
                }
            }
        }
        return satisfactorySum / 4.0;
    }

    public double overallAverageCompetencyLevel(String projectId) {
        Team team = this.getTeamByProjectId(projectId);
        return overallAverageCompetencyLevel(team);
    }

    public double overallAverageCompetencyLevel(Team team) {
        Collection<String> studentIds = team.getStudentIds();

        double sum = 0;
        for (String id : studentIds) {
            Student student = studentManager.getStudentById(id);
            TechnicalSkillCategories tech = student.getTechnicalSkillCategories();
            sum += tech.getAnalytics();
            sum += tech.getNetworking();
            sum += tech.getProgramming();
            sum += tech.getWeb();
        }
        return sum / 16;
    }

    public double standardDeviationForSatisfactoryPercentage() {
        return standardDeviationForSatisfactoryPercentage(this.getAllTeams());
    }

    public double standardDeviationForSatisfactoryPercentage(Collection<Team> teams) {

        ArrayList<Double> percentages = new ArrayList<>();

        for (Team t : teams) {
            percentages.add(satisfactoryPercentage(t));
        }

        return Utils.standardDeviation(percentages);
    }

    public double standardDeviationForShortfall() {
        return standardDeviationForShortfall(this.getAllTeams());
    }

    public double standardDeviationForShortfall(Collection<Team> teams) {

        ArrayList<Double> shortFalls = new ArrayList<>();

        for (Team t : teams) {
            shortFalls.add(skillShortFall(t));
        }

        return Utils.standardDeviation(shortFalls);
    }

//    public HashMap<String, Double> standardDeviationForSkillCompetency() {
//
//        ArrayList<Double> ws = new ArrayList<>();
//        ArrayList<Double> ps = new ArrayList<>();
//        ArrayList<Double> ns = new ArrayList<>();
//        ArrayList<Double> as = new ArrayList<>();
//
//        for (Team t : this.getAllTeams()) {
//
//            TechnicalSkillCategories skills = this.averageTechnicalSkill(t.getProjectId());
//
//            ws.add(skills.getWeb());
//            ps.add(skills.getProgramming());
//            ns.add(skills.getNetworking());
//            as.add(skills.getAnalytics());
//
//        }
//
//        HashMap<String, Double> result = new HashMap<>();
//
//        result.put("W", Utils.standardDeviation(ws));
//        result.put("P", Utils.standardDeviation(ps));
//        result.put("N", Utils.standardDeviation(ns));
//        result.put("A", Utils.standardDeviation(as));
//
//        return result;
//    }

    public double standardDeviationForOverallSkillCompetency() {
        return standardDeviationForOverallSkillCompetency(this.getAllTeams());
    }

    public double standardDeviationForOverallSkillCompetency(Collection<Team> teams) {
        ArrayList<Double> competencies = new ArrayList<>();

        for (Team t : teams) {
            competencies.add(overallAverageCompetencyLevel(t));
        }
        return Utils.standardDeviation(competencies);
    }

    public boolean areAllTeamFormed() {
        Collection<Team> allTeams = getAllTeams();
        if (allTeams.size() != projectManager.getAllProject().size()) {
            return false;
        }
        for (Team t : allTeams) {
            if (t.getStudentIds().size() != 4) {
                return false;
            }
        }
        return true;
    }

    public Collection<Team> getAllTeams() {

        Connection connection = DBHelper.connection();
        ArrayList<Team> teams = new ArrayList<>();
        try {
            String sql = "SELECT project_id FROM teams";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String projectId = result.getString("project_id");
                Team team = this.getTeamByProjectId(projectId);
                teams.add(team);
            }

        } catch (SQLException err) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }

    public void swapStudent(String studentA, String projectA, String studentB, String projectB) throws Exception {
        Team teamA = getTeamByProjectId(projectA);
        if (!teamA.getStudentIds().contains(studentA)) {
            throw new Exception("Student " + studentA + " is not in " + projectA);
        }
        Team teamB = getTeamByProjectId(projectB);
        if (!teamB.getStudentIds().contains(studentB)) {
            throw new Exception("Student " + studentB + " is not in " + projectB);
        }
        Connection connection = DBHelper.connection();

        try {
            String sql = "UPDATE students SET teamId = ? WHERE students.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projectA);
            statement.setString(2, studentB);
            statement.executeUpdate();

            statement = connection.prepareStatement(sql);
            statement.setString(1, projectB);
            statement.setString(2, studentA);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            try {
                connection.close();
            } catch (SQLException err) {
                err.printStackTrace();
            }

        }

    }

    public void reassignTeam(Collection<Team> teams) throws Exception {
        for (Team t : teams) {
            List<String> studentIds = t.getStudentIds();

            String s1 = studentIds.get(0);
            String s2 = studentIds.get(1);
            String s3 = studentIds.get(2);
            String s4 = studentIds.get(3);

            this.validateConflict(s1, s2, s3, s4);
            this.validateNoLeader(s1, s2, s3, s4);
            this.validatePersonalityImbalance(s1, s2, s3, s4);
            this.validateRepeatedMember(s1, s2, s3, s4);
        }

        Connection connection = DBHelper.connection();

        try {
            for (Team t : teams) {
                List<String> studentIds = t.getStudentIds();

                StringBuilder builder=new StringBuilder();
                for (int i = 0; i < studentIds.size(); i++) {
                    builder.append("?,");
                }

                builder.deleteCharAt(builder.length()-1);

                String sql = "UPDATE students SET teamId = ? WHERE students.id IN ("+builder.toString()+")";
                PreparedStatement statement =connection.prepareStatement(sql);
                statement.setString(1,t.getProjectId());
                for (int i = 2; i < studentIds.size(); i++) {
                    statement.setString(i,studentIds.get(i-2));
                }
                statement.executeUpdate();
            }
        } catch (SQLException throwables) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}

