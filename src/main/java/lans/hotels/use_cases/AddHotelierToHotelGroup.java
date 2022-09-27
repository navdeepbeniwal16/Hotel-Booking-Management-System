package lans.hotels.use_cases;
import lans.hotels.domain.IDataSource;
import org.json.JSONObject;

public class AddHotelierToHotelGroup extends UseCase {

    public AddHotelierToHotelGroup(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void doExecute() throws Exception {
        succeed();
    }

    @Override
    protected void constructResult() {
        JSONObject aHG = new JSONObject();
        try {
            if (succeeded) {
                aHG.put("created", succeeded);
            }
        } catch (Exception e) {
            aHG.put("created", succeeded);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }

}
