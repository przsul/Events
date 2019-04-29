package utp;


public class Event {
    private long eventId;
    private String name;
    private String agend;
    private String time;

    public Event() {
    }

    public Event(long eventId, String name, String agend, String time) {
        this.eventId = eventId;
        this.name = name;
        this.agend = agend;
        this.time = time;
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

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", name='" + name + '\'' +
                ", agend='" + agend + '\'' +
                ", time=" + time +
                '}';
    }
}
