public class Process {
    public Process (String id, int burstTime, int priority, int completionTime) {
        _id = id;
        _burstTime = burstTime;
        _priority = priority;
        _completionTime = completionTime;
    }

    public String getID () {
        return _id;
    }

    public int getBurstTime () {
        return _burstTime;
    }

    public int getPriority () {
        return _priority;
    }

    public int getCompletionTime () {
        return _completionTime;
    }

    public void setID (String id) {
        _id = id;
    }

    public void setBurstTime (int burstTime) {
        _burstTime = burstTime;
    }

    public void setPriority (int priority) {
        _priority = priority;
    }

    public void setCompletionTime (int completionTime) {
        _completionTime = completionTime;
    }

    private String _id;
    private int _burstTime;
    private int _priority;
    private int _completionTime;
}
