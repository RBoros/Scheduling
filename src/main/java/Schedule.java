import java.util.ArrayList;


public class Schedule {
    ArrayList<ArrayList<Job>> jobAdjacencyList;
    ArrayList<Integer> inDegrees;
    public Schedule(){
        jobAdjacencyList = new ArrayList<>();
        inDegrees = new ArrayList<>();
    }
    public Job insert(int time){
        ArrayList<Job> newJob = new ArrayList<>();
        newJob.add(new Job(time));
        jobAdjacencyList.add(newJob);
        return get(jobAdjacencyList.size()-1);
    }
    public Job get(int index){
        return jobAdjacencyList.get(index).get(0);
    }
    public int finish(){
        //start topological sort using kahn's

    }

    class Job{
        int sTime;
        int cTime;
        private Job(int cTime){
            sTime = 0;
            this.cTime = cTime;
        }
        private void requires(Job j){
            int i = 0;
            while(jobAdjacencyList.get(i).get(0) != j){
                i++;
            }
            jobAdjacencyList.get(i).add(this);
        }
        public int start(){}
    }
}