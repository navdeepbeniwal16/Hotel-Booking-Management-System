import React, { useState } from 'react';
import { map } from 'lodash';

import Table from 'react-bootstrap/Table';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import HotelGroup from '../../types/HotelGroup';
import { toString } from '../../types/AddressType';
import GroupModal from './GroupModal';
import { Button } from 'react-bootstrap';
import { PlusCircleFill } from 'react-bootstrap-icons';

interface IHotelGroupProps {
  hotel_groups: HotelGroup[];
  refresh: () => void;
}

const Groups = ({ hotel_groups, refresh }: IHotelGroupProps) => {
  const [showModal, setShowModal] = useState(false);
  const handleClose = () => setShowModal(false);
  return (
    <Row>
      <GroupModal
        show={showModal}
        handleClose={handleClose}
        onCreateGroup={refresh}
      />
      <Row>
        <Col>
          <Row>
            <div>
              <Button
                variant='primary'
                size='sm'
                onClick={() => setShowModal(true)}
              >
                Create a new hotel group
                <PlusCircleFill className='ms-2' />
              </Button>
            </div>
          </Row>
        </Col>
      </Row>
      <Row>
        <Table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Phone</th>
              <th>Address</th>
            </tr>
          </thead>
          <tbody>
            {map(hotel_groups, (hotel_group: HotelGroup) => (
              <tr key={hotel_group.id}>
                <td>{`${hotel_group.id}`}</td>
                <td>{`${hotel_group.name}`}</td>
                <td>{`${hotel_group.phone}`}</td>
                <td>{`${
                  hotel_group.address ? toString(hotel_group.address) : '-'
                }`}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Row>
    </Row>
  );
};

export default Groups;
