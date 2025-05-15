import java.util.HashMap;

public abstract class Task {
    protected final long id;
    protected final String name;
    protected final String description;
    protected Status status;
    public Task(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}

class SimpleTask extends Task {
    public SimpleTask(long id, String name, String description) {
        super(id, name, description);
    }
}
