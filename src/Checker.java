import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker implements CheckerInterface {
    private static final Logger LOGGER = Logger.getLogger(Checker.class.getName());
    private final Line origin;
    private final Line[] destination;

    private int tick;

    private DateFormat dateFormat;

    private String name;

    public Checker(Line origin, Line[] destination, int tick, String name) {
        this.origin = origin;
        this.destination = destination;
        this.tick = tick;
        this.name = name;
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
    }

    public void run() {
        System.out.println("Running checker thread: "+name);
        Thread.currentThread().setName(name);
        while(true) {
            try {
                if (!origin.isEmpty()) {
                    int threadSleep = tick * (ThreadLocalRandom.current().nextInt(1,15));
                    Thread.sleep(threadSleep);
                    process(origin.take());
                } else {
                    Thread.sleep(tick);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.INFO, String.format("Thread %s Interrupted", Thread.currentThread().getName()));
                return;
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
