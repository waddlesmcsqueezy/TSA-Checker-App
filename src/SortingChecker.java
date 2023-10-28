import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SortingChecker implements CheckerInterface {
    private static final Logger LOGGER = Logger.getLogger(SortingChecker.class.getName());
    private final Line origin;
    private final Line[] destination;

    private TreeSet<Date> removeTimes;

    private DateFormat dateFormat;

    private String name;

    public SortingChecker(Line origin, Line[] destination, TreeSet<Date> removeTimes, String name) {
        this.origin = origin;
        this.destination = destination;
        this.removeTimes = removeTimes;
        this.name = name;
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
    }

    public void setProcessTime(TreeSet<Date> times) {
        this.removeTimes = times;
    }

    public void run() {
        Thread.currentThread().setName(name);
        while(!removeTimes.isEmpty()) {
            if(System.currentTimeMillis() >= removeTimes.first().getTime()) {
                removeTimes.remove(removeTimes.first());
                try {
                    Person person = origin.take();
                    //System.out.println("Dest Size: "+destination[0].size() + " and " + destination[1].size());
                    process(person);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.INFO, String.format("Thread %s Interrupted", Thread.currentThread().getName()));
                    return;
                }
            }
        }
    }

    public void process(Person person) {
        CheckerInterface.finish(person);

        int dest = ThreadLocalRandom.current().nextInt(destination.length);

        if (destination.length > 1) {
            destination[dest].add(person);
        } else {
            destination[0].add(person);
        }

        LOGGER.log(Level.INFO, String.format("Passenger %s transferred from %s to %s at time %s by %s", String.valueOf(person.getId()), origin.getName(), destination[dest].getName(), new Date(), Thread.currentThread().getName()));
    }
}
