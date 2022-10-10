import React, { useContext } from 'react';
import SearchForm from '../components/layouts/SearchForm';
import HotelsList from '../components/molecules/HotelsList';
import { useEffect, useState } from 'react';
import Hotel from '../types/HotelType';
import AppContext from '../context/AppContext';

const Home = () => {
  const [hotels, setHotels] = useState<Hotel[]>([]);
  const { backend } = useContext(AppContext.GlobalContext);
  useEffect(() => {
    const fetchHotels = async () => {
      const fetchedHotels = await backend.getAllHotels();
      setHotels(fetchedHotels);
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
