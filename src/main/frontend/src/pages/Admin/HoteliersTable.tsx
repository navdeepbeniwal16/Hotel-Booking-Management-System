import React, { useContext } from 'react';
import { map } from 'lodash';
import AppContext from '../../context/AppContext';
import Table from 'react-bootstrap/Table';

import { XCircle } from 'react-bootstrap-icons';
import Hotelier from '../../types/HotelierType';

interface IHoteliersProps {
  hoteliers: Hotelier[];
  removeHotelier: (hotelier_id: number) => void;
}

const Hoteliers = ({ hoteliers, removeHotelier }: IHoteliersProps) => {
  const { backend } = useContext(AppContext.GlobalContext);
  const handleRemoveHotelier = async (hotelier_id: number) => {
    const removedSuccessfully = await backend.removeHotelierFromHotelGroup(
      hotelier_id
    );
    if (removedSuccessfully) {
      console.log(
        'removed successfully - calling removeHotelier: ',
        hotelier_id
      );
      removeHotelier(hotelier_id);
    } else {
      console.log('removed failed: ', hotelier_id);
    }
  };
  return (
    <Table>
      <thead>
        <tr>
          <th>User ID</th>
          <th>Email</th>
          <th>Name</th>
          <th>Hotel Group</th>
        </tr>
      </thead>
      <tbody>
        {map(hoteliers, (hotelier: Hotelier) => (
          <tr key={hotelier.id}>
            <td>{`${hotelier.id}`}</td>
            <td>{`${hotelier.email}`}</td>
            <td>{`${hotelier.name}`}</td>
            <td>
              <div>
                <div>
                  {hotelier.hotel_group.name == undefined ||
                  hotelier.hotel_group.name == ''
                    ? '-'
                    : hotelier.hotel_group.name}
                </div>
                <XCircle
                  style={{
                    color: '#8b0000',
                    marginLeft: '5px',
                    cursor: 'pointer',
                  }}
                  onClick={() => handleRemoveHotelier(hotelier.id)}
                />
              </div>
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default Hoteliers;
