import React from 'react';
import Row from 'react-bootstrap/Row';

interface IBookingListProps {
  bookings: Array<Object>;
}

const BookingsList = ({ bookings }: IBookingListProps) => {
  return (
    <div className='p-3'>
      <Row xs={1} md={1} className='g-4'>
        BookingList
      </Row>
    </div>
  );
};

export default BookingsList;
