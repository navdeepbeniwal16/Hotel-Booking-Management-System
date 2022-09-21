package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.user_types.Customer;

import java.util.ArrayList;
import java.util.Map;

public class CustomerMap implements IIdentityMap<Customer> {

    Map<Integer, Customer> customers;

    public CustomerMap(Map<Integer, Customer> customers) {
        this.customers = customers;
    }

    @Override
    public void add(AbstractDomainObject customers) throws IdentityMapException {
        if (customers.getClass()==Customer.class && customers.hasId()) {
            this.customers.put(customers.getId(), (Customer) customers);
        } else {
            throw new IdentityMapException("attempted to add object without an ID to identity map");
        }
    }

    @Override
    public Customer getById(Integer id) {
        return customers.get(id);
    }

    @Override
    public Customer remove(Integer id) {
        return customers.remove(id);
    }

    @Override
    public ArrayList<Customer> findAll() {
        return null;
    }

    @Override
    public void clear() {
        customers.clear();
    }
}
