import java.util.ArrayList;

public class Schedule {
    ArrayList<Job> jobList;
	int completion;
    boolean rerun;
    public Schedule(){
		jobList = new ArrayList<>();
		completion = 0;
        rerun = true;
    }
    //constant time
    public Job insert(int time){
        jobList.add(new Job(time));
        rerun = true;
        return get(jobList.size()-1);
    }
    //constant time
    public Job get(int index){
        return jobList.get(index);
    }

    public int finish(){
        if(rerun){
            rerun = false;
            dag();
        }
		return completion;
    }
    //V + (V*E) time

    /**
     * Runs Kahn's algorithm then
     * relaxes edges in order in ordered list
     *
     * 0(V + (V*E)) time complexity
     */
	private void dag(){
		ArrayList<Job> ordered = new ArrayList<>();

		//insert 0 indegree nodes first
		//resets all variables
		for(int i = 0; i < jobList.size(); i++){
			Job jobber = jobList.get(i);
			jobber.reset();
			if(jobber.kahnDegree == 0){
				ordered.add(jobber);
			}
		}

		//decrements indegrees and adds to ordered
        //relaxes start and finish times
        int m = 0;
		for(int i = 0; i < ordered.size(); i++){
            Job u = ordered.get(i);
            u.fTime = u.sTime + u.time;
			for(int j = 0; j < ordered.get(i).edgeList.size(); j++){
				Job v = u.edgeList.get(j);
                v.sTime = Math.max(u.fTime, v.sTime);
				if((--v.kahnDegree) <= 0 ){
					ordered.add(v);
				}
			}
            m = Math.max(m, u.fTime);
		}
        completion = (ordered.size() != jobList.size())
                ? -1
                : m;

	}

    class Job{
        int sTime;
        int fTime;
		final int time;
		int indegree;
		int kahnDegree;
		ArrayList<Job> edgeList;

        private Job(int time){
            sTime = 0;
            fTime = 0;
			this.time = time;
			indegree = 0;
			kahnDegree = 0;
			edgeList = new ArrayList<>();
        }
        public void requires(Job j){
            j.edgeList.add(this);
            rerun = true;
			indegree++;
        }
        public int start(){
            if(rerun){
                rerun = false;
                dag();
            }
			sTime = (kahnDegree != 0) ? -1 : sTime;
			return sTime;
		}
		private void reset(){
			sTime = 0;
			fTime = time;
			kahnDegree = indegree;
		}
    }
}