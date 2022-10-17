import React, { useEffect, useState, useContext } from 'react';
import { Container, Row, Col, Form, Button} from 'react-bootstrap';
import AppContext from '../../context/AppContext';
import Hotel from '../../types/HotelType';

interface CreateRoomProps {
  hotel: Hotel;
}

/*
{
    "room": {
        "hotel_id": 2,
        "type": "New Type",
        "max_occupancy": 6,
        "bed_type": "New Bed Type",
        "room_price": 600,
        "number": 1001
    }
}
*/

const CreateRoom = ({ hotel }: CreateRoomProps) => {
  const price = (): React.ReactNode => {
    return (<Form.Group controlId="price">
      <Form.Label>Bed Type</Form.Label>
      <Form.Control type="number" placeholder="$99" />
      <Form.Text className="text-muted">
        Price per night
      </Form.Text>
    </Form.Group>)
  }

  const roomNumber = (): React.ReactNode => {
    return (<Form.Group controlId="roomNumber">
      <Form.Label>Bed Type</Form.Label>
      <Form.Control type="number" placeholder="101" />
      <Form.Text className="text-muted">
        Room number
      </Form.Text>
    </Form.Group>)
  }

  const bed = (): React.ReactNode => {
    return (<Form.Group controlId="bed">
      <Form.Label>Bed Type</Form.Label>
      <Form.Control type="text" placeholder="Queen bed" />
      <Form.Text className="text-muted">
        Type/size of bed
      </Form.Text>
    </Form.Group>)
  }
  const roomType = (): React.ReactNode => {
    return (<Form.Group controlId="roomType">
      <Form.Label>Room Type</Form.Label>
      <Form.Control type="text" placeholder="Superior room" />
      <Form.Text className="text-muted">
        Room type or classification
      </Form.Text>
    </Form.Group>)
  }

  const occupancy = (): React.ReactNode => {
    return (<Form.Group controlId="occupancy">
      <Form.Label>Occupancy</Form.Label>
      <Form.Control type="number" placeholder="1" />
      <Form.Text className="text-muted">
        Maximum number of guests
      </Form.Text>
    </Form.Group>)
  }

  const buttons = (): React.ReactNode => {
    return (<div className="d-flex justify-content-end">
      <Button type="submit">Submit</Button>
    </div>)
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log("submitted", event);
  }
  return (
    <Container fluid>
      <Row>
        <Col>
          <Form onSubmit={handleSubmit}>
            {roomNumber()}
            {roomType()}
            {occupancy()}
            {bed()}
            {price()}
            {buttons()}
          </Form>
        </Col>
      </Row>
    </Container>
  );
};

export default CreateRoom;
