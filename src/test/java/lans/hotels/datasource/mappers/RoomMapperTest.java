package lans.hotels.datasource.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.Connection;
import java.sql.Statement;

class RoomMapperTest {
    @InjectMocks private Connection mockDbConnection;
    @Mock private Statement mockStatement;

    protected static final String findStatement = "SELECT " +
    " room_number, room_floor, isActive, room_spec_id " +
    " FROM room " + " WHERE room_id = ? ";

    @Test
    void find_statement_is_correct() {
        // Arrange
        RoomMapper roomMapper = new RoomMapper(mockDbConnection);

        // Act
        String statement = roomMapper.findStatement();

        // Assert
        Assertions.assertEquals(findStatement, statement);
    }
}