# Job Scheduling via Topological Sort (Kahn's Algorithm)
 
**Author:** Raymond Okolo
**Date Started:** April 21, 2026
**Date Modified:** September 25, 2026
 
## Overview
 
This project models a set of **jobs with precedence constraints** as a
directed acyclic graph (DAG) and computes the earliest possible start/finish
time for each job, along with the overall project completion time. Scheduling
is performed with **Kahn's algorithm** (BFS-style topological sort), relaxing
start/finish times as jobs are processed in topological order. If the
dependency graph contains a cycle, the schedule is reported as infeasible.
 
## Files
 
| File               | Description                                                        |
|--------------------|----------------------------------------------------------------------|
| `Schedule.java`     | The scheduler implementation, including the inner `Job` class.      |
| `ScheduleTest.java` | JUnit test suite covering chained dependencies, recomputation, and cycle detection. |
 
## Public API (`Schedule`)
 
- **`Job insert(int time)`** — *O(1)*
  Creates a new `Job` that takes `time` units to complete, adds it to the
  schedule, marks the schedule as dirty (`rerun = true`), and returns the
  new `Job`.
- **`Job get(int index)`** — *O(1)*
  Returns the job at the given index (in insertion order).
- **`int finish()`** — *O(V + V·E)*
  Returns the overall completion time of the schedule. If any job or
  dependency has changed since the last computation, this re-runs the
  topological sort (`dag()`) before returning the result. Returns **-1** if
  the dependency graph contains a cycle (i.e., not all jobs could be
  topologically ordered).
## Internal Design
 
### `dag()` (private)
 
Implements Kahn's algorithm:
1. Every job is reset (start time = 0, finish time = its own duration,
   `kahnDegree` reset to its full indegree), and all jobs with indegree 0
   are seeded into the `ordered` list.
2. The `ordered` list is processed as a growing queue: for each job `u`,
   its finish time is fixed (`sTime + time`), and every job `v` that depends
   on `u` has its start time relaxed to `max(u.fTime, v.sTime)`. When `v`'s
   remaining indegree reaches 0, it's appended to `ordered`.
3. The overall completion time is the maximum finish time seen.
4. If `ordered` never grows to include every job, the graph has a cycle and
   `completion` is set to `-1`.
This gives each job's **earliest possible start/finish time** subject to its
prerequisites, and the schedule's completion time is the length of the
critical path.
 
### `Job` (inner class)
 
Each `Job` stores:
- `time` — the fixed duration of the job.
- `sTime` / `fTime` — computed earliest start/finish time.
- `indegree` / `kahnDegree` — total and remaining number of prerequisites.
- `edgeList` — jobs that depend on this one (outgoing edges in the DAG).
Key operations:
- **`requires(Job j)`** — declares that `j` must complete before this job can
  start (adds an edge `j -> this`, increments this job's `indegree`, and
  marks the schedule dirty).
- **`start()`** — returns this job's earliest start time, recomputing the
  schedule first if it's stale. Returns `-1` if the job is part of a cycle
  (its `kahnDegree` never reached 0).
## Lazy Recomputation
 
Both `Schedule` and `Job` share a single `rerun` flag. Any structural change
(`insert` or `requires`) sets `rerun = true`; `finish()` and `start()` only
re-run the O(V + V·E) `dag()` pass when something has actually changed,
avoiding redundant recomputation between queries.
 
## Running the Tests
 
The test suite uses **JUnit 4** and exercises:
- A chain of jobs with dependencies added incrementally, checking that
  `finish()` and `start()` update correctly after each new constraint.
- Cycle detection (`finish()` and `start()` returning `-1` once a circular
  dependency is introduced).
- A single-job schedule with no dependencies.
Example (with JUnit 4 + Hamcrest on the classpath):
 
```bash
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar Schedule.java ScheduleTest.java
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore ScheduleTest
```
 
## Notes
 
- `requires` adds the edge in the direction "prerequisite → dependent"
  (`j.edgeList.add(this)`), so `edgeList` always points from a job to the
  jobs that depend on it.
- Cycle detection relies on comparing `ordered.size()` to `jobList.size()`
  after Kahn's algorithm terminates — any job left with `kahnDegree > 0` is
  part of (or downstream of) a cycle.
 
