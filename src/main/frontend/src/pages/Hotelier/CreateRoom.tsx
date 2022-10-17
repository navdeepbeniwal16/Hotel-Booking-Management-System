import React, { useEffect, useState, useContext, ChangeEvent } from 'react';
import { Container, Row, Col, Form, Button, Toast } from 'react-bootstrap';
import AppContext from '../../context/AppContext';
import Hotel from '../../types/HotelType';
import Room, { defaultRoom } from '../../types/RoomType';

interface CreateRoomProps {
  hotel: Hotel;
}

const CreateRoom = ({ hotel }: CreateRoomProps) => {
  const { backend } = useContext(AppContext.GlobalContext);
  const [room, setRoom] = useState<Room>({
    ...defaultRoom,
    hotel_id: hotel.id,
  });

  enum FormState {
    WIP = 0,
    SUCCESS = 1,
    FAILURE = 2,
  }

  const [formState, setFormState] = useState<FormState>(FormState.WIP);
  const [responseMessage, setResponseMessage] = useState("")
  useEffect(() => {
    setRoom({ ...defaultRoom, hotel_id: hotel.id });
    if (formState == FormState.WIP) setResponseMessage("");
  }, [formState]);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log('submitted', event);
    if (room) {
      backend.createRoom(room).then(([success, message]: [boolean, string]) => {
        console.log('room:', room);
        console.log('room created?', success);
        console.log('room created?', message);
        setResponseMessage(message);
        const updateFormState: FormState = success
          ? FormState.SUCCESS
          : FormState.FAILURE;
        setFormState(updateFormState);
        setTimeout(() => {
          if (success) {
            window.location.reload();
          } else {
            setFormState(FormState.WIP);
          }
        }, 5000);
      });
    }
  };

  const handleField = (field: string) => {
    return (event: ChangeEvent<HTMLInputElement>) => {
      setRoom({ ...room, [field]: event.target.value });
    };
  };

  const price = (): React.ReactNode => {
    return (
      <Form.Group controlId='price'>
        <Form.Label>Price</Form.Label>
        <Form.Control
          required
          onChange={handleField('price')}
          value={room.price}
          type='number'
          placeholder='$99'
        />
        <Form.Text className='text-muted'>Price per night</Form.Text>
      </Form.Group>
    );
  };

  const roomNumber = (): React.ReactNode => {
    return (
      <Form.Group controlId='roomNumber'>
        <Form.Label>Room number</Form.Label>
        <Form.Control
          required
          onChange={handleField('number')}
          value={room.number}
          type='number'
          placeholder='101'
        />
        <Form.Text className='text-muted'>Room number</Form.Text>
      </Form.Group>
    );
  };

  const bed = (): React.ReactNode => {
    return (
      <Form.Group controlId='bed'>
        <Form.Label>Bed Type</Form.Label>
        <Form.Control
          required
          onChange={handleField('bed_type')}
          value={room.bed_type}
          type='text'
          placeholder='Queen bed'
        />
        <Form.Text className='text-muted'>Type/size of bed</Form.Text>
      </Form.Group>
    );
  };
  const roomType = (): React.ReactNode => {
    return (
      <Form.Group controlId='roomType'>
        <Form.Label>Room Type</Form.Label>
        <Form.Control
          required
          onChange={handleField('type')}
          value={room.type}
          type='text'
          placeholder='Superior room'
        />
        <Form.Text className='text-muted'>
          Room type or classification
        </Form.Text>
      </Form.Group>
    );
  };

  const occupancy = (): React.ReactNode => {
    return (
      <Form.Group controlId='occupancy'>
        <Form.Label>Occupancy</Form.Label>
        <Form.Control
          required
          onChange={handleField('occupancy')}
          value={room.occupancy}
          type='number'
          placeholder='1'
        />
        <Form.Text className='text-muted'>Maximum number of guests</Form.Text>
      </Form.Group>
    );
  };

  const buttons = (): React.ReactNode => {
    return (
      <div className='d-flex justify-content-end'>
        <Button type='submit'>Submit</Button>
      </div>
    );
  };

  const renderForm = (): React.ReactNode => {
    return (
      <Form onSubmit={handleSubmit}>
        {roomNumber()}
        {roomType()}
        {occupancy()}
        {bed()}
        {price()}
        {buttons()}
      </Form>
    );
  };

  const renderSuccess = (success = true): React.ReactNode => {
    return (
      <Toast animation={true} bg={success ? 'success' : 'danger'}>
        <Toast.Header>
          <strong className='me-auto'>{success ? 'Success' : 'Failed'}</strong>
          <small>{success ? 'Yay!' : 'Oops!'}</small>
        </Toast.Header>
        <Toast.Body>
          {success
            ? 'New room created. Redirecting you to hotel room now.'
            : `Sorry, something went wrong: \n\t${responseMessage}\n\nPlease try again.`}
        </Toast.Body>
      </Toast>
    );
  };

  return (
    <Container fluid>
      <Row>
        <Col>
          {formState == FormState.WIP ? (
            renderForm()
          ) : formState == FormState.SUCCESS ? (
            renderSuccess()
          ) : formState == FormState.FAILURE ? (
                renderSuccess(false)
          ) : (
            <div>Error</div>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default CreateRoom;
