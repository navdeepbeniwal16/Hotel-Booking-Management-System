package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.Role;
import lans.hotels.domain.user_types.User;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;

class GetAllUsersTest {
    @Mock
    Connection mockConnection;

    @Mock
    HttpSession mockSession;

    @Mock
    IDataSource mockDataSource;

    ArrayList<User> users;
    User user;

    String name = "name";
    String email = "name@example.com";
    Address address = new Address("line_1",
            "line_2",
            new District("VIC"),
            "Melbourne",
            3000);
    Role role = new Role(1);
    String contact = "12345";
    Integer age = 99;

    JSONObject responseJson = new JSONObject();
    JSONObject result = new JSONObject();
    JSONArray usersArray = new JSONArray();
    JSONObject userJson = new JSONObject();


    GetAllUsersTest() throws Exception {
    }

    @BeforeEach
    void Before_each() throws Exception {
        mockConnection = mock(Connection.class);
        mockSession = mock(HttpSession.class);
        mockDataSource = mock(IDataSource.class);

        // User domain object
        users = new ArrayList<>();
        user = new User(1, mockDataSource, name, email, address, role, contact, age,null,null);
        users.add(user);

        // JSON
        usersArray.put(userJson);
        result.put("users", usersArray);
        userJson.put("id", user.getId())
                .put("role", role.getId())
                .put("email", user.getEmail())
                .put("name", user.getName());
        responseJson.put("result", result);
        responseJson.put("success", true);
    }

    @Test
    void Should_succeed() {

        try {
            // Given
            Mockito
                    .when(mockDataSource.findAll(User.class))
                    .thenReturn(users);
            UseCase useCase = new GetAllUsers(mockDataSource);

            // When
            useCase.execute();

            // Then
            Assertions.assertTrue(useCase.succeeded());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void Should_marshall_correctly() {

        try {
            // Given
            Mockito
                    .when(mockDataSource.findAll(User.class))
                    .thenReturn(users);
            UseCase useCase = new GetAllUsers(mockDataSource);

            // When
            useCase.execute();

            // Then
            Assertions.assertEquals(responseJson.toString(), useCase.getResult().toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Assertions.fail();
        }
    }
}