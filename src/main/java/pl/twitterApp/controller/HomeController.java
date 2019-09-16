package pl.twitterApp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import pl.twitterApp.bean.SessionManager;
import pl.twitterApp.entity.Message;
import pl.twitterApp.entity.Tweet;
import pl.twitterApp.entity.User;
import pl.twitterApp.repository.TweetRepository;

@Controller
public class HomeController {
	
	@Autowired
	private TweetRepository tweetRepository;

	@GetMapping("")
	public String home(Model m) {
		m.addAttribute("tweet", new Tweet());
		return "home";
	}
	
	@GetMapping("usersTweets/{userId}")
	public String userTweets(@PathVariable long userId, Model m) {
		HttpSession s = SessionManager.session();
		User u = (User) s.getAttribute("user");
		if (u != null && u.getId() == userId) {
			return "redirect:/user";
		}
		m.addAttribute("message", new Message());
		List<Tweet> usersTweets = this.tweetRepository.findAllByUserIdOrderByCreatedDesc(userId);
		m.addAttribute("usersTweets", usersTweets);
		m.addAttribute("recieverId", userId);
		return "users";
	}
	
	@ModelAttribute("availableTweets")
	public List<Tweet> getTweets() {
		return this.tweetRepository.findAll();
	}
}