package lans.hotels.domain.hotel_group;

import lans.hotels.domain.AbstractReferenceObject;

import java.util.HashMap;

public class HotelGroup extends AbstractReferenceObject  {
    HashMap<Integer, BusinessDetail> businessDetails;
    protected HotelGroup() {
        super();
    }
    protected HotelGroup(int id) {
        super(id);
    }
}

