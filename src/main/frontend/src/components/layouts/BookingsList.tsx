import React from 'react';
import Row from 'react-bootstrap/Row';
import { map } from 'lodash';
import Booking from '../../types/BookingType';
interface IBookingListProps {
  bookings: Array<Booking>;
}

const BookingsList = ({ bookings }: IBookingListProps) => {
  return (
    <div className='p-3'>
      <Row xs={1} md={1} className='g-4'>
        BookingList
      </Row>
      <Row xs={1} md={1} className='g-4'>
        {map(bookings, (booking: Booking) => {
          <div>{booking.id}</div>;
        })}
      </Row>
    </div>
  );
};

export default BookingsList;
