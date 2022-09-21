import React, { useContext } from 'react';
import SearchForm from '../components/layouts/SearchForm';
import HotelsList from '../components/molecules/HotelsList';
import { useEffect, useState } from 'react';
import Hotel from '../types/HotelType';
import endpoints from '../api/endpoints';
import AppContext from '../context/AppContext';
import { useAuth0 } from '@auth0/auth0-react';

const Home = () => {
  const { isLoading, isAuthenticated } = useAuth0();
  const [hotels, setHotels] = useState<Hotel[]>([]);
  const { userMetadata, getAccessToken } = useContext(AppContext.GlobalContext);
  useEffect(() => {
    const getHoteliers = async () => {
      if (!isLoading && isAuthenticated && userMetadata.apiAccessToken !== '') {
        await getAccessToken();
        console.log(endpoints.getHoteliers(userMetadata.apiAccessToken));
      }
    };
    if (userMetadata.apiAccessToken !== '') {
      const fetchHotels = async () => {
        const fetchedHotels = await endpoints.getAllHotels();
        setHotels(fetchedHotels);
      };
      fetchHotels().catch(console.error);
    }
    getHoteliers().catch(console.error);
  }, [userMetadata.apiAccessToken, isLoading, isAuthenticated]);

  return (
    <div>
      <SearchForm></SearchForm>
      <HotelsList hotels={hotels}></HotelsList>
    </div>
  );
};

export default Home;
