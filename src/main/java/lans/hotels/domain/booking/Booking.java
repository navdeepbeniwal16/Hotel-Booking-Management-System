package lans.hotels.domain.booking;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.DateRange;

import java.util.HashMap;

public class Booking extends ReferenceObject {
    Integer hotelId;
    Integer customerId;
    DateRange dateRange;
    HashMap<Integer, RoomBooking> roomBookings;

    public Booking(IDataSource dataSource) throws UoWException {
        super(dataSource);
        markNew();
    }

    public Booking(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
    }

   public Booking(Integer id, IDataSource dataSource, Integer hotelId, Integer customerId, DateRange dateRange){
       super(id, dataSource);
       this.hotelId = hotelId;
       this.customerId = customerId;
       this.dateRange = dateRange;
       initRoomBookings();
   }

    private void initRoomBookings() {
        this.roomBookings = new HashMap<>();
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public void remove() throws UoWException {
        markRemoved();
    }
}
