import React, { createContext } from 'react';
import Booking from '../../types/BookingType';

const emptyBookings: Booking[] = [];

const loading = false;
const setLoading: React.Dispatch<React.SetStateAction<boolean>> = () => false;

const BookingContext = createContext({
  bookings: emptyBookings,
  loading,
  setLoading
});

export default BookingContext;