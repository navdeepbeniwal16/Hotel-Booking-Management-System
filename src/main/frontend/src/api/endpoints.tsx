import Hotel from '../types/HotelType';
import Room from '../types/RoomType';

const getHotelRooms = async (hotel_id: Number): Promise<Room[]> => {
  const res = await fetch('/api/hotels', {
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      search: {
        hotel_id,
      },
    }),
  });
  const data = await res.json();
  const rooms: Array<Room> = data.result;
  console.log('getHotelRooms:', rooms);
  return rooms;
};

const searchHotels = async (
  hotel_id: Number = -1,
  location: String = '',
  startDate: Date = new Date(),
  endDate: Date = new Date()
): Promise<Hotel[]> => {
  const res = await fetch('/api/hotels', {
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      search: {
        hotel_id,
        location,
        startDate: startDate.toLocaleDateString('en-GB'),
        endDate: endDate.toLocaleDateString('en-GB'),
      },
    }),
  });
  const data = await res.json();
  const hotels: Array<Hotel> = data.result;
  return hotels;
};

const getAllHotels = async (): Promise<Hotel[]> => {
  const res = await fetch('/api/hotels');
  const data = await res.json();
  const hotels: Array<Hotel> = data.result;
  return hotels;
};

const getAllRooms = async (): Promise<Room[]> => {
  const res = await fetch('/api/rooms');
  const data = await res.json();
  const rooms: Array<Room> = data.result;
  return rooms;
};

const endpoints = {
  getAllHotels,
  searchHotels,
  getHotelRooms,
  getAllRooms,
};

export default endpoints;
