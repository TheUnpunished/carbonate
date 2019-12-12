package ru.icmit.Yakovlev.DAO;

import ru.icmit.Yakovlev.Model.PhoneType;
import ru.icmit.Yakovlev.Util.DbWork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhoneTypeDAOImpl implements PhoneTypeDAO {
    private DbWork db;
    public PhoneTypeDAOImpl(DbWork dbWork){
        this.db=dbWork;
    }
    @Override
    public void save(PhoneType o) throws ClassNotFoundException {
        String sql =
                "insert into dict_phonetype (id, name, fullname, code) values ( ? , ? , ? , ? ) ";
        if(o.getId()==null){
            o.setId(db.generateId("phonetype_seq"));
        }
        try{
            db.setConnection();
            PreparedStatement st = db.getConnection().prepareStatement(sql);
            st.setLong(1, o.getId());
            st.setString(2, o.getName());
            st.setString( 3, o.getFullName());
            st.setString( 4, o.getCode());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            o = null;
        }
    }

    @Override
    public void update(PhoneType o) throws ClassNotFoundException {
        String sql =
                "update dict_phonetype set name = ? , fullname = ? , code = ? where id = ? ";

        try{
            db.setConnection();
            PreparedStatement st = db.getConnection().prepareStatement(sql);
            st.setString(1, o.getName());
            st.setString( 2, o.getFullName());
            st.setString( 3, o.getCode());
            st.setLong(4, o.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(PhoneType o) throws ClassNotFoundException {
        String sql =
                "delete from dict_phonetype  where id = ? ";

        try{
            db.setConnection();
            PreparedStatement st = db.getConnection().prepareStatement(sql);
            st.setLong(1, o.getId());

            st.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PhoneType> findAll() throws ClassNotFoundException {
        List<PhoneType> result = new ArrayList<>();
        String sql =
                "select * from dict_phonetype ";

        try{
            db.setConnection();
            PreparedStatement st = db.getConnection().prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                PhoneType o = new PhoneType();
                o.setId(rs.getLong("id"));
                o.setName(rs.getString("name"));
                o.setFullName(rs.getString("fullname"));
                o.setCode(rs.getString("code"));

                result.add(o);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Optional<PhoneType> find(PhoneType model) throws ClassNotFoundException {
        String sql = "select * from dict_phonetype where id=?";
        try{
            db.setConnection();
            PreparedStatement st = db.getConnection().prepareStatement(sql);
            st.setLong(1,model.getId());
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                PhoneType phoneType = new PhoneType();
                phoneType.setId(rs.getLong("id"));
                phoneType.setName(rs.getString("name"));
                phoneType.setFullName(rs.getString("fullname"));
                phoneType.setCode(rs.getString("code"));
                rs.close();
                return Optional.of(phoneType);
            }
            else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<PhoneType> getByName(String name) throws ClassNotFoundException {
        List<PhoneType> phoneTypes = new ArrayList<>();
        String sql = "select * from dict_phonetype where fullname=?";
        try{
            db.setConnection();
            PreparedStatement st = db.getConnection().prepareStatement(sql);
            st.setString(1,name);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                PhoneType phoneType = new PhoneType();
                phoneType.setId(rs.getLong("id"));
                phoneType.setName(rs.getString("name"));
                phoneType.setFullName("name");
                phoneType.setCode(rs.getString("code"));
                phoneTypes.add(phoneType);
            }
            rs.close();
            return phoneTypes;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PhoneType> getByCode(String code) throws ClassNotFoundException {
        List<PhoneType> phoneTypes = new ArrayList<>();
        String sql="select * from dict_phonetype where code=?";
        try{
            db.setConnection();
            PreparedStatement st = db.getConnection().prepareStatement(sql);
            st.setString(1,code);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                PhoneType phoneType = new PhoneType();
                phoneType.setId(rs.getLong("id"));
                phoneType.setName(rs.getString("name"));
                phoneType.setFullName(rs.getString("fullname"));
                phoneType.setCode(rs.getString("code"));
                phoneTypes.add(phoneType);
            }
            rs.close();
            return phoneTypes;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<PhoneType> getByNameAndCode(String name, String code) throws ClassNotFoundException {
        String sql = "select * from dict_phonetype where name=? and code=? and fullname=?";
        try{
            db.setConnection();
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2, code);
            ps.setString(3, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                PhoneType phoneType = new PhoneType();
                phoneType.setId(rs.getLong("id"));
                phoneType.setName(rs.getString("name"));
                phoneType.setFullName(rs.getString("fullname"));
                phoneType.setCode(rs.getString("code"));
                return Optional.of(phoneType);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }
}
