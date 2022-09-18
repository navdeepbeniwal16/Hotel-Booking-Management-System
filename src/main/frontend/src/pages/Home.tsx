import React from 'react';
import SearchForm from '../components/layouts/SearchForm';
import HotelsList from '../components/layouts/HotelsList';

const Home = () => {
  return (
    <div>
      <SearchForm></SearchForm>
      <HotelsList></HotelsList>
    </div>
  );
};

export default Home;
