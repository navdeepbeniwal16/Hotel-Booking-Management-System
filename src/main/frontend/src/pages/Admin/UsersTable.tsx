import React, { useContext, ReactNode, useState } from 'react';
import { map } from 'lodash';
import { Table, Button, Toast } from 'react-bootstrap';

import UserDataType from '../../types/UserDataType';
import AppContext from '../../context/AppContext';
import { Roles } from '../../types/RoleTypes';

interface UserTableProps {
  users: UserDataType[];
  makeHotelier: (id: number) => void;
}

const UsersTable = ({ users, makeHotelier }: UserTableProps) => {
  const { backend } = useContext(AppContext.GlobalContext);
  const defaultLoading: number[] = [];
  const [loading, setLoading] = useState<number[]>(defaultLoading);
  const [error, setError] = useState('');

  const renderButton = (user: UserDataType) => {
    return (
      <Button
        disabled={loading.includes(user.id)}
        onClick={
          loading.includes(user.id)
            ? () => {
                console.log('button disabled');
              }
            : () => {
                backend.makeHotelier(user).then((success: boolean) => {
                  if (success) {
                    makeHotelier(user.id);
                  } else {
                    setError('Something wen wrong. Please try again.');
                    setTimeout(() => {
                      window.location.reload();
                    }, 2000)
                  }
                  setLoading(loading.filter((id: number) => id != user.id));
                });
                setLoading([...loading, user.id]);
              }
        }
        variant='warning'
      >
        {loading.includes(user.id) ? 'Loading…' : 'Make hotelier'}
      </Button>
    );
  };

  const renderRole = (user: UserDataType): ReactNode => {
    return (
      <div>
        {`${user.role}`} {user.role == Roles.CUSTOMER && renderButton(user)}
      </div>
    );
  };
  return (
    <>
      {error != '' ? (
        <Toast bg='danger'>
          <Toast.Header>Error</Toast.Header>
          <Toast.Body>{error}</Toast.Body>
        </Toast>
      ) : (
        <Table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
            </tr>
          </thead>
          <tbody>
            {map(users, (user: UserDataType) => (
              <tr key={user.id}>
                <td>{`${user.id}`}</td>
                <td>{`${user.name}`}</td>
                <td>{`${user.email}`}</td>
                <td>{renderRole(user)}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </>
  );
};

export default UsersTable;
