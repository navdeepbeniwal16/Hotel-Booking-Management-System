import React, { Dispatch } from 'react';

type Hotel = {
  address: String;
  phone: String;
  name: String;
  id: Number;
  email: String;
};

export type HotelState = {
  hotel: Hotel;
  setHotel: Dispatch<React.SetStateAction<Hotel>>;
};

export default Hotel;
