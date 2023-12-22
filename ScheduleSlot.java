public class ScheduleSlot {
    private String subject;
    private String teacher;
    private String classroom;

    public ScheduleSlot(String subject, String teacher, String classroom) {
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
    }

    // Getters and setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
