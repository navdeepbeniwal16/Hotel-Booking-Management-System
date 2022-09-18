import React from 'react';
import Row from "react-bootstrap/Row";

interface IBookingListProps {
  bookings: Array<Object>;
}

export const BookingsList = ({ bookings }: IBookingListProps) => {
  return (
    <div className="p-3">
        {
            bookings.map()
        }

      <Row xs={1} md={1} className="g-4">
        {Array.from({ length: 4 }).map((_, idx) => ())}
      </Row>
    </div>
  );
};
