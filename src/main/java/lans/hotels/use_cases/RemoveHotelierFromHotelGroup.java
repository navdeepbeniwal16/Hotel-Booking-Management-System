package lans.hotels.use_cases;
import lans.hotels.domain.IDataSource;
import org.json.JSONObject;

public class RemoveHotelierFromHotelGroup extends UseCase {

    public RemoveHotelierFromHotelGroup(IDataSource dataSource) {
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
                aHG.put("removed", succeeded);
            }
        } catch (Exception e) {
            aHG.put("removed", succeeded);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }

}
