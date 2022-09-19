import React from 'react';
import { useParams } from 'react-router-dom';
import AppContext from '../context/AppContext';
import { useContext } from 'react';

const Hotel = () => {
  const params = useParams();
  const hotelState = useContext(AppContext.GlobalContext).hotel;

  return (
    <>
      <h1>{hotelState.hotel.name}</h1>

      <div>
        <p>{hotelState.hotel.address}</p>
      </div>

      <h2>Rooms</h2>
    </>
  );
};

export default Hotel;
