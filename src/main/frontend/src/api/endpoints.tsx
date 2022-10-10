import Hotel from '../types/HotelType';
import Room from '../types/RoomType';
import Hotelier from '../types/HotelierType';
import HotelGroupHotelier from '../types/HotelGroupHotelier';
import UserDataType from '../types/UserDataType';

// const getAllUsers = async (accessToken: string): Promise<UserDataType[]> => {
//   const res = await fetch('/api/users', {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: `Bearer ${accessToken}`,
//     },
//   });
//   const data = await res.json();
//   const users: Array<UserDataType> = data.result.users;
//   return users;
// };

const removeHotelierFromHotelGroup = async (
  accessToken: string,
  id: number
): Promise<Response> => {
  const res = await fetch('/api/hotelgrouphoteliers', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify({ hotel_group_hotelier: { id } }),
  });
  return res;
};

const getHotelGroupHoteliers = async (
  accessToken: string
): Promise<HotelGroupHotelier[]> => {
  const res = await fetch('/api/hotelgrouphoteliers', {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
  const data = await res.json();
  const hgh: HotelGroupHotelier[] = data.result;
  return hgh;
};

const getHoteliers = async (accessToken: string): Promise<Hotelier[]> => {
  const res = await fetch('/api/users', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify({
      search: { type: 'hotelier' },
    }),
  });
  const data = await res.json();
  const hoteliers: Array<Hotelier> = data.result.users;
  return hoteliers;
};

const getHotelRooms = async (
  hotel_id: number,
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
  hotel_id = -1,
  location = '',
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

// const getAllHotels = async (headers: HeadersInit = {}): Promise<Hotel[]> => {
//   const res = await fetch('/api/hotels', {
//     headers,
//   });
//   const data = await res.json();
//   const hotels: Array<Hotel> = data.result;
//   return hotels;
// };

// const getAllRooms = async (): Promise<Room[]> => {

// };

const endpoints = {
  searchHotels,
  getHotelRooms,
  getHoteliers,
  getHotelGroupHoteliers,
  removeHotelierFromHotelGroup,
};

export default endpoints;
