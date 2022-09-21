package lans.hotels.domain.booking;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelsSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomBookingSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.hotel.Hotel;
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
    String hotelName;

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

    // Fetching room-bookings on the go, either locally from the object, or from the backend if not present locally
    public HashMap<Integer, RoomBooking> getRoomBookings() {
        if(isLoaded()) {
            System.out.println("Room bookings already in Booking");
            return roomBookings;
        }
        else {
            System.out.println("Room bookings lazy loaded by Booking");
            return loadBookings();
        }
    }

    // Lazy load the roomBookings pertaining to object of this booking class
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

    public String getHotelName() {
        if(hotelName != null) return hotelName;
        else return loadHotelName();
    }

    public String loadHotelName() {
        // TODO: Get hotel_name from the backend
        HotelsSearchCriteria criteria = new HotelsSearchCriteria();
        criteria.setHotelId(this.getHotelId());
        try {
            ArrayList<Hotel> hotels = dataSource.findBySearchCriteria(Hotel.class, criteria);
            hotelName = hotels.get(0).getName();
        } catch (Exception e) {
            System.out.println("In Booking: Exception while fetching hotel details");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return hotelName;
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
