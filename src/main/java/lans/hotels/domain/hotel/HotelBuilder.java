package lans.hotels.domain.hotel;

import lans.hotels.domain.IBuilder;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.utils.Phone;

public class HotelBuilder implements IBuilder<Hotel> {
    Integer id;
    Hotel hotel;
    Phone phone;
    String name;
    String email; // TODO: refactor email into value object.
    IDataSource dataSource;

    public HotelBuilder(IDataSource dataSource) {
        this.dataSource = dataSource;
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

    public HotelBuilder id(Integer id) {
        this.id = id;
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
        this.hotel = null;
    }

    @Override
    public Hotel getResult() {
        if (this.hotel == null) {
            this.hotel = new Hotel(name, phone, email, id, dataSource);
        }
        return this.hotel;
    }
}
