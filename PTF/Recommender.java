package PTF;

import PTF.Manager.TeamManager;
import PTF.Model.Team;

import java.util.*;

class Tuple<A, B> {
    A a;
    B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }
}

public class Recommender {
    private List<Team> initialState;
    private TeamManager teamManager;

    public Recommender(TeamManager tm) {
        this.teamManager = tm;
        // The current state
        this.initialState = new ArrayList<>(tm.getAllTeams());
    }

    public static void main(String[] args) throws Exception {
        DataEntryPoint dataEntryPoint = DataEntryPoint.getInstance();
        Recommender recommender = new Recommender(dataEntryPoint.teamManager);
        recommender.recommend();
    }

    public Collection<Team> recommend() throws Exception {
        if (initialState.size() < 5) {
            throw new Exception("Teams are not completely formed");
        }
        // Check all teams
        for (Team t : initialState) {
            if (t.getStudentIds().size() < 4) {
                throw new Exception("Teams are not completely formed");
            }
        }
        Set<String> visitedTags = new HashSet<>();

        double minLoss = loss(initialState);
        List<Team> bestState = initialState;

//       Stack<Tuple<List<Team>,Integer>> priorityQueue=new Stack<>();

        // Best first, (State, depth)
        PriorityQueue<Tuple<List<Team>, Integer>> priorityQueue
                = new PriorityQueue<>((state1, state2) -> (int) Math.ceil(loss(state1.a) - loss(state2.a)));
        priorityQueue.add(new Tuple<>(initialState, 1));
        visitedTags.add(stateTag(initialState));


        while (!priorityQueue.isEmpty()) {

            if(Thread.currentThread().isInterrupted()){
                System.out.println("Thread interrupted");
                return null;
            }

            Tuple<List<Team>, Integer> stateTuple = priorityQueue.poll();
            List<Team> state = stateTuple.a;
            int depth = stateTuple.b;
            double loss = loss(state);
            if (loss < minLoss) {
                minLoss = loss;
                bestState = state;
                System.out.println("improved");
                printState(state);
            }

            // Expand but cap the max depth
            if (depth < 6) {
                List<List<Team>> expanded = expand(state);
                System.out.println("depth: "+depth);
                for (List<Team> newState : expanded) {
                   boolean visited = visitedTags.contains(stateTag(newState));
                    if (!visited) {
                        priorityQueue.add(new Tuple<>(newState, depth + 1));
                        visitedTags.add(stateTag(newState));
                        System.out.println("expanded");
                    }else{
                        System.out.printf("not visited: %b\n", false);
                    }
                }
                System.out.println(priorityQueue.size());
            }
        }

        return bestState;
    }

    public List<List<Team>> expand(List<Team> state) {
        // Find the team having the max/min skill shortfall from given state

        ArrayList<Team> sortedState = new ArrayList<>(state);
        sortedState.sort((s1, s2) -> (int)Math.ceil((teamManager.skillShortFall(s1) - teamManager.skillShortFall(s2))));

        List<List<Team>> expandedStates = new ArrayList<>();

        for (int worstPointer = sortedState.size() - 1; worstPointer > 0; worstPointer--) {
            for (int bestPointer = 0; bestPointer < sortedState.size() - 1; bestPointer++) {
                Team betterT = state.get(bestPointer);
                Team minT = state.get(worstPointer);

                System.out.println("maxT: " + betterT.getProjectId());
                System.out.println("minT: " + minT.getProjectId());

                expandedStates.clear();

                // Try to swap one person from team having max short fall to that having smallest short fall
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        // Remember to deep copy
                        ArrayList<Team> newState = new ArrayList<>(state);
                        newState.remove(betterT);
                        newState.remove(minT);
                        // Deep copy
                        List<String> studentsProjA = new ArrayList<>(betterT.getStudentIds());
                        List<String> studentsProjB = new ArrayList<>(minT.getStudentIds());
                        // Swap
                        String tmp = studentsProjA.get(i);
                        studentsProjA.set(i, studentsProjB.get(j));
                        studentsProjB.set(j, tmp);
                        // Construct new teams
                        Team t1 = new Team(betterT.getProjectId(), studentsProjA);
                        Team t2 = new Team(minT.getProjectId(), studentsProjB);
                        newState.add(t1);
                        newState.add(t2);

                        // See if it's valid
                        if (isValid(newState)) {
                            expandedStates.add(newState);
                        }
                    }
                }

                if (expandedStates.size() > 0) {
                    break;
                }
            }
        }


        return expandedStates;
    }

    // The smaller the better
    private double loss(List<Team> state) {
        double stdDevShortFall = teamManager.standardDeviationForShortfall(state);
        double stdDevAverageSkills = teamManager.standardDeviationForOverallSkillCompetency(state);
        double stdDevPreference = teamManager.standardDeviationForSatisfactoryPercentage(state);
        return 0.7 * stdDevShortFall + 0.2 * stdDevAverageSkills + 0.1 * stdDevPreference;
    }

    private boolean isValid(List<Team> state) {
        try {
            for (Team t : state) {
                List<String> studentIds = t.getStudentIds();
                String s1 = studentIds.get(0);
                String s2 = studentIds.get(1);
                String s3 = studentIds.get(2);
                String s4 = studentIds.get(3);
                teamManager.validateConflict(s1, s2, s3, s4);
                teamManager.validateNoLeader(s1, s2, s3, s4);
                teamManager.validatePersonalityImbalance(s1, s2, s3, s4);
                teamManager.validateRepeatedMember(s1, s2, s3, s4);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String stateTag(List<Team> state) {
        // Form a unique tag to identify a state
        // pr1s1s2s3s4pr2s5s7s8s9pr3s6s10
        state.sort(Comparator.comparing(Team::getProjectId));
        StringBuilder sb = new StringBuilder();
        for (Team t : state) {
            sb.append(t.getProjectId());
            ArrayList<String> sortedStudentIds = new ArrayList<>(t.getStudentIds());
            sortedStudentIds.sort(String::compareTo);
            for (String sId : sortedStudentIds) {
                sb.append(sId);
            }
        }
        return sb.toString();
    }

    private void printState(List<Team> state) {
        System.out.println("-----------");
        for (Team t : state) {
            System.out.println(t.getProjectId());
            System.out.println(String.join(",", t.getStudentIds()));
        }
    }

}