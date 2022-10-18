import React, { useContext, useState } from 'react';
import { map } from 'lodash';

import Table from 'react-bootstrap/Table';

import Hotel from '../../types/HotelType';
import { toString } from '../../types/AddressType';
import { DashCircleFill } from 'react-bootstrap-icons';
import AppContext from '../../context/AppContext';
import { Spinner, Toast } from 'react-bootstrap';
interface IHotelsTableProps {
  hotels: Hotel[];
  onRemoveHotel: (hotel: Hotel) => void;
}

const Hotels = ({ hotels, onRemoveHotel }: IHotelsTableProps) => {
  const { backend } = useContext(AppContext.GlobalContext);
  const loadingStates: Record<number, boolean> = {};
  map(hotels, (hotel: Hotel) => {
    loadingStates[hotel.id] = false;
  });
  const [error, setError] = useState('');
  const [waiting, setWaiting] = useState(loadingStates);
  const renderIcon = (hotel: Hotel): React.ReactNode => {
    return (
      <div className='d-flex justify-content-center'>
        {waiting[hotel.id] ? (
          <Spinner animation='border' variant='secondary' size='sm' />
        ) : (
          hotel.is_active && (
            <DashCircleFill
              className='text-danger ms-2'
              style={{ cursor: 'pointer' }}
              onClick={() => {
                setWaiting({ ...waiting, [hotel.id]: true });
                backend
                  .removeHotel(hotel)
                  .then((success: boolean) => {
                    setWaiting({ ...waiting, [hotel.id]: false });
                    if (success != undefined && success) {
                      onRemoveHotel({ ...hotel, is_active: false });
                      console.log('removed hotel', hotel.name);
                      setError('');
                    } else {
                      console.log('failed to remove hotel', hotel.name);
                      console.log('success=', success);
                      setError('Failed to remove hotel. Please try again');
                      setTimeout(() => {
                        window.location.reload();
                      }, 3000);
                    }
                  })
                  .catch(console.log);
              }}
            />
          )
        )}
      </div>
    );
  };
  return (
    <>
      {error != '' ? (
        <Toast show={error != ''} bg='danger'>
          <Toast.Header>Something went wrong</Toast.Header>
          <Toast.Body>{error}</Toast.Body>
        </Toast>
      ) : (
        <Table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Hotel Group</th>
              <th>Phone</th>
              <th>Address</th>
              <th>Status</th>
              <th>Delist</th>
            </tr>
          </thead>
          <tbody>
            {map(hotels, (hotel: Hotel) => (
              <tr
                key={hotel.id}
                className={`${!hotel.is_active && 'bg-light text-muted'}`}
              >
                <td>{`${hotel.id}`}</td>
                <td>{`${hotel.name}`}</td>
                <td>{`${hotel.hotel_group_id}`}</td>
                <td>{`${hotel.phone}`}</td>
                <td>{`${hotel.address ? toString(hotel.address) : '-'}`}</td>
                <td className={`${!hotel.is_active && 'text-info'}`}>{`${
                  hotel.is_active ? 'Listed' : 'Unlisted'
                }`}</td>
                <td>{renderIcon(hotel)}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </>
  );
};

export default Hotels;
