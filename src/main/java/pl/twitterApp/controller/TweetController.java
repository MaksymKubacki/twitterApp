package pl.twitterApp.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.twitterApp.bean.SessionManager;
import pl.twitterApp.entity.Comment;
import pl.twitterApp.entity.Tweet;
import pl.twitterApp.entity.User;
import pl.twitterApp.repository.CommentRepository;
import pl.twitterApp.repository.TweetRepository;

@Controller
@RequestMapping("/tweet")
public class TweetController {
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	CommentRepository commentRepository;

	@PostMapping("/add")
	public String addPost(@Valid @ModelAttribute Tweet tweet, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "home";
		}
		HttpSession s = SessionManager.session();
		User u = (User)s.getAttribute("user");
		tweet.setUser(u);
		tweet.setCreated(new Date());
		this.tweetRepository.save(tweet);
		return "redirect:/";
	}
	
	@GetMapping("/{id}")
	public String oneTweet(Model m, @PathVariable long id) {
		Tweet tweet = this.tweetRepository.findOne(id);
		List<Comment> comments = this.commentRepository.findByTweetIdOrderByCreatedDesc(id);
		m.addAttribute("tweet", tweet);
		m.addAttribute("comments", comments);
		m.addAttribute("comment", new Comment());
		return "single_tweet";
	}
	
	@PostMapping("/addComment/{tweetId}")
	public String addPost(@Valid @ModelAttribute Comment comment, BindingResult bindingResult, @PathVariable long tweetId) {
		if (bindingResult.hasErrors()) {
			return "redirect:/tweet/" + tweetId;
		}
		HttpSession s = SessionManager.session();
		User u = (User) s.getAttribute("user");
		Tweet tweet = this.tweetRepository.findOne(tweetId);
		comment.setTweet(tweet);
		comment.setUser(u);
		comment.setCreated(new Date());
		this.commentRepository.save(comment);
		return "redirect:/tweet/" + tweetId;
	}
}