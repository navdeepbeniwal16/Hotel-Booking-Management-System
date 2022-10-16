package lans.hotels.api.DTOs;

import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressDTO extends AbstractDTO<Address> {
    public AddressDTO(Address address) {
        super(address);
    }
    public AddressDTO(JSONObject json) {
        super(json);
    }
    @Override
    protected Address buildObject() throws JSONException  {
        String line_1 = json().getString("line_1");
        String line_2 = json().getString("line_2");
        String city = json().getString("city");
        Integer postcode = json().getInt("postcode");
        String district = json().getString("district");
        return new Address(line_1, line_2, new District(district), city, postcode);
    }

    @Override
    protected JSONObject buildJson() {
        JSONObject address = new JSONObject();
        address.put("line_1", object().getLine1());
        address.put("line_2", object().getLine2());
        address.put("district", object().getDistrict().toString());
        address.put("city", object().getCity());
        address.put("postcode", object().getPostCode());
        return address;
    }
}
