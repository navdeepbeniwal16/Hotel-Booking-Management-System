package lans.hotels.domain.hotel_group;

import lans.hotels.domain.ReferenceObject;

import java.util.HashMap;
import java.util.List;

public class HotelGroup {
    String name;
    HashMap<String, BusinessDetail> businessDetails;
    protected HotelGroup() {
//        super();
    }

    protected HotelGroup(int id, String name, List<BusinessDetail> businessDetails) {
//        super(id);
        this.name = name;
        businessDetails.stream().map(businessDetail -> this.businessDetails.put(businessDetail.type, businessDetail));
    }

    private HashMap<String, BusinessDetail> getBusinessDetails() {
        return this.businessDetails;
    }
}

