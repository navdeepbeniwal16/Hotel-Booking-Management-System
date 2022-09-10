package lans.hotels.domain.hotel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class HotelTest {
    static UUID uuid1;
    static UUID uuid2;
    @BeforeAll
    static void beforeAllTests() {
        uuid1 = UUID.fromString("2a532027-2654-451e-866f-d8ab19e5a525");
        uuid2 = UUID.fromString("8fe5b331-82a7-459c-a9c1-8a026577b337");
    }

    @Test
    void Hotel_has_name() {
        // Arrange
        String name = "LANS";

        // Act
        Hotel hotel = new Hotel(uuid1, name);

        // Assert
        Assertions.assertEquals(name, hotel.getName());
    }

    @Test
    void Compare_hotels_with_different_UUIDS() {
        // Arrange
        String sameName = "LANS";
        Hotel hotel1 = new Hotel(uuid1, sameName);
        Hotel hotel2 = new Hotel(uuid2, sameName);

        // Act
        boolean isHotel1Hotel2 = hotel1.is(hotel2);
        boolean isHotel2Hotel1= hotel2.is(hotel1);

        // Assert
        Assertions.assertFalse(isHotel1Hotel2);
        Assertions.assertFalse(isHotel2Hotel1);
    }
    @Test
    void Compare_hotels_with_the_same_UUIDS() {
        // Arrange
        String sameName = "LANS";
        Hotel hotel1 = new Hotel(uuid1, sameName);
        Hotel hotel2 = new Hotel(uuid1, sameName);

        // Act
        boolean isHotel1Hotel2 = hotel1.is(hotel2);
        boolean isHotel2Hotel1= hotel2.is(hotel1);

        // Assert
        Assertions.assertTrue(isHotel1Hotel2);
        Assertions.assertTrue(isHotel2Hotel1);
    }
}