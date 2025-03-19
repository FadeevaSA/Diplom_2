import com.github.javafaker.Faker;
import ru.stellarburgers.models.UserData;

public class UserFactory {
    private Faker faker;

    public UserFactory() {
        faker = new Faker();
    }

    public UserData createRandomUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        return new UserData(email, password, name);
    }
}
