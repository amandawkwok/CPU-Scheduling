import java.util.*;

public class CPUScheduler {

    /**
     * Executes the FCFS CPU scheduling algorithm
     *
     * @param processList a list of processes waiting to be executed
     * @return the list of steps to be executed, with each step in the following format:
     * (CpuTime, PID, StartingBurstTime, EndingBurstTime, CompletionTime)
     * the last string in the list represents the average turn around time of all processes
     */
    public List<String> executeFCFS(List<Process> processList) {
        List<String> steps = new ArrayList<>();
        CPUTime = 0;

        for (Process process : processList) {
            process.setCompletionTime(CPUTime + process.getBurstTime());

            steps.add(CPUTime + "," + process.getID() + "," + process.getBurstTime() + "," + 0 + ","
                    + process.getCompletionTime());

            CPUTime += (CONTEXT_SWITCH + process.getBurstTime());
            process.setBurstTime(0);
        }

        steps.add("Average Completion Time:," + getAverageProcessTime(processList));
        return steps;
    }

    /**
     * Executes the lottery CPU scheduling algorithm
     *
     * @param processList a list of processes waiting to be executed
     * @param timeQuantum the time slice
     * @return the list of steps to be executed, with each step in the following format:
     * (CpuTime, PID, StartingBurstTime, EndingBurstTime, CompletionTime)
     * the last string in the list represents the average turn around time of all processes
     */
    public List<String> executeLottery(List<Process> processList, int timeQuantum) {
        List<List<Integer>> ticketRange = new ArrayList<>();
        List<Integer> individualTicketRange;
        List<String> steps = new ArrayList<>();
        int chosenTicket, lastTicket = 0, nextProcessIndex = 0, startingBurstTime, endingBurstTime, schedulingProgress = 0;
        Process nextProcess;
        Random random = new Random();
        CPUTime = 0;

        for (Process p : processList) {
            individualTicketRange = new ArrayList<>();
            individualTicketRange.add(lastTicket + 1);
            individualTicketRange.add(lastTicket += p.getBurstTime());
            ticketRange.add(individualTicketRange);
        }

        while (hasUnfinishedProcesses(processList)) {
            chosenTicket = random.nextInt(lastTicket) + 1;

            for (int i = 0; i < ticketRange.size(); i++) {
                individualTicketRange = ticketRange.get(i);
                if (individualTicketRange.get(0) <= chosenTicket && individualTicketRange.get(1) >= chosenTicket) {
                    nextProcessIndex = i;
                    break;
                }
            }

            nextProcess = processList.get(nextProcessIndex);
            startingBurstTime = nextProcess.getBurstTime();

            if (nextProcess.getBurstTime() == 0) {
                endingBurstTime = 0;
                schedulingProgress = 0;
            } else if (startingBurstTime <= timeQuantum) {
                endingBurstTime = 0;
                nextProcess.setBurstTime(0);
                schedulingProgress = startingBurstTime;
                nextProcess.setCompletionTime(CPUTime + startingBurstTime);
            } else {
                schedulingProgress = timeQuantum;
                endingBurstTime = startingBurstTime - timeQuantum;
                nextProcess.setBurstTime(endingBurstTime);
            }

            steps.add(CPUTime + "," + nextProcess.getID() + "," + startingBurstTime + "," + endingBurstTime + ","
                    + nextProcess.getCompletionTime());
            CPUTime += (CONTEXT_SWITCH + schedulingProgress);
            processList.set(nextProcessIndex, nextProcess);
        }
        steps.add("Average Completion Time:," + getAverageProcessTime(processList));

        return steps;
    }

