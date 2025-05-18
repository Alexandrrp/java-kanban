import java.util.Objects;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;

    public Epic(String name, String description) {
        super(name, Status.NEW, description);
        subtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksId;
    }

    public void addSubtaskId(int subtaskId) {
        subtasksId.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtasksId.remove(subtaskId);
    }

    @Override
    public String toString() {
        String result = "Epic{" + "subtasksId=" + subtasksId + ", name='" + super.getName() + '\''
                + ", id=" + super.getId();

        if (super.getDescription() == null) {
            result += ", description=null" + '\'' +
                    ", status=" + super.getStatus() + '}';
        } else {
            result += ", description='" + super.getDescription() + '\'' +
                    ", status=" + super.getStatus() + '}';
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus(), subtasksId);
    }
}