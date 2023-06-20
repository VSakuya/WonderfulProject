package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 会員詳細画面コントローラ
 * 
 * @author Kaine
 */
@Controller
public class ClientUserShowController {

	/**
	 * 会員情報 リポジトリ
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * 会員詳細画面
	 * @param model リクエストスコープ
	 * @return "client/user/detail" 会員詳細画面
	 */
	@RequestMapping(path = "/client/user/detail")
	public String showUser(Model model) {

		// セッション保持情報から値を再取得
		UserBean userBean = (UserBean) session.getAttribute("user");

		// Beanに保存した値からユーザー名を取得
		String name = userBean.getName();

		// ユーザー名に該当する会員情報を取得
		User user = userRepository.findByName(name);

		// リクエストスコープに登録
		model.addAttribute("user", user);

		// 会員詳細画面を表示
		return "client/user/detail";
	}
}
