import React, { useContext } from 'react';
import SearchForm from '../components/layouts/SearchForm';
import HotelsList from '../components/molecules/HotelsList';
import { useEffect, useState } from 'react';
import Hotel from '../types/HotelType';
import AppContext from '../context/AppContext';
import { filter, forEach, map } from 'lodash';

const Home = () => {
  const emptyHotels: Hotel[] = [];
  const [hotels, setHotels] = useState(emptyHotels);
  const { backend } = useContext(AppContext.GlobalContext);
  useEffect(() => {
    const fetchHotels = async () => {
      const _hotels = await backend.getAllHotels();
      const activeHotels: Hotel[] = [];
      forEach(_hotels, (hotel: Hotel) => {
        if (hotel.is_active) activeHotels.push(hotel);
      })
      setHotels(activeHotels);
      // setHotels([..._hotels]);
    };
    fetchHotels().catch(console.error);
  }, []);

  return (
    <div>
      {/* <SearchForm /> */}
      <HotelsList hotels={hotels}></HotelsList>
    </div>
  );
};

export default Home;
