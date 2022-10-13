import React, { ReactNode, useContext, useState } from 'react';
import { map } from 'lodash';
import AppContext from '../../context/AppContext';
import Table from 'react-bootstrap/Table';

import { PersonPlusFill, PersonDashFill } from 'react-bootstrap-icons';
import Hotelier, { defaultHotelier } from '../../types/HotelierType';
import HotelierModal from './HotelierModal';
import HotelGroup from '../../types/HotelGroup';

interface IHoteliersProps {
  hoteliers: Hotelier[];
  hotel_groups: HotelGroup[];
  removeHotelier: (hotelier: Hotelier) => void;
  addHotelierToGroup: (hotelier: Hotelier, group: HotelGroup) => void;
}

const Hoteliers = ({
  hoteliers,
  hotel_groups,
  removeHotelier,
  addHotelierToGroup,
}: IHoteliersProps) => {
  const [showModal, setShowModal] = useState(false);
  const [editHotelier, setEditHotelier] = useState(defaultHotelier);
  const handleClose = () => setShowModal(false);
  const handleShow = (hotelier: Hotelier) => {
    setEditHotelier(hotelier);
    setShowModal(true);
  };

  const { backend } = useContext(AppContext.GlobalContext);

  const handleRemoveHotelier = (hotelier: Hotelier) => {
    backend
      .removeHotelierFromHotelGroup(hotelier.id)
      .then((success: boolean) => success && removeHotelier(hotelier));
  };

  const xIcon = (hotelier: Hotelier): ReactNode => {
    return (
      <PersonDashFill
        className='text-danger ms-2'
        style={{ cursor: 'pointer' }}
        onClick={() => handleRemoveHotelier(hotelier)}
      />
    );
  };

  const plusIcon = (hotelier: Hotelier): ReactNode => {
    return (
      <PersonPlusFill
        className='text-primary ms-2'
        style={{ cursor: 'pointer' }}
        onClick={() => handleShow(hotelier)}
      />
    );
  };

  const renderGroup = (hotelier: Hotelier): ReactNode => {
    const hasHotelGroup = !(
      hotelier.hotel_group.name == undefined || hotelier.hotel_group.name == ''
    );
    return (
      <div>
        {hasHotelGroup ? (
          <div>
            {hotelier.hotel_group.name}
            {xIcon(hotelier)}
          </div>
        ) : (
          plusIcon(hotelier)
        )}
      </div>
    );
  };
  return (
    <>
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
              <td>{renderGroup(hotelier)}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <HotelierModal
        show={showModal}
        handleClose={handleClose}
        hotelier={editHotelier}
        groups={hotel_groups}
        addHotelier={addHotelierToGroup}
      />
    </>
  );
};

export default Hoteliers;
