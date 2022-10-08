package lans.hotels.domain.booking;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.DomainValueObject;
import lans.hotels.domain.IDataSource;

public class RoomBooking extends DomainValueObject {
    private Integer roomId;
    private Boolean isActive;
    private String mainGuest;
    private Integer numOfGuests;

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
        markNew();
    }

    public RoomBooking(Integer id,
                       IDataSource dataSource,
                       Integer roomId,
                       Boolean isActive,
                       String mainGuest,
                       Integer numOfGuests) {
        super(id, dataSource);
        this.roomId = roomId;
        this.isActive = isActive;
        this.mainGuest = mainGuest;
        this.numOfGuests = numOfGuests;
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
}
