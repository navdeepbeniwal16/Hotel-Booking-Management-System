import React, { Dispatch } from 'react';

export type Room = {
  room_id: Number;
  hotel_id: Number;
  type: String;
  occupancy: Number;
  bed_type?: String;
  price?: Number;
};

export type RoomState = {
  room: Room;
  setRoom: Dispatch<React.SetStateAction<Room>>;
};

export const defaultRoom: Room = {
  room_id: -1,
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
