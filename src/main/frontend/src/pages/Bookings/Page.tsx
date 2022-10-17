import React from 'react';
import Booking from '../../types/BookingType';
import BookingsList from '../../components/layouts/BookingsList';

const Bookings = () => {
  const bookings: Booking[] = [];
  return (
    <div>
      <BookingsList bookings={bookings}></BookingsList>
    </div>
  );
};

export default Bookings;
