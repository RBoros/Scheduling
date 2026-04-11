import org.junit.Test;

import static org.junit.Assert.*;

public class ScheduleTest {

    @Test
    public void test1(){
        Schedule schedule = new Schedule();
        schedule.insert(8); //adds job 0 with time 8
        Schedule.Job j1 = schedule.insert(3); //adds job 1 with time 3
        schedule.insert(5); //adds job 2 with time 5

		//job 0 takes time 8 to complete.
		assertEquals(8, schedule.finish());

		//job 2 must precede job 0
        schedule.get(0).requires(schedule.get(2));

        //(job 0 cannot start until time 5)
		assertEquals(13, schedule.finish());

		//job 1 must precede job 0
		schedule.get(0).requires(j1);

		//should return 13
		assertEquals(13, schedule.finish());

		assertEquals(5, schedule.get(0).start());
		assertEquals(0, j1.start());
		assertEquals(0, schedule.get(2).start());

		//job 2 must precede job 1
        j1.requires(schedule.get(2));

		assertEquals(16, schedule.finish());

		assertEquals(8, schedule.get(0).start());
		assertEquals(5, schedule.get(1).start());
		assertEquals(0, schedule.get(2).start());

		//job 0 must precede job 1 (creates loop)
        schedule.get(1).requires(schedule.get(0));

		assertEquals(-1, schedule.finish());

		assertEquals(-1, schedule.get(0).start());
		assertEquals(-1, schedule.get(1).start());
		// (no loops in prerequisites)
		assertEquals(0, schedule.get(2).start());

    }

}