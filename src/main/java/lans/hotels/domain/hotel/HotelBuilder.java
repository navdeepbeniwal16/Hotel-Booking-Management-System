package lans.hotels.domain.hotel;

import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.IBuilder;
import lans.hotels.domain.utils.Phone;

public class HotelBuilder implements IBuilder<Hotel> {
    Integer uid;
    Hotel hotel;
    Phone phone;
    String name;
    String email; // TODO: refactor email into value object.

    public HotelBuilder() {
        reset();
    }

    public HotelBuilder phone(Phone phone) {
        this.phone = phone;
        return this;
    }

    public HotelBuilder email(String email) {
        this.email = email;
        return this;
    }

    public HotelBuilder uid(int id) {
        this.uid = id;
        return this;
    }

    public HotelBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void reset() {
        this.phone = new Phone(0,0,0);
        this.name = "Default Hotel Name";
        this.email = "example@email.com";
        this.uid = ReferenceObject.NOT_SET;
        this.hotel = null;
    }

    @Override
    public Hotel getResult() {
        if (this.hotel == null) {
            this.hotel = new Hotel(this.uid, this.name, this.phone, this.email);
        }
        return this.hotel;
    }
}
