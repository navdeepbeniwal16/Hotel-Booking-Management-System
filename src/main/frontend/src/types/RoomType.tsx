import React, { Dispatch } from 'react';

type Room = {
  id: Number;
  hotelId: Number;
  type: String;
  occupancy: Number;
  bed?: String;
  view?: String;
  price?: String;
};

export type RoomState = {
  room: Room;
  setRoom: Dispatch<React.SetStateAction<Room>>;
};

export const defaultRoom: Room = {
  id: -1,
  hotelId: -1,
  type: '',
  occupancy: -1,
  bed: '',
  view: '',
  price: '',
};

export const defaultRoomState: RoomState = {
  room: defaultRoom,
  setRoom: () => console.error('Error: cannot call setHotel() without context'),
};

export default Room;
