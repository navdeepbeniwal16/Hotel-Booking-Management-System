import React from 'react';
import SearchForm from '../components/layouts/SearchForm';
import HotelsList from '../components/molecules/HotelsList';
import { useEffect, useState } from 'react';
import Hotel from '../types/HotelType';
import endpoints from '../api/endpoints';

const Home = () => {
  const [hotels, setHotels] = useState<Hotel[]>([]);
  useEffect(() => {
    const fetchHotels = async () => {
      const fetchedHotels = await endpoints.getAllHotels();
      setHotels(fetchedHotels);
      console.log(hotels);
    };
    fetchHotels().catch(console.error);
  }, []);
  return (
    <div>
      <SearchForm></SearchForm>
      <HotelsList hotels={hotels}></HotelsList>
    </div>
  );
};

export default Home;
