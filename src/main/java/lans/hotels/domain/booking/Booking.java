package lans.hotels.domain.booking;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomBookingSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.DateRange;

import java.util.ArrayList;
import java.util.HashMap;

public class Booking extends ReferenceObject {
    Integer hotel_id;
    Integer customer_id;
    DateRange date_range;
    Boolean is_active;
    String hotel_name;
    String customer_name;
    HashMap<Integer, RoomBooking> room_bookings;

    public Booking(IDataSource dataSource) throws UoWException {
        super(dataSource);
        markNew();
    }

    public Booking(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
    }

    public Booking(IDataSource dataSource,
                   Integer hotel_id,
                   Integer customer_id,
                   DateRange date_range,
                   Boolean is_active,
                   String hotel_name,
                   String customer_name) throws UoWException {
        super(dataSource);
        this.hotel_id = hotel_id;
        this.customer_id = customer_id;
        this.date_range = date_range;
        this.is_active = is_active;
        this.hotel_name = hotel_name;
        this.customer_name = customer_name;
        markNew();
        markLoaded();
    }

   public Booking(Integer id,
                  IDataSource dataSource,
                  Integer hotel_id,
                  Integer customer_id,
                  DateRange date_range,
                  Boolean is_active,
                  String hotel_name,
                  String customer_name) throws UoWException {
       super(id, dataSource);
       this.hotel_id = hotel_id;
       this.customer_id = customer_id;
       this.date_range = date_range;
       this.is_active = is_active;
       this.hotel_name = hotel_name;
       this.customer_name = customer_name;
       markClean();
       markLoaded();
   }

    private void initRoomBookings() {
        this.room_bookings = new HashMap<>();
    }

    public Integer getHotelId() {
        return hotel_id;
    }

    public void setHotelId(Integer hotelId) throws UoWException {
        this.hotel_id = hotelId;
        markDirty();
    }

    public Integer getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(Integer customer_id) throws UoWException {
        this.customer_id = customer_id;
        markDirty();
    }

    public DateRange getDateRange() {
        return date_range;
    }

    public void setDateRange(DateRange date_range) throws UoWException {
        this.date_range = date_range;
        markDirty();
    }

    public Boolean getActive() {
        return is_active;
    }

    public void setActive(Boolean is_active) throws UoWException {
        this.is_active = is_active;
        markDirty();
    }

    public String getHotelName() {
        return this.hotel_name;
    }

    public void setHotelName(String hotel_name) throws UoWException {
        this.hotel_name = hotel_name;
        markDirty();
    }

    public String getCustomerName() {
        return this.customer_name;
    }

    public void setCustomerName(String customer_name) throws UoWException {
        this.customer_name = customer_name;
        markDirty();
    }

    // Fetching room-bookings on the go, either locally from the object, or from the backend if not present locally
    public HashMap<Integer, RoomBooking> getRoomBookings() {
        if(isLoaded()) {
            return room_bookings;
        } else {
            loadRemainingData();
            return room_bookings;
        }
    }

    // Lazy load the roomBookings pertaining to object of this booking class
    private void loadBookings() {
        RoomBookingSearchCriteria criteria = new RoomBookingSearchCriteria();
        criteria.setBookingId(this.getId());
        try {
            ArrayList<RoomBooking> roomBookingArrayList = dataSource.findBySearchCriteria(RoomBooking.class, criteria);
            if(roomBookingArrayList.size() > 0) {
                markLoading();
                for(RoomBooking roomBooking: roomBookingArrayList) {
                    room_bookings.put(roomBooking.getId(), roomBooking);
                }
                markLoaded();
            }
        } catch (Exception e) {
            System.out.println("In Booking : " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void loadHotelName() {
        HotelSearchCriteria criteria = new HotelSearchCriteria();
        criteria.setId(this.getHotelId());
        try {
            ArrayList<Hotel> hotels = dataSource.findBySearchCriteria(Hotel.class, criteria);
            hotel_name = hotels.get(0).getName();
        } catch (Exception e) {
            System.out.println("In Booking: Exception while fetching hotel details");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadRemainingData() {
        loadBookings();
        loadHotelName();
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "hotelId=" + hotel_id +
                ", customerId=" + customer_id +
                ", dateRange=" + date_range +
                ", isActive=" + is_active +
                '}';
    }
}
