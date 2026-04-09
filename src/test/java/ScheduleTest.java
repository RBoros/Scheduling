import org.junit.Test;

import static org.junit.Assert.*;

public class ScheduleTest {

    @Test
    public void test1(){
        Schedule schedule = new Schedule();
        schedule.insert(8); //adds job 0 with time 8
        Schedule.Job j1 = schedule.insert(3); //adds job 1 with time 3
        schedule.insert(5); //adds job 2 with time 5
        schedule.finish(); //should return 8, since job 0 takes time 8 to complete.
        /* Note it is not the earliest completion time of any job, but the earliest the entire set can complete. */
        schedule.get(0).requires(schedule.get(2)); //job 2 must precede job 0
        schedule.finish(); //should return 13 (job 0 cannot start until time 5)
        schedule.get(0).requires(j1); //job 1 must precede job 0
        schedule.finish(); //should return 13
        schedule.get(0).start(); //should return 5
        j1.start(); //should return 0
        schedule.get(2).start(); //should return 0
        j1.requires(schedule.get(2)); //job 2 must precede job 1
        schedule.finish(); //should return 16
        schedule.get(0).start(); //should return 8
        schedule.get(1).start(); //should return 5
        schedule.get(2).start(); //should return 0
        schedule.get(1).requires(schedule.get(0)); //job 0 must precede job 1 (creates loop)
        schedule.finish(); //should return -1
        schedule.get(0).start(); //should return -1
        schedule.get(1).start(); //should return -1
        schedule.get(2).start(); //should return 0 (no loops in prerequisites)
    }

}