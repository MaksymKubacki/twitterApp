package pl.twitterApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.twitterApp.entity.Message;
import pl.twitterApp.entity.User;

public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findByRecieverIdOrderByCreatedDesc(long id);
	List<Message> findByRecieverOrderByCreatedDesc(User reciever);
	List<Message> findBySenderOrderByCreatedDesc(User sender);
	List<Message> findByRecieverAndCheckedLikeOrderByCreatedDesc(User reciever, int checked);
}