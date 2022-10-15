package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user.Role;
import lans.hotels.domain.user.User;
import org.json.JSONObject;

public class OnboardHotelier extends UseCase {
    public OnboardHotelier(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void doExecute() throws Exception {
        succeed();
    }

    @Override
    protected void constructResult() {
        try {
            responseData.put("upgrade_to_hotelier", true);
        } catch (Exception e) {
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }

}
