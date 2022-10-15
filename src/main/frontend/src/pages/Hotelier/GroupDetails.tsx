import React from 'react';
import HotelGroup from '../../types/HotelGroup';
import { toString as addressToString } from '../../types/AddressType';


export interface GroupDetailsProps {
  group: HotelGroup
}

const GroupDetails = ({ group }: GroupDetailsProps) => {
  return (<div>
    <h3>Group details</h3>
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
  </div>)
}

export default GroupDetails;