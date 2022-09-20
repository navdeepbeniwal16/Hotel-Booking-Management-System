import Hotel from '../types/HotelType';
import Room from '../types/RoomType';

const getHotelRooms = async (hotelId: Number): Promise<Room[]> => {
  const res = await fetch('/api/hotels', {
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      search: {
        hotelId,
      },
    }),
  });
  const data = await res.json();
  const rooms: Array<Room> = data.result;
  return rooms;
};

const searchHotels = async (
  location: String,
  startDate: Date = new Date(),
  endDate: Date = new Date()
): Promise<Hotel[]> => {
  const res = await fetch('/api/hotels', {
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      search: {
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

const endpoints = {
  getAllHotels,
  searchHotels,
  getHotelRooms,
};

export default endpoints;
