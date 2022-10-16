import React, { useState } from 'react';
import { ListGroup, Button } from 'react-bootstrap';
import { toString as addressToString } from '../../types/AddressType';
import Hotel from '../../types/HotelType';
import { map } from 'lodash';
import { PlusCircle } from 'react-bootstrap-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import CreateHotel from './CreateHotel';

export interface HotelListProps {
  hotels: Hotel[];
}



const GroupHotels = ({ hotels }: HotelListProps) => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const [showModal, setShowModal] = useState(false);
  const handleSubmitCreateHotel = () => {
    console.log("submitted");
    setShowModal(false);
  }
  const handleCloseModal = () => {
    console.log("modal closed");
    setShowModal(false);
  }
  return (
    <>
      <div className="my-2">
        <h3>Hotels</h3>
        <Button variant='info' onClick={()=>setShowModal(true)}>
          Create hotel <PlusCircle />
        </Button>
      </div>
      <CreateHotel
        show={showModal}
        onSubmit={handleSubmitCreateHotel}
        onClose={handleCloseModal} />
      <ListGroup as='ul'>
        {map(hotels, (hotel: Hotel, index: number) => {
          return (
            <ListGroup.Item
              as='li'
              key={`${index}`}
              className='d-flex justify-content-between align-items-start'
            >
              <div className='ms-2 me-auto'>
                <div className='fw-bold'>{hotel.name}</div>
                {addressToString(hotel.address)}
              </div>
              <Button
                variant='primary'
                onClick={() => navigate(`${pathname}/${hotel.id}`)}
              >
                {' '}
                View bookings{' '}
              </Button>
            </ListGroup.Item>
          );
        })}
      </ListGroup>
    </>
  );
};

export default GroupHotels;
