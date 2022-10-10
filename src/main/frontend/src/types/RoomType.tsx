import React, { Dispatch } from 'react';

export type Room = {
  id: number;
  hotel_id: number;
  type: string;
  occupancy: number;
  bed_type?: string;
  price?: number;
};

export type RoomState = {
  room: Room;
  setRoom: Dispatch<React.SetStateAction<Room>>;
};

export const defaultRoom: Room = {
  id: -1,
  hotel_id: -1,
  type: 'default_type',
  occupancy: -1,
  bed_type: 'default_bed',
  price: 0,
};

export const defaultRoomState: RoomState = {
  room: defaultRoom,
  setRoom: () => console.error('Error: cannot call setHotel() without context'),
};

export default Room;
