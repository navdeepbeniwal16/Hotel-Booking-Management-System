package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.AbstractDomainObject;

import java.sql.Connection;

public interface IUnitOfWork { // TODO: remove generic
    <DomainObject extends AbstractDomainObject> void registerNew(AbstractDomainObject obj) throws UoWException;
    <DomainObject extends AbstractDomainObject> void registerDirty(DomainObject obj) throws UoWException;
    <DomainObject extends AbstractDomainObject> void registerRemoved(DomainObject obj) throws UoWException;
    <DomainObject extends AbstractDomainObject> void registerClean(DomainObject obj) throws UoWException;
    void commit(Connection connection, IMapperRegistry mappers) throws Exception ;
}
