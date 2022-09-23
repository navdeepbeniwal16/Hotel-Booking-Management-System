-- search hotels free between dates in given city
SELECT DISTINCT h.name FROM hotel h
    JOIN (room_spec rs JOIN room r ON rs.id = r.room_spec_id)
    ON h.id = rs.hotel_id
WHERE h.city = 'Melbourne' AND
        h.is_active = TRUE AND
        r.is_active = TRUE AND
        r.id not in
        (
            SELECT r.id FROM room_booking rb
                                 JOIN (room r JOIN room_spec rs on r.room_spec_id = rs.id)
                                      ON r.id = rb.room_id
                                 JOIN booking b ON rb.booking_id = b.id
            WHERE (start_date >= '2022-09-11' AND end_date <= '2022-09-15')
        )
;


-- find room types free in hotel
SELECT rs.type AS room_type, r.number AS room_number, r.floor AS room_floor,
       rs.max_occupancy AS room_max_occupancy, rs.bed_type AS room_bed_type,
       rs.room_price AS room_price
FROM hotel h
        JOIN (room_spec rs JOIN room r ON rs.id = r.room_spec_id)
        ON h.id = rs.hotel_id
WHERE h.id = 9 AND
        h.is_active = TRUE AND
        r.is_active = TRUE AND
        r.id not in
        (
            SELECT r.id FROM room_booking rb
                                 JOIN (room r JOIN room_spec rs on r.room_spec_id = rs.id)
                                      ON r.id = rb.room_id
                                 JOIN booking b ON rb.booking_id = b.id
            WHERE (start_date >= '2022-09-11' AND end_date <= '2022-09-15')
        )
ORDER BY room_type
;
