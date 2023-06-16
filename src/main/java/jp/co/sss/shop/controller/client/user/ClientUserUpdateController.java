package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientUserUpdateController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	@RequestMapping(path = "/client/user/update/input", method = RequestMethod.POST)
	public String updateInput() {

		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if(userForm == null) {
			UserBean userBean = (UserBean) session.getAttribute("user");
			String name = userBean.getName();
			User user = userRepository.findByName(name);
			
			userForm = new UserForm();
			BeanUtils.copyProperties(user, userForm);
			
			session.setAttribute("userForm", userForm);
		}
		return "client/user/update_input";
	}
}
