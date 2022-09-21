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
    Boolean isActive;
    HashMap<Integer, RoomBooking> roomBookings;

    public Booking(IDataSource dataSource) throws UoWException {
        super(dataSource);
        markNew();
    }

    public Booking(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
    }

   public Booking(Integer id, IDataSource dataSource, Integer hotelId, Integer customerId, DateRange dateRange, Boolean isActive){
       super(id, dataSource);
       this.hotelId = hotelId;
       this.customerId = customerId;
       this.dateRange = dateRange;
       this.isActive = isActive;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "hotelId=" + hotelId +
                ", customerId=" + customerId +
                ", dateRange=" + dateRange +
                ", isActive=" + isActive +
                '}';
    }
}
