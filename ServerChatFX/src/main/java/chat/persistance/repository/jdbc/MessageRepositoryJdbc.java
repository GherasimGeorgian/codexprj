package chat.persistance.repository.jdbc;

import chat.model.Message;
import chat.persistance.MessageRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class MessageRepositoryJdbc implements MessageRepository {
    JdbcUtils jdbcUtils;
    public MessageRepositoryJdbc(Properties props){
        jdbcUtils=new JdbcUtils(props);
    }
    @Override
    public Message save(Message message) {
        Connection con=jdbcUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("insert into messages (sender,receiver,mtext, mdata) values (?,?,?,?)")){

            preStmt.setString(1,message.getSender().getId());
            preStmt.setString(2,message.getReceiver().getId());
            preStmt.setString(3,message.getText());
            preStmt.setString(4, LocalDate.now().toString());
            if (preStmt.executeUpdate()<1)
                System.err.println("Message not saved");
        } catch (SQLException e) {
            System.err.println("Error Jdbc "+e);
        }
        return message;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Message findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, Message message) {

    }

    @Override
    public Iterable<Message> getAll() {
        return null;
    }
}
