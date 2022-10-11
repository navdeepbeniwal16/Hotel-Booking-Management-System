package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user.Role;
import lans.hotels.domain.user.User;
import org.json.JSONObject;

public class OnboardHotelier extends UseCase {
    Integer id;
    User user;

    public OnboardHotelier(IDataSource dataSource, Integer id) {
        super(dataSource);
        this.id = id;
    }

    @Override
    public void doExecute() throws Exception {
        user = dataSource.find(User.class, id);
        if (user != null && user.getRole().isCustomer()) {
            user.setRole(Role.hotelier());
            succeed();
        } else {
            fail();
        }
    }

    @Override
    protected void constructResult() {
        try {
            responseData.put("id", id);
        } catch (Exception e) {
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }

}
