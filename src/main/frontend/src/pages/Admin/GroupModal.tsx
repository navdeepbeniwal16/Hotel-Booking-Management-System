import React, { useState, useContext, ReactNode, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import Fade from 'react-bootstrap/Fade';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Spinner from 'react-bootstrap/Spinner';
import HotelGroup, { defaultHotelGroup } from '../../types/HotelGroup';
import AppContext from '../../context/AppContext';
import Address, { AddressField } from '../../types/AddressType';

interface GroupModalProps {
  show: boolean;
  handleClose: () => void;
  onCreateGroup: (group: HotelGroup) => void;
}
function GroupModal({ show, handleClose, onCreateGroup }: GroupModalProps) {
  const { backend } = useContext(AppContext.GlobalContext);
  const [group, setGroup] = useState(defaultHotelGroup);
  const [waiting, setWaiting] = useState(false);
  type modalStates = 'FRESH' | 'SUCCESS' | 'FAIL';
  const FRESH: modalStates = 'FRESH';
  const SUCCESS: modalStates = 'SUCCESS';
  const FAIL: modalStates = 'FAIL';
  const [modalState, setModalState] = useState<modalStates>(FRESH);

  const closeIfNotWaiting = () => {
    if (!waiting) handleClose();
  };

  const reset = () => {
    setGroup(defaultHotelGroup);
    setWaiting(false);
    setModalState('FRESH');
  };

  useEffect(() => {
    if (show) reset();
  }, [show]);

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    setWaiting(true);
    backend.createGroup(group).then((success: boolean) => {
      setWaiting(false);
      if (success) {
        setModalState(SUCCESS);
        onCreateGroup(group);
      } else {
        setModalState(FAIL);
      }
      return setTimeout(() => {
        if (success) handleClose();
      }, 1500);
    });
  };

  useEffect(() => {
    console.log(group);
  }, [group]);
  const onNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setGroup({ ...group, name: event.target.value });
  };

  const renderNameField = (): ReactNode => {
    return (
      <Form.Group controlId='nameField'>
        <Form.Label>Name</Form.Label>
        <Form.Control
          required
          onChange={onNameChange}
          value={group.name}
          type='text'
          placeholder='Name of hotel group'
          aria-placeholder='Name of hotel group'
        />
        <Form.Text className='text-muted'>Enter name of hotel group</Form.Text>
      </Form.Group>
    );
  };

  const onPhoneChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const { value } = event.target;
    const phoneRegex = /^\+?\d{0,11}$/g;
    const phone = `${value}`;
    const isValid = phoneRegex.test(phone);
    if (isValid) {
      setGroup({ ...group, phone });
    }
  };

  const renderPhoneField = (): ReactNode => {
    return (
      <Form.Group controlId='phoneField'>
        <Form.Label>Phone</Form.Label>
        <Form.Control
          required
          onChange={onPhoneChange}
          value={group.phone}
          type='text'
          placeholder='+6103789456'
          aria-placeholder='+6103789456'
        />
        <Form.Text className='text-muted'>Enter contact phone number</Form.Text>
      </Form.Group>
    );
  };

  const onAddressFieldChange = (field: AddressField) => {
    return (event: React.ChangeEvent<HTMLInputElement>) =>
      onAddressChange(field, event);
  };

  const onAddressChange = (
    field: AddressField,
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { value } = event.target;
    const postcodeRegex = /^\d{0,4}$/g;
    const validPostcode = postcodeRegex.test(value);
    if (field == 'postcode' && !validPostcode) {
      return;
    }
    if (Object.keys(group.address).includes(field) && value) {
      const updatedAddress: Record<string, string | number> = {
        ...group.address,
      };
      updatedAddress[field] = value;
      setGroup({ ...group, address: updatedAddress as Address });
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
            value={group.address.line_1}
          />
          <Form.Text className='text-muted'>First address line</Form.Text>
        </Form.Group>
        <Form.Group controlId='line2Field'>
          <Form.Label>Line 2</Form.Label>
          <Form.Control
            onChange={onAddressFieldChange('line_2')}
            required
            type='text'
            placeholder='Unit 42'
            aria-placeholder='Unit 42'
            value={group.address.line_2}
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
            value={group.address.city}
          />
          <Form.Text className='text-muted'>City, town or suburb</Form.Text>
        </Form.Group>
        <Form.Group controlId='districtField'>
          <Form.Label>District</Form.Label>
          <Form.Control
            onChange={onAddressFieldChange('district')}
            required
            type='text'
            placeholder='VIC'
            aria-placeholder='VIC'
            value={`${group.address.district}`}
          />
          <Form.Text className='text-muted'>State or territory</Form.Text>
        </Form.Group>
        <Form.Group controlId='postcodeField'>
          <Form.Label>Postcode</Form.Label>
          <Form.Control
            onChange={onAddressFieldChange('postcode')}
            required
            type='text'
            placeholder='Melbourne'
            aria-placeholder='Melbourne'
            value={`${group.address.postcode}`}
          />
          <Form.Text className='text-muted'>Postcode</Form.Text>
        </Form.Group>
      </>
    );
  };

  const renderForm = (): React.ReactNode => (
    <>
      {!waiting ? (
        <Fade in={!waiting} className='p-3'>
          <>
            <Fade in={modalState == FRESH}>
              <>
                {renderNameField()}
                {renderPhoneField()}
                <Modal.Dialog>
                  <Modal.Header className='p-3'>Address</Modal.Header>
                  {renderAddressSection()}
                </Modal.Dialog>
              </>
            </Fade>
          </>
        </Fade>
      ) : (
        <Fade in={waiting}>
          <Alert variant='info'>Creating hotel group...</Alert>
        </Fade>
      )}
    </>
  );

  return (
    <>
      <Modal show={show} onHide={closeIfNotWaiting} centered>
        <Form onSubmit={handleSubmit}>
          <Modal.Header closeButton>
            <Modal.Title>Create a group</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {modalState === 'FRESH' ? (
              renderForm()
            ) : modalState === 'SUCCESS' ? (
              <Fade in={modalState == SUCCESS}>
                <Alert variant='success'>Successfully created new group</Alert>
              </Fade>
            ) : (
              <Fade in={modalState == FAIL}>
                <Alert variant='success'>
                  Failed to create hotel group: {group.name}
                </Alert>
              </Fade>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button
              disabled={waiting}
              variant='secondary'
              onClick={closeIfNotWaiting}
            >
              Close
            </Button>
            <Button disabled={waiting} type='submit' variant='primary'>
              {waiting ? <Spinner animation='border' /> : 'Create'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
}

export default GroupModal;
