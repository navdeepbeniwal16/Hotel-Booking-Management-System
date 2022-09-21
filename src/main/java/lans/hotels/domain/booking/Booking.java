package lans.hotels.domain.booking;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.RoomBookingSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.DateRange;

import java.util.ArrayList;
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

    public void setHotelId(Integer hotelId) throws UoWException {
        this.hotelId = hotelId;
        markDirty();
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) throws UoWException {
        this.customerId = customerId;
        markDirty();
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) throws UoWException {
        this.dateRange = dateRange;
        markDirty();
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) throws UoWException {
        isActive = active;
        markDirty();
    }


    // TODO: Check with @Levi, if this is a possible way to implement LazyLoading
    public HashMap<Integer, RoomBooking> getRoomBookings() {
        if(isLoaded()) return roomBookings;
        else return loadBookings();
    }

    private HashMap<Integer, RoomBooking> loadBookings() {
        RoomBookingSearchCriteria criteria = new RoomBookingSearchCriteria();
        criteria.setBookingId(this.getId());
        try {
            ArrayList<RoomBooking> roomBookingArrayList = dataSource.findBySearchCriteria(RoomBooking.class, criteria);
            if(roomBookingArrayList.size() > 0) {
                markLoading();
                for(RoomBooking roomBooking: roomBookingArrayList) {
                    roomBookings.put(roomBooking.getId(), roomBooking);
                }
                markLoaded();
            }
        } catch (Exception e) {
            System.out.println("In Booking : " + e.getMessage());
            e.printStackTrace();
        }

        return roomBookings;
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
