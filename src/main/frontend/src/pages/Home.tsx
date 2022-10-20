import React, { useContext } from 'react';
import HotelsList from '../components/molecules/HotelsList';
import { useEffect, useState } from 'react';
import Hotel from '../types/HotelType';
import AppContext from '../context/AppContext';
import { forEach } from 'lodash';
import Map from '../components/map/Map';
import { Col, Container, Row } from 'react-bootstrap';


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
    };
    fetchHotels().catch(console.error);
  }, []);

  return (
    <Container>
      <Row xs={12}>
          <HotelsList hotels={hotels}></HotelsList>
      </Row>
    </Container>
  );
};

export default Home;
