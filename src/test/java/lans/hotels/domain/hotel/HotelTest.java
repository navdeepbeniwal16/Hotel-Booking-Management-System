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
        Hotel hotel = new Hotel(name);

        // Assert
        Assertions.assertEquals(name, hotel.getName());
    }
}