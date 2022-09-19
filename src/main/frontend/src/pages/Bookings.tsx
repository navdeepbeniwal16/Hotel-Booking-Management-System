import React from 'react';

import BookingsList from '../components/layouts/BookingsList';

const Bookings = () => {
  const bookings: any[] = [];
  return (
    <div>
      <BookingsList bookings={bookings}></BookingsList>
    </div>
  );
};

export default Bookings;
