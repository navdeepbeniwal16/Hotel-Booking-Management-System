package lans.hotels.domain.hotel;

public class Hotel {
    private String name;
    private Integer UUID;
    public Hotel(Integer UUID, String name) {
        this.name = name;
        this.UUID = UUID;
    }
    public String getName() {
        return name;
    }

    public boolean isHotel(Hotel other) {
        return this.UUID == other.UUID;
    }
}
