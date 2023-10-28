import java.util.concurrent.LinkedBlockingQueue;

public class Line extends LinkedBlockingQueue<Person> {
    private final String name;
    public Line(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
