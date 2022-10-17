import React, { Dispatch } from 'react';

export type Room = {
  id: number;
  hotel_id: number;
  type: string;
  occupancy: number;
  bed_type?: string;
  price?: number;
  number?: number;
};

export type RoomState = {
  room: Room;
  setRoom: Dispatch<React.SetStateAction<Room>>;
};

export const defaultRoom: Room = {
  id: 0,
  hotel_id: 0,
  type: '',
  occupancy: 0,
  bed_type: '',
  price: 0,
  number: 0
};

export const defaultRoomState: RoomState = {
  room: defaultRoom,
  setRoom: () => console.error('Error: cannot call setHotel() without context'),
};

export default Room;
