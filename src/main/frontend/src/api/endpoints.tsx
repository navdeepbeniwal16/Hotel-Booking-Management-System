import Hotel from '../types/HotelType';
import Room from '../types/RoomType';
import Hotelier from '../types/HotelierType';

const getHoteliers = async (accessToken: string): Promise<Hotelier[]> => {
  const res = await fetch('/api/hoteliers', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
  });
  const data = await res.json();
  const hoteliers: Array<Hotelier> = data.result;
  console.log('getHoteliers:', hoteliers);
  return hoteliers;
};

const getHotelRooms = async (
  hotel_id: Number,
  headers: HeadersInit = {}
): Promise<Room[]> => {
  const res = await fetch('/api/hotels', {
    headers: {
      'Content-Type': 'application/json',
      ...headers,
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

const getAllHotels = async (headers: HeadersInit = {}): Promise<Hotel[]> => {
  const res = await fetch('/api/hotels', {
    headers,
  });
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
  getHoteliers,
};

export default endpoints;