    /**
     * Executes the round robin CPU scheduling algorithm
     *
     * @param processList a list of processes waiting to be executed
     * @param timeQuantum the time slice
     * @return the list of steps to be executed, with each step in the following format:
     * (CpuTime, PID, StartingBurstTime, EndingBurstTime, CompletionTime)
     * the last string in the list represents the average turn around time of all processes
     */
    public List<String> executeRoundRobin(List<Process> processList, int timeQuantum) {
        List<String> steps = new ArrayList<>();
        List<Integer> waitingQueue = new ArrayList<>(); //index of process in processList
        Process p;
        int startingBurstTime, endingBurstTime, nextProcessIndex, schedulingProgress;
        CPUTime = 0;

        // Add all processes to waiting queue
        for (int i = 0; i < processList.size(); i++) {
            waitingQueue.add(i);
        }

        // Process waiting queue
        while (!waitingQueue.isEmpty()) {
            nextProcessIndex = waitingQueue.get(0);
            p = processList.get(nextProcessIndex);
            startingBurstTime = p.getBurstTime();

            if (startingBurstTime <= timeQuantum) {
                schedulingProgress = startingBurstTime;
                endingBurstTime = 0;
                p.setCompletionTime(CPUTime + schedulingProgress);
            } else {
                schedulingProgress = timeQuantum;
                endingBurstTime = startingBurstTime - timeQuantum;
                p.setBurstTime(endingBurstTime);
                waitingQueue.add(nextProcessIndex);
            }

            waitingQueue.remove(waitingQueue.get(0));
            processList.set(nextProcessIndex, p);

            steps.add(CPUTime + "," + p.getID() + "," + startingBurstTime + "," + endingBurstTime + ","
                    + p.getCompletionTime());

            CPUTime += (schedulingProgress + CONTEXT_SWITCH);

        }

        steps.add("Average Completion Time:," + getAverageProcessTime(processList));
        return steps;
    }

    /**
     * Executes the shortest job first CPU scheduling algorithm
     *
     * @param processList a list of processes waiting to be executed
     * @return the list of steps to be executed, with each step in the following format:
     * (CpuTime, PID, StartingBurstTime, EndingBurstTime, CompletionTime)
     * the last string in the list represents the average turn around time of all processes
     */
    public List<String> executeSJF(List<Process> processList) {
        List<String> steps = new ArrayList<>();
        Process smallestProcess;
        int smallestProcessIndex;
        CPUTime = 0;

        while (hasUnfinishedProcesses(processList)) {
            smallestProcessIndex = getIndexOfSmallestProcess(processList);
            smallestProcess = processList.get(smallestProcessIndex);

            smallestProcess.setCompletionTime(CPUTime + smallestProcess.getBurstTime());

            steps.add(CPUTime + "," + smallestProcess.getID() + "," + smallestProcess.getBurstTime() + "," + 0 + ","
                    + smallestProcess.getCompletionTime());

            CPUTime += (CONTEXT_SWITCH + smallestProcess.getBurstTime());
            smallestProcess.setBurstTime(0);
            processList.set(smallestProcessIndex, smallestProcess);
        }

        steps.add("Average Completion Time:," + getAverageProcessTime(processList));
        return steps;
    }

    private boolean hasUnfinishedProcesses(List<Process> processList) {
        for (Process process : processList) {
            if (process.getBurstTime() != 0) {
                return true;
            }
        }
        return false;
    }

    private float getAverageProcessTime(List<Process> processList) {
        int totalCompletionTime = 0;
        int listSize = processList.size();

        for (Process process : processList) {
            totalCompletionTime += process.getCompletionTime();
        }

        return (float) totalCompletionTime / listSize;
    }

    private int getIndexOfSmallestProcess(List<Process> processList) {
        int smallestProcessIndex = 0, burstTime;
        int smallestBurstTime = Integer.MAX_VALUE;

        for (int i = 0; i < processList.size(); i++) {
            burstTime = processList.get(i).getBurstTime();

            if (burstTime < smallestBurstTime && burstTime != 0) {
                smallestBurstTime = burstTime;
                smallestProcessIndex = i;
            }
        }
        return smallestProcessIndex;
    }

    private static final int CONTEXT_SWITCH = 3;
    private int CPUTime;
}
