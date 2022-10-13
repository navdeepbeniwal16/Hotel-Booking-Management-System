package lans.hotels.domain.booking;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.DomainValueObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.room.Room;

public class RoomBooking extends DomainValueObject {
    Integer roomId;
    Boolean isActive;
    String mainGuest;
    Integer numOfGuests;
    Integer version;

    public RoomBooking(IDataSource dataSource) throws UoWException {
        super(dataSource);
        markNew();
    }

    public RoomBooking(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
    }

    public RoomBooking(IDataSource dataSource,
                       Integer roomId,
                       String mainGuest,
                       Integer numOfGuests) throws UoWException {
        super(dataSource);
        this.roomId = roomId;
        this.isActive = true;
        this.mainGuest = mainGuest;
        this.numOfGuests = numOfGuests;
    }

    public RoomBooking(Integer id,
                       IDataSource dataSource,
                       Integer roomId,
                       Boolean isActive,
                       String mainGuest,
                       Integer numOfGuests,
                       Integer version) throws UoWException {
        super(id, dataSource);
        this.roomId = roomId;
        this.isActive = isActive;
        this.mainGuest = mainGuest;
        this.numOfGuests = numOfGuests;
        this.version = version;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) throws UoWException {
        this.roomId = roomId;
        markDirty();
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) throws UoWException {
        isActive = active;
        markDirty();
    }

    public String getMainGuest() {
        return mainGuest;
    }

    public void setMainGuest(String mainGuest) throws UoWException {
        this.mainGuest = mainGuest;
        markDirty();
    }

    public Integer getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(Integer numOfGuests) throws UoWException {
        this.numOfGuests = numOfGuests;
        markDirty();
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) throws UoWException {
        this.version = version;
        markDirty();
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    @Override
    public String toString() {
        return "RoomBooking{" +
                "id=" + getId() +
                ", roomId=" + roomId +
                ", isActive=" + isActive +
                ", mainGuest='" + mainGuest + '\'' +
                ", numOfGuests=" + numOfGuests +
                '}';
    }

    @Override
    public int compareTo(AbstractDomainObject other) {
        if (other.getClass() == Room.class || other.getClass() == Booking.class) {
            return -1;
        }
        return super.compareTo(other);
    }
}
