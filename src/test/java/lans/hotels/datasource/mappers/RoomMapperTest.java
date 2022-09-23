package lans.hotels.datasource.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.Connection;
import java.sql.Statement;

class RoomMapperTest {
    @InjectMocks private Connection mockDbConnection;
    @Mock private Statement mockStatement;

    // TODO: refactor to test BEHAVIOUR, not implementation!
    protected static final String findStatement = "SELECT " + " id, " +
    " hotel_id, number, floor, is_active " +
    " FROM room " + "WHERE id = ? ";
}