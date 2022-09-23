-- search hotels free between dates in given city
SELECT DISTINCT h.name FROM hotel h
    JOIN room r ON h.id = r.hotel_id
WHERE h.city = 'Melbourne' AND
        h.is_active = TRUE AND
        r.is_active = TRUE AND
        r.id not in
        (
            SELECT r.id FROM room_booking rb
                                 JOIN room r ON rb.room_id = r.id
                                 JOIN booking b ON rb.booking_id = b.id
            WHERE (start_date >= '2022-09-11' AND end_date <= '2022-09-15')
        )
;


-- find room types free in hotel
SELECT r.type AS room_type, r.number AS room_number,
       r.max_occupancy AS room_max_occupancy, r.bed_type AS room_bed_type,
       r.room_price AS room_price
FROM hotel h
         JOIN room r ON h.id = r.hotel_id
WHERE h.id = 14 AND
        h.is_active = TRUE AND
        r.is_active = TRUE AND
        r.id not in
        (
            SELECT r.id FROM room_booking rb
                                 JOIN room r ON r.id = rb.room_id
                                 JOIN booking b ON rb.booking_id = b.id
            WHERE (start_date >= '2022-09-11' AND end_date <= '2022-09-15')
        )
ORDER BY room_type
;
