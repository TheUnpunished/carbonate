package ru.icmit.Yakovlev.DAO;

import ru.icmit.Yakovlev.Model.Contact;
import ru.icmit.Yakovlev.Model.Phone;
import ru.icmit.Yakovlev.Model.PhoneType;
import ru.icmit.Yakovlev.Util.DbWork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhoneDAOImpl implements PhoneDAO {
    private DbWork db;
    public PhoneDAOImpl(DbWork dbWork){
        this.db=dbWork;
    }
    @Override
    public Optional<Phone> find(Phone model) throws ClassNotFoundException {
        String sql = "select contact_id, phone.id as phone_id, phonetype_id, phonenumber, \n" +
                "dict_phonetype.code as phonetype_code, dict_phonetype.name as phonetype_name,\n" +
                "dict_phonetype.fullname as phonetype_fullname,contact.fullname as contact_fullname,\n" +
                "contact.lastname as contact_lastname, contact.firstname as contact_firstname,\n" +
                "contact.inblacklist as contact_inblacklist from phone inner join dict_phonetype\n" +
                "on phonetype_id=dict_phonetype.id inner join contact on contact_id = contact.id\n" +
                "where phone.id=?";
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setLong(1,model.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Long id = rs.getLong("phone_id");
                String phoneNumber = rs.getString("phonenumber");
                Long contactId = rs.getLong("contact_id");
                String contactFullName = rs.getString("contact_fullname");
                String contactLastName = rs.getString("contact_lastname");
                String contactFirstName = rs.getString("contact_firstname");
                Boolean contactInBlackList = rs.getBoolean("contact_inblacklist");
                Long phoneTypeId = rs.getLong("phonetype_id");
                String phoneTypeCode = rs.getString("phonetype_code");
                String phoneTypeName = rs.getString("phonetype_name");
                String phoneTypeFullName = rs.getString("phonetype_fullname");
                Contact contact = new Contact(contactFullName,contactLastName,contactFirstName,contactInBlackList);
                PhoneType phoneType = new PhoneType();
                phoneType.setId(phoneTypeId);
                phoneType.setName(phoneTypeName);
                phoneType.setFullName(phoneTypeFullName);
                phoneType.setCode(phoneTypeCode);
                contact.setId(contactId);
                Phone phone = new Phone(contact,phoneType,phoneNumber);
                phone.setId(id);
                rs.close();
                return Optional.of(phone);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    return Optional.empty();
    }

    @Override
    public void save(Phone model) throws ClassNotFoundException {
        String sql = "insert into phone values " +
                "(?,?,?,?)";
        if(model.getId()==null){
            model.setId(db.generateId("phone_seq"));
        }
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setLong(1,model.getContact().getId());
            ps.setLong(2,model.getId());
            ps.setLong(3,model.getPhoneType().getId());
            ps.setString(4, model.getPhoneNumber());
            ps.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Phone model) throws ClassNotFoundException {
        String sql = "update phone set contact_id = ?, phonetype_id = ?, phonenumber = ?" +
                "where id = ? ";
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setLong(1,model.getContact().getId());
            ps.setLong(2, model.getPhoneType().getId());
            ps.setString(3,model.getPhoneNumber());
            ps.setLong(4,model.getId());
            ps.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Phone model) throws ClassNotFoundException {
        String sql = "delete from phone where id = ?";
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setLong(1,model.getId());
            ps.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public List<Phone> findAll() {
        List<Phone> phones = new ArrayList<>();
        String sql = "select contact_id, phone.id as phone_id, phonetype_id, phonenumber, \n" +
                "dict_phonetype.code as phonetype_code, dict_phonetype.name as phonetype_name,\n" +
                "dict_phonetype.fullname as phonetype_fullname,contact.fullname as contact_fullname,\n" +
                "contact.lastname as contact_lastname, contact.firstname as contact_firstname,\n" +
                "contact.inblacklist as contact_inblacklist from phone inner join dict_phonetype\n" +
                "on phonetype_id=dict_phonetype.id inner join contact on contact_id = contact.id\n";
        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                Long id = rs.getLong("phone_id");
                String phoneNumber = rs.getString("phonenumber");
                Long contactId = rs.getLong("contact_id");
                String contactFullName = rs.getString("contact_fullname");
                String contactLastName = rs.getString("contact_lastname");
                String contactFirstName = rs.getString("contact_firstname");
                Boolean contactInBlackList = rs.getBoolean("contact_inblacklist");
                Long phoneTypeId = rs.getLong("phonetype_id");
                String phoneTypeCode = rs.getString("phonetype_code");
                String phoneTypeName = rs.getString("phonetype_name");
                String phoneTypeFullName = rs.getString("phonetype_fullname");
                Contact contact = new Contact(contactFullName,contactLastName,contactFirstName,contactInBlackList);
                PhoneType phoneType = new PhoneType();
                phoneType.setId(phoneTypeId);
                phoneType.setName(phoneTypeName);
                phoneType.setFullName(phoneTypeFullName);
                phoneType.setCode(phoneTypeCode);
                contact.setId(contactId);
                Phone phone = new Phone(contact,phoneType,phoneNumber);
                phone.setId(id);
                phones.add(phone);
            }
            rs.close();
            return phones;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
