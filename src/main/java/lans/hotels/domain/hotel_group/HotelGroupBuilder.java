package lans.hotels.domain.hotel_group;

import lans.hotels.domain.IBuilder;

public class HotelGroupBuilder implements IBuilder<HotelGroup>  {
    HotelGroup hotelGroup;

    public HotelGroupBuilder() {
        reset();
    }

    public HotelGroupBuilder(int id) {
        hotelGroup = new HotelGroup(id);
    }



    public void reset() {
        hotelGroup = new HotelGroup();
    }

    public HotelGroup getResult() {
        return this.hotelGroup;
    }
}
