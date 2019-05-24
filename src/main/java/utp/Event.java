package utp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "events")
public class Event implements Serializable {

	@Transient
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long eventId;
	
	@Column(name = "name", nullable = false)
    private String name;
	
	@Column(name = "agend", nullable = false)
    private String agend;
	
	@Column(name = "time", nullable = false)
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
    	Timestamp.valueOf(LocalDateTime.parse(time,formatter));
        this.time = time;
    }

    @Override
    public String toString() {
        return name;
    }
}
