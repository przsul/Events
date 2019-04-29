package utp;

public class EventSign {
    private long id;
    private User user;
    private Event event;
    private String type;
    private String food;
    private String status;

    public EventSign() {
    }

    public EventSign(long id, User user, Event event, String type, String food, String status) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.type = type;
        this.food = food;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EventSign{" +
                "id=" + id +
                ", user=" + user +
                ", event=" + event +
                ", type='" + type + '\'' +
                ", food='" + food + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
