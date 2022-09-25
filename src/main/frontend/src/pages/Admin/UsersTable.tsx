import React from 'react';
import { map } from 'lodash';
import { Table } from 'react-bootstrap';

import UserDataType from '../../types/UserDataType';

interface UserTableProps {
  users: UserDataType[];
}

const UsersTable = ({ users }: UserTableProps) => {
  const roleIdToString = (id: number) => {
    if (id === 1) return 'Admin';
    if (id === 2) return 'Hotelier';
    if (id === 3) return 'Customer';
    return 'Unknown';
  };
  return (
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
            <td>{`${roleIdToString(user.role)}`}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default UsersTable;
