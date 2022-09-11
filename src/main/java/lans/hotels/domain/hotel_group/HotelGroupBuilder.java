package lans.hotels.domain.hotel_group;

import lans.hotels.domain.IBuilder;
import lans.hotels.domain.exceptions.ReferenceObjectException;

import java.util.ArrayList;

public class HotelGroupBuilder implements IBuilder<HotelGroup>  {
    HotelGroup hotelGroup;
    String name;
    Integer uid;
    ArrayList<BusinessDetail> businessDetails;

    public HotelGroupBuilder() {
        reset();
    }

    public void uid(int id) {
        this.uid = id;
    }

    public void businessDetail(BusinessDetail businessDetail) {
        this.businessDetails.add(businessDetail);
    }

    public void name(String name) {
        this.name = name;
    }

    public void reset() {
        hotelGroup = null;
        name = "Default Hotel Group Name";
        uid = -1;
        businessDetails = new ArrayList<>();
    }

    public HotelGroup getResult() {
        if (this.hotelGroup == null) {
            this.hotelGroup = new HotelGroup(this.uid, this.name, this.businessDetails);
        }
        return this.hotelGroup;
    }
}
