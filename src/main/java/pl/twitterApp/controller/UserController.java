package pl.twitterApp.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.twitterApp.bean.LoginData;
import pl.twitterApp.bean.SessionManager;
import pl.twitterApp.entity.User;
import pl.twitterApp.repository.UserRepository;

@Controller

public class UserController {
	
	@Autowired
	UserRepository userRepo;
	
	
	@GetMapping("/register")
	public String register(Model m) {
		m.addAttribute("user", new User());
		return "register";
	}
	@PostMapping("/register")
	public String registerPost(@Valid @ModelAttribute User user, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "redirect:/register";
		}else {
			this.userRepo.save(user);
//			HttpSession s = SessionManager.session();
//			s.setAttribute("user", user);
			return "redirect:/";
		}
	}
	@GetMapping("/login")
	public String login(Model m) {
		
		m.addAttribute("loginData", new LoginData());
		return "login";
	}
	@PostMapping("/login")
	public String loginPost(@ModelAttribute LoginData loginData, Model m, RedirectAttributes ra) {
		User u = this.userRepo.findOneByEmail(loginData.getEmail());
		
			if(u != null && u.isPasswordCorrect(loginData.getPassword())) {
				HttpSession s = SessionManager.session();
				s.setAttribute("user", u);
				ra.addFlashAttribute("msg", "Jesteś zalogowany");
				return "redirect:/";
			}
		
			m.addAttribute("msg", "Wprowadz poprawne dane");
			return "login";
		}
	
	@GetMapping("/change")
	public String change(Model m) {
		HttpSession s = SessionManager.session();
		User u = (User) s.getAttribute("user");
		m.addAttribute("user", u);
		return "change";
	}

	@PostMapping("/change")
	public String changePost(@Valid @ModelAttribute User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "redirect:/register";
		}
		HttpSession s = SessionManager.session();
		User u = (User) s.getAttribute("user");
		user.setId(u.getId());
		this.userRepo.save(user);

		return "redirect:/";
	}
	
	@GetMapping("/delete")
	public String delete(Model m) {
		HttpSession s = SessionManager.session();
		User u = (User) s.getAttribute("user");
		m.addAttribute("user", u);
		return "delete";
	}
	
	@GetMapping("/delete/{dec}")
	public String deletePost(@PathVariable int dec) {
		if (dec == 1) {
		HttpSession s = SessionManager.session();
		User u = (User) s.getAttribute("user");
		s.invalidate();
		this.userRepo.delete(u);
		return "redirect:/login";
		}
		return "redirect:/";
}

	@GetMapping("/logout")
	public String logout(Model m) {
		m.addAttribute("loginData", new LoginData());
		HttpSession s = SessionManager.session();
		s.invalidate();
		return "redirect:/";
		
	}
	
	
}
