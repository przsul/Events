package utp;

import java.io.Serializable;

public class EventSign implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	private User user;
	
    private Event event;

    private String type;
	
    private String food;
	
    private String status;

    public EventSign() {
    }

    public EventSign( User user, Event event, String type, String food, String status) {
        this.user = user;
        this.event = event;
        this.type = type;
        this.food = food;
        this.status = status;
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
                ", user=" + user +
                ", event=" + event +
                ", type='" + type + '\'' +
                ", food='" + food + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
