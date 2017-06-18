package selectedproblem;

import com.google.common.base.MoreObjects;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SchedulingProblem {
    private static class Job {
        private final Object task;
        private final int length;
        private final int weight;
        public Job(Object task, int length, int weight) {
            this.task = task;
            this.length = length;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("task", task)
                    .add("length", length)
                    .add("weight", weight)
                    .toString();
        }
    }
    private final List<Job> jobs;
    public SchedulingProblem() {
        this.jobs = new ArrayList<>();
    }
    public void addJob(Object content, int length, int weight) {
        jobs.add(new Job(content, length, weight));
    }

    public long getScheduleWrong() {
        List<Job> schedule = new ArrayList<>(jobs);
        schedule.sort((j1, j2) -> {
            // incorrect greedy function
            int diff = j2.weight - j2.length - j1.weight + j1.length;
            return diff != 0 ? diff : Integer.compare(j2.weight, j1.weight);
        });
        return computeCost(schedule);
    }

    public long getSchedule() {
        List<Job> schedule = new ArrayList<>(jobs);
        schedule.sort((j1, j2) ->
             Double.compare(j2.weight*1.0/j2.length, j1.weight*1.0/j1.length));

        // check rep: decreasing order
        double score = Double.MAX_VALUE;
        for(Job job: schedule) {
            assert job.weight*1.0/job.length <= score;
            score = job.weight*1.0/job.length;
        }

        return computeCost(schedule);
    }

    /** Compute sum of weighted completion time:
     *  Sum of Weighted Completion Time = wi*ci for all i
     *  Where Ci = l1 + l2 + ... + l3
     * */
    private long computeCost(List<Job> schedule) {
        long l = 0;
        long sum = 0;
        for(Job job : schedule) {
            l += job.length;
            sum += l*job.weight;
        }
        return sum;
    }

    public static void main(String[] args) throws Exception{
        SchedulingProblem sp = new SchedulingProblem();
        Scanner in = new Scanner(new FileReader("./testData/graph/jobsSchedule.txt"));
        int i = 1;
        if(in.hasNextLine()) {
            System.out.println("Number of job: " + in.nextLine());
        }
        while (in.hasNextLine()) {
            String[] input = in.nextLine().trim().split("\\s+");
            sp.addJob(i++, Integer.valueOf(input[1]), Integer.valueOf(input[0]));
        }
        System.out.println(sp.getScheduleWrong());
        System.out.println(sp.getSchedule());
    }

}
