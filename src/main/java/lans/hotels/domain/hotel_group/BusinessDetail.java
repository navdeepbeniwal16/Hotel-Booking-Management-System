package lans.hotels.domain.hotel_group;

import lans.hotels.domain.AbstractReferenceObject;

public class BusinessDetail extends AbstractReferenceObject {
    String type;
    String description;

    public BusinessDetail(int id, String type, String description) {
        super(id);
        this.type = type;
        this.description = description;
    }
}
