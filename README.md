# CPU Scheduling

This project serves to compare the average turn around times of the following CPU Scheduling Algorithms:
   - First Come First Serve (FCFS)
   - Shortest Job First (SJF)
   - Round Robin with Time Quantum = 20
   - Round Robin with Time Quantum = 40
   - Lottery with Time Quantum = 40

## Running the tests

### Compilation:

```
javac Driver.java
```

### To run the program, use the following format: java Driver *fileName*

```
java Driver testdata1.txt
```
The file must be in the following format:

```
pid1
burstTime
priority
pid2
burstTime
priotity
...
```

### Result:

After running the program, five .csv files will be created in the same location as the test file.
These files show the output of each scheduler's execution and are named using the following format: *scheduler_name-testfile_name.csv*
