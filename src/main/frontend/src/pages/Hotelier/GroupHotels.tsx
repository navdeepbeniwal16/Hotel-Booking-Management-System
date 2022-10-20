import React, { useState, useContext } from 'react';
import { ListGroup, Button, Toast } from 'react-bootstrap';
import { toString as addressToString } from '../../types/AddressType';
import Hotel from '../../types/HotelType';
import { map } from 'lodash';
import { HouseHeart } from 'react-bootstrap-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import CreateHotel from './CreateHotel';
import AppContext from '../../context/AppContext';
import HotelGroup from '../../types/HotelGroup';
export interface HotelListProps {
  hotels: Hotel[];
  group: HotelGroup;
}

const GroupHotels = ({ group, hotels }: HotelListProps) => {
  const { backend } = useContext(AppContext.GlobalContext);
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const [showModal, setShowModal] = useState(false);
  const [createMessage, setCreateMessage] = useState('');
  const handleSubmitCreateHotel = (hotel: Hotel) => {
    console.log('submitted');
    backend.createHotel(hotel).then(([success, error]: [boolean, string]) => {
      console.log('create hotel:', success);
      // TODO: @levim - improve alert
      if (success) {
        setCreateMessage('Success!');
      } else {
        setCreateMessage(`${error} Please refresh the page and try again.`);
      }
      setTimeout(() => {
        setShowModal(false);
        window.location.reload();
      }, 2000);
    });
  };
  const handleCloseModal = () => {
    console.log('modal closed');
    setShowModal(false);
  };
  return (
    <>
      {group.id == -1 ? (
        <Toast bg='danger' className="text-white" >
          <Toast.Header className="text-dark" closeButton={ false}>Error</Toast.Header>
          <Toast.Body>
            You are not currently assigned to a hotel group.
          </Toast.Body>
        </Toast>
      ) : (
        <>
          <div className='my-2'>
            <Button variant='success' onClick={() => setShowModal(true)}>
              <HouseHeart /> Create hotel
            </Button>
          </div>
          <CreateHotel
            message={createMessage}
            show={showModal}
            onSubmit={handleSubmitCreateHotel}
            onClose={handleCloseModal}
          />
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
                    <div>
                      Status:{' '}
                      <span
                        className={
                          hotel.is_active ? 'text-success' : 'text-danger'
                        }
                      >
                        {hotel.is_active ? 'Active' : 'Delisted'}
                      </span>
                    </div>
                    {addressToString(hotel.address)}
                  </div>
                  <Button
                    variant='primary'
                    onClick={() => navigate(`${pathname}/${hotel.id}`)}
                  >
                    Hotel Details
                  </Button>
                </ListGroup.Item>
              );
            })}
          </ListGroup>
        </>
      )}
    </>
  );
};

export default GroupHotels;
