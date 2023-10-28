import java.util.Date;
import java.util.UUID;

public class PersonBuilder {
    public PersonBuilder() {
    }

    public Person buildPerson() {
        return new Person(
                UUID.randomUUID(),
                new Date()
        );
    }
}
