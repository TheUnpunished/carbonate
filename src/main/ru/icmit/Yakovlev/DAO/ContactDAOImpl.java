package DAO;

import Model.Contact;
import Util.DbWork;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactDAOImpl implements ContactDAO {
    private DbWork db;
    public ContactDAOImpl(DbWork dbWork){
        this.db=dbWork;
    }
    @Override
    public Optional<Contact> find(Contact model) throws ClassNotFoundException {
        String  sql = "select * from contact where id = ?";
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setLong(1,model.getId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Long id = rs.getLong("id");
                String fullname = rs.getString("fullname");
                String lastname = rs.getString("lastname");
                String firstname = rs.getString("firstname");
                Boolean blacklisted = rs.getBoolean("inblacklist");
                Contact contact = new Contact(fullname,lastname,firstname,blacklisted);
                contact.setId(id);
                rs.close();
                return Optional.of(contact);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void save(Contact model) throws ClassNotFoundException {
        String sql = "insert into contact values\n" +
                "(?,?,?,?,?)";
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            if(model.getId()==null){
                model.setId(db.generateId("contact_seq"));
            }
            ps.setLong(1,model.getId());
            ps.setString(2,model.getFullName());
            ps.setString(3,model.getLastName());
            ps.setString(4,model.getFirstName());
            ps.setBoolean(5,model.getInBlackList());
            ps.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Contact model) throws ClassNotFoundException {
        String sql = "update contact set fullname = ?, lastname = ?," +
                "firstname = ?, inblacklist = ? where id = ?";
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setString(1,model.getFullName());
            ps.setString(2,model.getLastName());
            ps.setString(3,model.getFirstName());
            ps.setBoolean(4,model.getInBlackList());
            ps.setLong(5,model.getId());
            ps.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Contact model) throws ClassNotFoundException {
        String sql = "delete from contact where id = ?";
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
    public List<Contact> findAll() throws ClassNotFoundException {
        String sql = "select * from contact";
        try{
            db.setConnection();
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<Contact> result = new ArrayList<>();
            while(rs.next()){
                Long id = rs.getLong("id");
                String fullname = rs.getString("fullname");
                String lastname = rs.getString("lastname");
                String firstname = rs.getString("firstname");
                Boolean inblacklist = rs.getBoolean("inblacklist");
                Contact contact = new Contact(fullname,lastname,firstname,inblacklist);
                contact.setId(id);
                result.add(contact);
            }
            rs.close();
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
