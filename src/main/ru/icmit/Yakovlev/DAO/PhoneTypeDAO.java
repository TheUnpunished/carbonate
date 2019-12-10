package DAO;

import Model.PhoneType;

import java.util.List;
import java.util.Optional;

public interface PhoneTypeDAO extends GenericDAO<PhoneType> {
    public List<PhoneType> getByName(String name) throws ClassNotFoundException;
    public List<PhoneType> getByCode(String code) throws ClassNotFoundException;
    public Optional<PhoneType> getByNameAndCode(String name, String code) throws ClassNotFoundException;
}
