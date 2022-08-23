package lans.hotels.domain.hotel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HotelTest {
    @Test
    void Hotel_has_name() {
        // Arrange
        String name = "LANS";

        // Act
        Hotel hotel = new Hotel(1234, name);

        // Assert
        Assertions.assertEquals(name, hotel.getName());
    }

    @Test
    void Hotels_are_compared_on_UUID() {
        // Arrange
        String sameName = "LANS";
        Hotel hotel1 = new Hotel(12345, sameName);
        Hotel hotel2 = new Hotel(67890, sameName);
        // Act
        boolean isHotel1Hotel1 = hotel1.isHotel(hotel1);
        boolean isHotel1Hotel2 = hotel1.isHotel(hotel2);
        boolean isHotel2Hotel1= hotel2.isHotel(hotel1);

        // Assert
        Assertions.assertTrue(isHotel1Hotel1);
        Assertions.assertFalse(isHotel1Hotel2);
        Assertions.assertFalse(isHotel2Hotel1);
    }
}