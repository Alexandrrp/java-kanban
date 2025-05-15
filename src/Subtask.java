public class Subtask extends Task {
    private final long epicId;

    public Subtask(long id, String name, String description, long epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                "}" + super.toString();
    }
}