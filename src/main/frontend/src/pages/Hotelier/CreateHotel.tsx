import React, { ReactNode, useState, ChangeEvent } from 'react';
import { Modal, Button, Form, Toast } from 'react-bootstrap';
import Address, { AddressField } from '../../types/AddressType';
import Hotel, { emptyHotel, schemas } from '../../types/HotelType';
interface CreateHotelProps {
  show: boolean;
  message: string;
  onSubmit: (hotel: Hotel) => void;
  onClose: () => void;
}

const CreateHotel = ({ show, onSubmit, onClose, message }: CreateHotelProps) => {
  const [hotel, setHotel] = useState(emptyHotel);
  const [validated, setValidated] = useState(false);
  const [districtSelect, setDistrictSelect] = useState('VIC');
  const [error, setError] = useState('');

  const handleOnEnter = () => {
    console.log('onEnter');
    setValidated(false);
    setHotel(emptyHotel);
  };

  const handleOnExit = () => {
    console.log('onExit');
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    event.currentTarget.checkValidity();
    schemas.create.schema.isValid({ ...hotel }).then((valid: boolean) => {
      setValidated(true);
      if (valid) {
        onSubmit(hotel);
      } else {
        event.stopPropagation();
      }
    });
  };

  const onNameChange = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setHotel({ ...hotel, name: event.target.value });
  };

  const validateField = (field: string) => {
    try {
      return validated && schemas.create.schema.validateSyncAt(field, hotel) ? (
        <Form.Control.Feedback tooltip type='invalid'>
          Please enter a valid name
        </Form.Control.Feedback>
      ) : (
        <Form.Control.Feedback tooltip type='valid'>
          Looks good!
        </Form.Control.Feedback>
      );
    } catch (error) {
      return null;
    }
  };

  const renderName = (): ReactNode => {
    return (
      <Form.Group controlId='nameField'>
        <Form.Label>Name</Form.Label>
        <Form.Control
          required
          onChange={onNameChange}
          value={hotel.name}
          type='text'
          placeholder='Name of hotel'
          aria-placeholder='Name of hotel'
        />
        <Form.Text className='text-muted'>Enter name of hotel</Form.Text>
        {validateField('name')}
      </Form.Group>
    );
  };

  const onPhoneChange = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const { value } = event.target;
    const phoneRegex = /^\+?\d{0,11}$/g;
    const phone = `${value}`;
    const isValid = phoneRegex.test(phone);
    if (isValid) {
      console.log('valid');
      setHotel({ ...hotel, phone });
    } else {
      console.log('invalid');
    }
  };

  const renderPhone = (): ReactNode => {
    return (
      <Form.Group controlId='phoneField'>
        <Form.Label>Phone</Form.Label>
        <Form.Control
          required
          onChange={onPhoneChange}
          value={hotel.phone}
          type='text'
          placeholder='+6103789456'
          aria-placeholder='+6103789456'
        />
        <Form.Text className='text-muted'>Enter contact phone number</Form.Text>
        {validateField('phone')}
      </Form.Group>
    );
  };

  const onEmailChange = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const { value } = event.target;
    const email = `${value}`;
    setHotel({ ...hotel, email: email.toLowerCase() });
  };

  const renderEmail = (): ReactNode => {
    return (
      <Form.Group controlId='emailField'>
        <Form.Label>Email</Form.Label>
        <Form.Control
          required
          onChange={onEmailChange}
          value={hotel.email}
          type='text'
          placeholder='admin@hotel.com'
          aria-placeholder='admin@hotel.com'
        />
        <Form.Text className='text-muted'>
          Enter email address for hotel
        </Form.Text>
        {validateField('email')}
      </Form.Group>
    );
  };

  const onAddressFieldChange = (field: AddressField) => {
    return (event: React.ChangeEvent<HTMLInputElement>) =>
      onAddressChange(field, event.target.value);
  };

  const onAddressChange = (field: AddressField, value: string) => {
    console.log(value);

    if (Object.keys(hotel.address).includes(field)) {
      const updatedAddress: Record<string, string | number> = {
        ...hotel.address,
      };
      const postcodeRegex = /^\d{0,4}$/g;
      const validPostcode = postcodeRegex.test(`${value}`);
      if (field === 'postcode' && validPostcode) {
        updatedAddress[field] = value;
      } else {
        updatedAddress[field] = value;
      }
      setHotel({ ...hotel, address: updatedAddress as Address });
    }
  };

  const renderAddressSection = (): ReactNode => {
    return (
      <>
        <Form.Group controlId='line1Field'>
          <Form.Label>Line 1</Form.Label>
          <Form.Control
            onChange={onAddressFieldChange('line_1')}
            required
            type='text'
            placeholder='100 Example Street'
            aria-placeholder='100 Example Street'
            value={hotel.address.line_1}
          />
          <Form.Text className='text-muted'>First address line</Form.Text>
          {validateField('address.line_1')}
        </Form.Group>
        <Form.Group controlId='line2Field'>
          <Form.Label>Line 2</Form.Label>
          <Form.Control
            onChange={onAddressFieldChange('line_2')}
            required
            type='text'
            placeholder='Unit 42'
            aria-placeholder='Unit 42'
            value={hotel.address.line_2}
          />
          <Form.Text className='text-muted'>Second address line</Form.Text>
        </Form.Group>
        <Form.Group controlId='cityField'>
          <Form.Label>City</Form.Label>
          <Form.Control
            onChange={onAddressFieldChange('city')}
            required
            type='text'
            placeholder='Melbourne'
            aria-placeholder='Melbourne'
            value={hotel.address.city}
          />
          <Form.Text className='text-muted'>City, town or suburb</Form.Text>
          {validateField('address.city')}
        </Form.Group>
        <Form.Group controlId='districtField'>
          <Form.Label>District</Form.Label>
          <Form.Control
            hidden
            // onChange={onAddressFieldChange('district')}
            required
            type='text'
            placeholder='VIC'
            aria-placeholder='VIC'
            value={`${districtSelect}`}
          />
          <Form.Select
            onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
              setDistrictSelect(event.target.value);
              onAddressChange('district', event.target.value);
            }}
            aria-label='Default select example'
          >
            <option>District</option>
            <option value='VIC'>VIC</option>
            <option value='NSW'>NSW</option>
            <option value='NT'>NT</option>
            <option value='QLD'>QLD</option>
            <option value='SA'>SA</option>
            <option value='ACT'>ACT</option>
            <option value='WA'>WA</option>
            <option value='TAS'>TAS</option>
          </Form.Select>
          <Form.Text className='text-muted'>
            District, state or territory
          </Form.Text>
          {validateField('address.district')}
        </Form.Group>
        <Form.Group controlId='postcodeField'>
          <Form.Label>Postcode</Form.Label>
          <Form.Control
            onChange={onAddressFieldChange('postcode')}
            required
            type='text'
            placeholder='3000'
            aria-placeholder='3000'
            value={`${hotel.address.postcode}`}
          />
          <Form.Text className='text-muted'>Postcode</Form.Text>
          {validateField('address.postcode')}
        </Form.Group>
      </>
    );
  };

  return (
    <Modal
      show={show}
      onHide={onClose}
      backdrop='static'
      scrollable={true}
      onEnter={handleOnEnter}
      onExit={handleOnExit}
    >
      <Modal.Header closeButton>
        <Modal.Title>Create a new hotel</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error != '' || message != '' ? (
          <Toast bg={message == "Success!" ? "success" : "danger"}>
            <Toast.Body>{error ? error : message ? message : ""}</Toast.Body>
          </Toast>
        ) : (
          <Form onSubmit={handleSubmit} noValidate validated={validated}>
            {renderName()}
            {renderEmail()}
            {renderPhone()}
            {renderAddressSection()}
            <Modal.Footer>
              <Button variant='secondary' onClick={onClose}>
                Close
              </Button>
              <Button type='submit' variant='primary'>
                Create
              </Button>
            </Modal.Footer>
          </Form>
        )}
      </Modal.Body>
    </Modal>
  );
};

export default CreateHotel;
