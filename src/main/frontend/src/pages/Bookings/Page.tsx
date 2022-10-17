import React, { useContext, useEffect, useState } from 'react';
import Booking from '../../types/BookingType';
import BookingsList from '../../components/layouts/BookingsList';
import AppContext from '../../context/AppContext';

const Bookings = () => {
  const { backend, user } = useContext(AppContext.GlobalContext);
  const emptyBookings: Booking[] = [];
  const [bookings, setBookings] = useState(emptyBookings);

  useEffect(() => {
    const setup = async () => {
      backend.getCustomerBookings(user.user).then(([bookings, message]: [Booking[], string]) => {
        console.log("/bookings", bookings, message);
        setBookings(bookings);
      })
    }
    setup();
  }, [])
  return (
    <div>
      <BookingsList bookings={bookings}></BookingsList>
    </div>
  );
};

export default Bookings;
