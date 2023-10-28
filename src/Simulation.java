import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Simulation {
    private static final int TICK_RATE = 100,
                             PASSENGERS = 50;

    private static final Logger LOGGER = Logger.getLogger(Simulation.class.getName());

    Line waitingGroup,
         lineA,
         lineB,
         lineC,
         processedGroup;

    Checker checkerA,
            checkerB,
            checkerC;

    SortingChecker controlChecker;

    int tickRate;

    public Simulation(int passengers, int tickRate) {
        this.tickRate = tickRate;

        PersonBuilder personBuilder = new PersonBuilder();

        waitingGroup = new Line("Waiting Group");
        lineA = new Line("Line A");
        lineB = new Line("Line B");
        lineC = new Line("Line C");
        processedGroup = new Line("Airport");

        controlChecker = new SortingChecker(waitingGroup, new Line[] {lineA, lineB}, createTimes(passengers), "controlChecker");

        checkerA = new Checker(lineA, new Line[]{lineC}, tickRate, "checkerA");
        checkerB = new Checker(lineB, new Line[]{lineC}, tickRate, "checkerB");
        checkerC = new Checker(lineC, new Line[]{processedGroup}, tickRate, "checkerC");


        createPassengers(passengers, personBuilder);
    }

    private void run() {
        System.out.println("Running simulation...");

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date begin = new Date();

        Thread threadA = new Thread(checkerA),
               threadB = new Thread(checkerB),
               threadC = new Thread(checkerC),
               threadD = new Thread(controlChecker);

        System.out.println("Starting simulation threads...");

        threadD.start();
        threadA.start();
        threadB.start();
        threadC.start();

        while (threadD.isAlive()) {
            try {
                threadD.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                // may have to rework this
                threadD.start();
            }
        }

        while ( threadA.isAlive() || threadB.isAlive()) {
            if(threadA.isAlive() && lineA.isEmpty()) {
                threadA.interrupt();
            }
            if(threadB.isAlive() && lineB.isEmpty()) {
                threadB.interrupt();
            }
        }

        while (threadC.isAlive()) {
            if (lineC.isEmpty()) {
                threadC.interrupt();
            }
        }

        Date finish = new Date();
        long diffMs = finish.getTime() - begin.getTime();
        long diffSec = diffMs/1000;
        LOGGER.log(Level.INFO, String.format("Finished.\nStart Time: %s\nFinish Time: %s\nFinal Run Time (Seconds): %s\nFinal Run Time (Ms): %s",
                dateFormat.format(begin),
                dateFormat.format(finish),
                diffSec, diffMs));
    }

    private TreeSet<Date> createTimes(int amount) {
        TreeSet<Date> times = new TreeSet<>();
        Random rand = new Random();
        int artificialSlowdownMult = 100;
        long min = System.currentTimeMillis();
        long max = min + tickRate * artificialSlowdownMult;
        Date dateMin = new Date(min);
        Date dateMax = new Date(max);

        while(times.size() < amount) {
            times.add(
                    new Date(rand.nextInt((int)(dateMax.getTime() - dateMin.getTime())) + dateMin.getTime())
            );
        }

        return times;
    }

    private void createPassengers(int passengers, PersonBuilder personBuilder) {
        for(int p = 0; p < passengers; p++) {
            waitingGroup.add(personBuilder.buildPerson());
        }
    }

    public static void main(String[] args) {

        Simulation simulation = new Simulation(PASSENGERS, TICK_RATE);
        simulation.run();
    }
}
