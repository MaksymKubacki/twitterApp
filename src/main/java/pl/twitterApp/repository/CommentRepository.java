package pl.twitterApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.twitterApp.entity.Comment;
import pl.twitterApp.entity.Tweet;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByTweet(Tweet tweet);
	List<Comment> findByTweetIdOrderByCreatedDesc(Long id);
}