import java.util.Date;
import java.util.UUID;

public class Person {
    private final UUID id;
    private final Date arrivalTime;
    private Date queueTime1 = null;
    private Date queueTime2 = null;
    private Date finalTime = null;

    public Person(UUID id, Date arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
    }

    public UUID getId() {
        return id;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public Date getQueueTime1() {
        return queueTime1;
    }

    public void setQueueTime1(Date queueTime1) {
        this.queueTime1 = queueTime1;
    }

    public Date getQueueTime2() {
        return queueTime2;
    }

    public void setQueueTime2(Date queueTime2) {
        this.queueTime2 = queueTime2;
    }

    public Date getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(Date finalTime) {
        this.finalTime = finalTime;
    }
}
