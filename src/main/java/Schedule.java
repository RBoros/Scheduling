import java.util.ArrayList;

public class Schedule {
    ArrayList<Job> jobList;
	int completion;
    public Schedule(){
		jobList = new ArrayList<>();
		completion = 0;
    }
    public Job insert(int time){
        jobList.add(new Job(time));
        return get(jobList.size()-1);
    }
    public Job get(int index){
        return jobList.get(index);
    }
    public int finish(){
		dag();
		return completion;
    }

	private ArrayList<Job> kahn(){
		ArrayList<Job> ordered = new ArrayList<>();

		//insert 0 indegree nodes first
		//also resets all variables
		for(int i = 0; i < jobList.size(); i++){
			Job jobber = jobList.get(i);
			jobber.reset();
			if(jobber.kahnDegree == 0){
				ordered.add(jobber);
			}
		}

		//decrements indegrees and completes ordered list
		for(int i = 0; i < ordered.size(); i++){
			for(int j = 0; j < ordered.get(i).edgeList.size(); j++){
				Job outjob = ordered.get(i).edgeList.get(j);
				if((--outjob.kahnDegree) <= 0 ){
					ordered.add(outjob);
				}
			}
		}

		return ordered;

	}
	private void dag(){
		//start topological sort using kahn's
		ArrayList<Job> schedList = kahn();

		int m = 0;
		for(int i = 0; i < schedList.size(); i++){
			Job u = schedList.get(i);
			u.fTime = u.sTime + u.time;
			for(int j = 0; j < schedList.get(i).edgeList.size(); j++){
				Job v = schedList.get(i).edgeList.get(j);
				v.sTime = Math.max(u.fTime, v.sTime);
			}
			m = Math.max(m, u.fTime);
		}
		completion = (schedList.size() != jobList.size())
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
			indegree++;
        }
        public int start(){
			dag();
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