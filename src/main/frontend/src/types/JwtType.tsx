import RoleT from './RoleTypes';

const namespace = 'lans_hotels';

export const customClaims = {
  email: `${namespace}/email`,
  roles: `${namespace}/roles`,
};

type JwtT = {
  iss: string;
  'lans_hotels/email'?: string;
  'lans_hotels/roles'?: RoleT[];
};

export default JwtT;
