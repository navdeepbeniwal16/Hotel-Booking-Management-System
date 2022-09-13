package lans.hotels.domain.hotel_group;

import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.IBuilder;

import java.util.ArrayList;

public class HotelGroupBuilder implements IBuilder<HotelGroup>  {
    HotelGroup hotelGroup;
    String name;
    Integer uid;
    ArrayList<BusinessDetail> businessDetails;

    public HotelGroupBuilder() {
        reset();
    }

    public HotelGroupBuilder uid(int id) {
        this.uid = id;
        return this;
    }

    public HotelGroupBuilder businessDetail(BusinessDetail businessDetail) {
        this.businessDetails.add(businessDetail);
        return this;
    }

    public HotelGroupBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void reset() {
        hotelGroup = null;
        name = "Default Hotel Group Name";
        uid = ReferenceObject.NOT_SET;
        businessDetails = new ArrayList<>();
    }

    @Override
    public HotelGroup getResult() {
        if (this.hotelGroup == null) {
            this.hotelGroup = new HotelGroup(this.uid, this.name, this.businessDetails);
        }
        return this.hotelGroup;
    }
}
