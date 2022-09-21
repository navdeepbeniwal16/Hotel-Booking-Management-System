import React, { useContext } from 'react';
import SearchForm from '../components/layouts/SearchForm';
import HotelsList from '../components/molecules/HotelsList';
import { useEffect, useState } from 'react';
import Hotel from '../types/HotelType';
import endpoints from '../api/endpoints';
import AppContext from '../context/AppContext';

const Home = () => {
  const [hotels, setHotels] = useState<Hotel[]>([]);
  const { userMetadata } = useContext(AppContext.GlobalContext);
  useEffect(() => {
    if (userMetadata.apiAccessToken != '') {
      const fetchHotels = async () => {
        const fetchedHotels = await endpoints.getAllHotels();
        setHotels(fetchedHotels);
      };
      fetchHotels().catch(console.error);
    }
  }, [userMetadata.apiAccessToken]);
  return (
    <div>
      <SearchForm></SearchForm>
      <HotelsList hotels={hotels}></HotelsList>
    </div>
  );
};

export default Home;
