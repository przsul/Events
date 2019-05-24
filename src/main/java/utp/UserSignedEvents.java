package utp;

public class UserSignedEvents {
    private long eventId;
    private String name;
    private String agend;
    private String time;
    private String status;

    public UserSignedEvents() {
    }

    public UserSignedEvents(Event event, String status){
        this.eventId = event.getEventId();
        this.name = event.getName();
        this.agend = event.getAgend();
        this.time = event.getTime();
        this.status = status;
    }

    public UserSignedEvents(long eventId, String name, String agend, String time, String status) {
        this.eventId = eventId;
        this.name = name;
        this.agend = agend;
        this.time = time;
        this.status = status;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgend() {
        return agend;
    }

    public void setAgend(String agend) {
        this.agend = agend;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
