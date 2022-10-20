package lans.hotels.use_cases;
import lans.hotels.domain.IDataSource;
import lans.hotels.use_cases.UseCase;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;

public class CreateHotel extends UseCase {

    public CreateHotel(IDataSource dataSource) {
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
            aHG.put("created", succeeded);
        } catch (Exception e) {
            fail();
            aHG.put("created", false);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }

}
