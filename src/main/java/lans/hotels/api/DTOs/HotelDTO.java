package lans.hotels.api.DTOs;

import jdk.jshell.spi.ExecutionControl;
import lans.hotels.domain.hotel.Hotel;
import org.json.JSONObject;

public class HotelDTO extends AbstractDTO<Hotel>{
    public HotelDTO(Hotel domainObject) {
        super(domainObject);
    }

    @Override
    protected JSONObject buildJson() throws ExecutionControl.NotImplementedException {
        return super.buildJson();
    }
}
