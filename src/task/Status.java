package task;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;
    public boolean isInProgress() {
        return this == IN_PROGRESS;
    }
}
