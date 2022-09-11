package lans.hotels.domain.hotel_group;

import lans.hotels.domain.AbstractReferenceObject;

import java.util.HashMap;
import java.util.List;

public class HotelGroup extends AbstractReferenceObject  {
    String name;
    HashMap<String, BusinessDetail> businessDetails;
    protected HotelGroup() {
        super();
    }

    protected HotelGroup(int id, String name, List<BusinessDetail> businessDetails) {
        super(id);
        this.name = name;
        businessDetails.stream().map(businessDetail -> this.businessDetails.put(businessDetail.type, businessDetail));
    }

    private HashMap<String, BusinessDetail> getBusinessDetails() {
        return this.businessDetails;
    }
}

