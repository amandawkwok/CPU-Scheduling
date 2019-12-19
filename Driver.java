import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Please enter a file as the command line argument.");
        } else {
            String inputFile = args[0];
            CPUScheduler scheduler = new CPUScheduler();
            List<Process> processList = initializeDefaultProcesses(inputFile);

            List<Process> processes = getDefaultProcesses(processList);
            List<String> steps = scheduler.executeFCFS(processes);
            printStepsToCSV("fcfs", inputFile, steps);

            processes = getDefaultProcesses(processList);
            steps = scheduler.executeSJF(processes);
            printStepsToCSV("sjf", inputFile, steps);

            processes = getDefaultProcesses(processList);
            steps = scheduler.executeRoundRobin(processes, 20);
            printStepsToCSV("round_robin_20", inputFile, steps);

            processes = getDefaultProcesses(processList);
            steps = scheduler.executeRoundRobin(processes, 40);
            printStepsToCSV("round_robin_40", inputFile, steps);

            processes = getDefaultProcesses(processList);
            steps = scheduler.executeLottery(processes, 40);
            printStepsToCSV("lottery_40", inputFile, steps);

            System.out.println("Success! The files containing the CPU Scheduling steps have been generated.");
        }
    }

    private static List<Process> initializeDefaultProcesses(String fileName) {
        List<Process> defaultProcessList = new ArrayList<>();
        String[] line;
        String token, processId = "";
        int burstTime = 0, priority, lineNumber = 0;

        try {
            File inputFile = new File(fileName);
            Scanner inputScanner = new Scanner(inputFile);

            while (inputScanner.hasNextLine()) {
                line = inputScanner.nextLine().split(" ");
                token = line[0];
                lineNumber++;

                // If there is more than one token in the line
                if (line.length != 1) {
                    throw new Exception("e");
                }

                if (lineNumber % 3 == 1) {
                    processId = token;
                } else if (lineNumber % 3 == 2) {
                    burstTime = Integer.parseInt(token);
                } else {
                    priority = Integer.parseInt(token);
                    defaultProcessList.add(new Process(processId, burstTime, priority, 0));
                }
            }

            if (lineNumber % 3 != 0) {
                throw new Exception("e");
            }

            inputScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error! File not found.");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error! Please verify that each process is in the following format:" +
                    "\npid\nburstTime\npriority\npid\nburstTime\n...");
            System.exit(0);
        }

        return defaultProcessList;
    }

    private static List<Process> getDefaultProcesses(List<Process> baseProcesses) {
        List<Process> newProcesses = new ArrayList<>();
        for (Process p : baseProcesses) {
            newProcesses.add(new Process(p.getID(), p.getBurstTime(), p.getPriority(), p.getCompletionTime()));
        }

        return newProcesses;
    }

    private static void printStepsToCSV(String schedulerName, String inputFile, List<String> processSteps) {
        try {
            String path = "";
            if (inputFile.contains(File.separator)) {
                path = inputFile.substring(0, inputFile.lastIndexOf(File.separator) + 1);
                inputFile = inputFile.substring(inputFile.lastIndexOf(File.separator) + 1, inputFile.lastIndexOf("."));
            }

            inputFile = inputFile.replaceAll("\\.", "_");

            FileWriter fw = new FileWriter(path + schedulerName + "-" + inputFile + ".csv");
            fw.append("CpuTime,PID,StartingBurstTime,EndingBurstTime,CompletionTime\n");

            for (String steps : processSteps) {
                fw.append(steps);
                fw.append("\n");
            }

            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}