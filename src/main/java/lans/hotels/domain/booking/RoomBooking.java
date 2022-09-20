package lans.hotels.domain.booking;

import lans.hotels.domain.DomainValueObject;
import lans.hotels.domain.IDataSource;

public class RoomBooking extends DomainValueObject {
    protected RoomBooking(IDataSource dataSource) {
        super(dataSource);
    }

    protected RoomBooking(Integer id, IDataSource dataSource) {
        super(id, dataSource);
    }
}
