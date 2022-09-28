package lans.hotels.use_cases;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangeHotelStatus extends UseCase {

    Integer hotel_id;
    ArrayList<Hotel> hotels;
    Boolean is_active;

    public ChangeHotelStatus(IDataSource dataSource,Integer hotel_id,Boolean is_active) {
        super(dataSource);
        this.hotel_id = hotel_id;
        this.hotels = new ArrayList<>();
        this.is_active = is_active;
    }

    @Override
    public void doExecute() throws Exception {
        HotelSearchCriteria criteria = new HotelSearchCriteria();
        criteria.setId(hotel_id);


        try {
            hotels = dataSource.findBySearchCriteria(Hotel.class, criteria);

            if(hotels.size() > 0) {
                Hotel hotel = hotels.get(0);

                if (is_active == !(hotel.getIsActive())) {
                    hotel.setIs_Active(!hotel.getIsActive());
                }
            }
            succeed();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void constructResult() {
        JSONObject resposeObject = new JSONObject();
        try {
            if (succeeded) {
                resposeObject.put("updated", succeeded);
            }
        } catch (Exception e) {
            resposeObject.put("updated", succeeded);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }


}
