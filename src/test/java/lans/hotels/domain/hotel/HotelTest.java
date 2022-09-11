package lans.hotels.domain.hotel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HotelTest {
    static final int id1 = 1;
    static final int id2 = 2;

    @Test
    void Hotel_has_name() {
        // Arrange
        String name = "LANS";

        // Act
        Hotel hotel = new Hotel(id1, name);

        // Assert
        Assertions.assertEquals(name, hotel.getName());
    }

    @Test
    void Compare_hotels_with_different_UUIDS() {
        // Arrange
        String sameName = "LANS";
        Hotel hotel1 = new Hotel(id1, sameName);
        Hotel hotel2 = new Hotel(id2, sameName);

        // Act
        boolean isHotel1Hotel2 = hotel1.equals(hotel2);
        boolean isHotel2Hotel1= hotel2.equals(hotel1);

        // Assert
        Assertions.assertFalse(isHotel1Hotel2);
        Assertions.assertFalse(isHotel2Hotel1);
    }
    @Test
    void Compare_hotels_with_the_same_UUIDS() {
        // Arrange
        String sameName = "LANS";
        Hotel hotel1 = new Hotel(id1, sameName);
        Hotel hotel2 = new Hotel(id1, sameName);

        // Act
        boolean isHotel1Hotel2 = hotel1.equals(hotel2);
        boolean isHotel2Hotel1= hotel2.equals(hotel1);

        // Assert
        Assertions.assertTrue(isHotel1Hotel2);
        Assertions.assertTrue(isHotel2Hotel1);
    }
}