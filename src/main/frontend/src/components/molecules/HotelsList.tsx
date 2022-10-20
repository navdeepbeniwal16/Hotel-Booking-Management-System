import React from 'react';

import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';

import Hotel from '../../types/HotelType';
import { map } from 'lodash';
import { Container, Nav, Stack } from 'react-bootstrap';
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
      <Row xs={1} className='gy-3 gx-2'>
        <Col >
          <h2>All hotels</h2>
          <Stack gap={3} >
            {map(hotels, (hotel: Hotel) => {
              return (
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

              );
            })}
          </Stack>
        </Col>
      </Row>
    </Container>
  );
};
export default HotelsList;
