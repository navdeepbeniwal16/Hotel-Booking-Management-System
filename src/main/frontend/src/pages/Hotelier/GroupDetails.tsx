import React from 'react';
import HotelGroup from '../../types/HotelGroup';
import { toString as addressToString } from '../../types/AddressType';
import { Card } from 'react-bootstrap';

export interface GroupDetailsProps {
  group: HotelGroup;
}

const GroupDetails = ({ group }: GroupDetailsProps) => {
  return (
    <Card>
      <Card.Header>
        <Card.Title>
          <h3>Group details</h3>
        </Card.Title>
      </Card.Header>
      <Card.Body>
        <div>
          <span className='fw-bold'>Name: </span>
          {group.name}
        </div>
        <div>
          <span className='fw-bold'>Phone: </span>
          {group.phone}
        </div>
        <div>
          <span className='fw-bold'>Address: </span>
          {`${addressToString(group.address)}`}
        </div>
      </Card.Body>
    </Card>
  );
};

export default GroupDetails;
