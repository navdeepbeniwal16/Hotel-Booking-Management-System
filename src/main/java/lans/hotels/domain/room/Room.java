package lans.hotels.domain.room;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.Address;

public class Room extends ReferenceObject {
    Integer hotel_id;
    String type;
    Integer max_occupancy;
    String bed_type;
    Integer room_price;
    Integer number;
    Boolean is_active;

    public Room(Integer id, IDataSource dataSource, Integer hotel_id) throws UoWException {
        super(id, dataSource);
        this.hotel_id = hotel_id;
        markClean();
        markGhost();
    }

    public Room(IDataSource dataSource,
                 Integer hotel_id,
                String type,
                Integer max_occupancy,
                String bed_type,
                Integer room_price,
                Integer number,
                Boolean is_active) throws UoWException {
        super(dataSource);
        this.hotel_id = hotel_id;
        this.type = type;
        this.max_occupancy = max_occupancy;
        this. bed_type = bed_type;
        this.room_price = room_price;
        this.number = number;
        this.is_active = is_active;
        markNew();
        markLoaded();
    }

    public Room(Integer id,
                IDataSource dataSource,
                Integer hotel_id,
                String type,
                Integer max_occupancy,
                String bed_type,
                Integer room_price,
                Integer number,
                Boolean is_active) throws UoWException {
        super(id,dataSource);
        this.hotel_id = hotel_id;
        this.type = type;
        this.max_occupancy = max_occupancy;
        this. bed_type = bed_type;
        this.room_price = room_price;
        this.number = number;
        this.is_active = is_active;
        markClean();
        markLoaded();
    }

    public Integer getHotelID() { return this.hotel_id; }

    public void setHotelID(Integer hotel_id) {
        this.hotel_id = hotel_id; }

    public String getType() {
        return this.type;
    }

    public void setType(String type) throws UoWException {
        this.type = type;
        markDirty();
    }

    public Integer getMaxOccupancy() { return this.max_occupancy; }

    public void setMaxOccupancy(Integer max_occupancy) {
        this.max_occupancy = max_occupancy; }

    public String getBedType() {
        return this.bed_type;
    }

    public void setBedType(String bed_type) throws UoWException {
        this.bed_type = bed_type;
        markDirty();
    }

    public Integer getRoomPrice() { return this.room_price; }

    public void setRoomPrice(Integer room_price) {
        this.room_price = room_price; }

    public Integer getRoomNumber() { return this.number; }

    public void setRoomNumber(Integer number) {
        this.number = number; }

    public Boolean getIsActive() { return this.is_active; }

    public void setIsActive(Boolean is_active) {
        this.is_active = is_active; }

    public void remove() throws UoWException {
        markRemoved();
    }
}
