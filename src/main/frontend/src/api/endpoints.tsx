import Hotel from '../types/HotelType';

const getAllHotels = async (): Promise<Hotel[]> => {
  const res = await fetch('/api/hotels');
  const data = await res.json();
  const hotels: Array<Hotel> = data.result;
  return hotels;
};

const endpoints = {
  getAllHotels,
};

export default endpoints;
