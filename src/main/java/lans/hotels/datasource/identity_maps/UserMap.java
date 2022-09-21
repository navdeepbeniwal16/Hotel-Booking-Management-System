package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.user_types.User;

import java.util.ArrayList;
import java.util.Map;

public class UserMap implements IIdentityMap<User> {

    Map<Integer, User> users;

    public UserMap(Map<Integer, User> users) {
        this.users = users;
    }

    @Override
    public void add(AbstractDomainObject users) throws IdentityMapException {
        if (users.getClass()==User.class && users.hasId()) {
            this.users.put(users.getId(), (User) users);
        } else {
            throw new IdentityMapException("attempted to add object without an ID to identity map");
        }
    }

    @Override
    public User getById(Integer id) {
        return users.get(id);
    }

    @Override
    public User remove(Integer id) {
        return users.remove(id);
    }

    @Override
    public ArrayList<User> findAll() {
        return null;
    }

    @Override
    public void clear() {
        users.clear();
    }
}
