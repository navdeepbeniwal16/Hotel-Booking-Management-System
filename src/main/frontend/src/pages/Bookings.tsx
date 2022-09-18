import React from 'react';

import BookingsList from '../components/layouts/BookingsList';
import HotelsList from '../components/layouts/HotelsList';

const Bookings = () => {
  const bookings: any[] = [];
  return (
    <div>
      <BookingsList bookings={bookings}></BookingsList>
    </div>
  );
};

export default Bookings;
