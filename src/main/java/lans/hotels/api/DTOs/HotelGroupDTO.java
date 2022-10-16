package lans.hotels.api.DTOs;

import lans.hotels.domain.hotel_group.HotelGroup;
import org.json.JSONObject;

public class HotelGroupDTO extends AbstractDTO<HotelGroup> {
    public HotelGroupDTO(HotelGroup hotelGroup) {
        super(hotelGroup);
    }

    @Override
    protected JSONObject buildJson() {
        JSONObject hotelGroup = new JSONObject();
        hotelGroup.put("id", object().getId());
        hotelGroup.put("name", object().getName());
        hotelGroup.put("phone", object().getPhone());
        AddressDTO address = new AddressDTO(object().getAddress());
        hotelGroup.put("address", address.json());
        return hotelGroup;
    }
}
