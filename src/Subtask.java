import java.util.Objects;
public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, Status status, String description, int epicId) {
        super(name, status, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String result = "Subtask{" + "epicId=" + epicId + ", name='" + super.getName() + '\''
                + ", id=" + super.getId();

        if (super.getDescription() == null) {
            result += ", description=null" + ", status=" + super.getStatus() + '}';
        } else {
            result += ", description='" + super.getDescription() + '\'' + ", status=" + super.getStatus() + '}';
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus(), epicId);
    }
}