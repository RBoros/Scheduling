import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
    ArrayList<Job> jobList;
	int completion;
    public Schedule(){
		jobList = new ArrayList<>();
		completion = 0;
    }
    public Job insert(int time){
        jobList.add(new Job(time));
		dag();
        return get(jobList.size()-1);
    }
    public Job get(int index){
        return jobList.get(index);
    }
    public int finish(){
		return completion;
    }

	private ArrayList<Job> kahn(){
		ArrayList<Job> ordered = new ArrayList<>();


		//insert 0 indegree nodes first
		for(int i = 0; i < jobList.size(); i++){
			if(jobList.get(i).kahnDegree == 0){
				ordered.add(jobList.get(i));
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

		/*
		//initialize indegree list
		for(int i = 0; i < jobList.size(); i++){
			inDeg.put(jobList.get(i), 0);
		}

		//increment indegrees
		for(int i = 0; i < jobList.size(); i++){
			for(int j = 0; j < jobList.get(i).edgeList.size(); j++){
				Job jobKey = jobList.get(i).edgeList.get(j);
				inDeg.put(jobKey, inDeg.get(jobKey)+1);
			}
		}
		//insert 0 indegree nodes first
		for(int i = 0; i < jobList.size(); i++){
			Job jobKey = jobList.get(i);
			if(inDeg.get(jobKey) == 0){
				inDeg.remove(jobKey);
				ordered.add(jobKey);
			}
		}

		//decrements indegrees and completes ordered list
		for(int i = 0; i < ordered.size(); i++){
			for(int j = 0; j < ordered.get(i).edgeList.size(); j++){
				Job jobKey = ordered.get(i).edgeList.get(j);

				Integer val = inDeg.put(jobKey, inDeg.get(jobKey)-1);
				if(val-1 <= 0){
					inDeg.remove(jobKey);
					ordered.add(jobKey);
				}
			}
		}

		 */

		return ordered;

	}
	private void dag(){
		//start topological sort using kahn's
		ArrayList<Job> schedList = kahn();

		//reset all starting and finish times.
		for(int i = 0; i < schedList.size(); i++){
			schedList.get(i).resetTime();
		}

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
		boolean cycle;
		ArrayList<Job> edgeList;

        private Job(int time){
            sTime = 0;
            fTime = 0;
			this.time = time;
			indegree = 0;
			kahnDegree = 0;
			cycle = false;
			edgeList = new ArrayList<>();
        }
        public void requires(Job j){
            j.edgeList.add(this);
			indegree++;
			kahnDegree++;
			dag();
        }
        public int start(){
			return (cycle) ? -1 : sTime;
		 }
		private void resetTime(){
			sTime = 0;
			fTime = time;
			kahnDegree = indegree;
		}
    }
}