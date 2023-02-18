package com.thegoate.spring.boot;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stupid tricky bits with oracle jdbc driver.
 * Too lazy to set up access to oracle's maven repo.
 * commented out the offending code until it can be revisted.
 * Simple DAO for inserting and querying simple table.
 * Created by Eric Angeli on 11/1/2017.
 */
@Repository
@Transactional
public class SimpleDAO {//extends JdbcDaoSupport{
    BleatBox LOG = BleatFactory.getLogger(getClass());

//    @Autowired
//    private OracleDataSource dataSource;

//    @PostConstruct
//    private void initialize(){
//        setDataSource(dataSource);
//    }

    public void insert(SimplePojo pojo){
        String sql = "INSERT INTO simple (MESSAGE, ID) VALUES (?, ?)";
        Connection connection = null;
        try{
            connection = getConnect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, pojo.getMessage());
            ps.setString(2, pojo.getId());
            ps.executeUpdate();
            ps.close();
        } catch(SQLException sqlE) {
            LOG.error("Problem with insert: " + sqlE.getMessage(), sqlE);
        }finally {
            if(connection!=null){
                try{
                    connection.close();
                }catch(SQLException sqlE){
                    LOG.error("problem closing connection: " + sqlE.getMessage(), sqlE);
                }
            }
        }
    }

    public SimplePojo findById(String id){

        String sql = "SELECT * FROM simple WHERE ID = ?";

        Connection conn = null;
        SimplePojo pojo = null;

        try {
            conn = getConnect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pojo = new SimplePojo(
                        rs.getString("MESSAGE"),
                        rs.getString("ID")
                );
            }
            rs.close();
            ps.close();
        } catch (SQLException sqlE) {
            LOG.error("Problem with query: " + sqlE.getMessage(), sqlE);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlE) {
                    LOG.error("Problem with closing after query: " + sqlE.getMessage(), sqlE);
                }
            }
        }
        return pojo;
    }

    protected Connection getConnect() throws SQLException{
//        Connection conn = dataSource.getConnection();
//        conn.setAutoCommit(false);
        return null;
    }

    public int countRows(){

        String sql = "SELECT COUNT(*) as C FROM simple";

        Connection conn = null;
        int count = -42;

        try {
            conn = getConnect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("C");
            }
            rs.close();
            ps.close();
        } catch (SQLException sqlE) {
            LOG.error("Problem with query: " + sqlE.getMessage(), sqlE);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlE) {
                    LOG.error("Problem with closing after query: " + sqlE.getMessage(), sqlE);
                }
            }
        }
        LOG.info("count: " + count);
        return count;
    }
}
