import java.util.Date;

public interface CheckerInterface extends Runnable {
    void run();
    void process(Person person);

    static void finish(Person person) {
        if (person.getQueueTime1() == null) {
            person.setQueueTime1(new Date());
        } else if (person.getQueueTime2() == null) {
            person.setQueueTime2(new Date());
        } else {
            person.setFinalTime(new Date());
        }
    }
}
