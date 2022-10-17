import React from 'react';

import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';

import Hotel from '../../types/HotelType';
import { map } from 'lodash';
import { Container, Nav } from 'react-bootstrap';
import AppContext from '../../context/AppContext';
import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { toString } from '../../types/AddressType';
interface HotelListProps {
  hotels: Array<Hotel>;
}

const HotelsList = ({ hotels }: HotelListProps) => {
  return (
    <Container>
      <Row  xs={1} md={2} className='gy-3 gx-2'>
      {map(hotels, (hotel: Hotel) => {
        return (
          <Col key={ hotel.id}>
            <Nav.Link key={`${hotel.id}`}
              as={Link}
              to={`/hotel/${hotel.id}`}
            >
              
                <Card>
                  <Card.Body>
                    <Card.Title>{hotel.name}</Card.Title>
                    <Card.Subtitle className='mb-2 text-muted'>
                      {toString(hotel.address)}
                    </Card.Subtitle>
                  </Card.Body>
                </Card>
              
            </Nav.Link>
            </Col>
        );
      })}
      </Row>
    </Container>
  );
};
export default HotelsList;
