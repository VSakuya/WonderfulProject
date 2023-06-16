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

/**
 * 会員情報変更機能のコントローラクラス
 * 
 * @author Kaine
 */
@Controller
public class ClientUserUpdateController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	@RequestMapping(path = "/client/user/update/input", method = RequestMethod.POST)
	public String updateInput() {
		
		// セッションスコープより入力情報を取り出す
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if(userForm == null) {
			
			// セッション情報がない場合、詳細画面からの遷移と判断し初期値を準備
			// 変更対象の情報を取得
			UserBean userBean = (UserBean) session.getAttribute("user");
			
			// Beanに保存した値からユーザー名を取得
			String name = userBean.getName();
			
			// ユーザー名に該当する会員情報を取得
			User user = userRepository.findByName(name);
			
			// フォーム初期値の生成
			userForm = new UserForm();
			BeanUtils.copyProperties(user, userForm);
			
			// 変更入力フォームをセッションに保持
			session.setAttribute("userForm", userForm);
		}
		
		// 変更入力画面表示
		return "client/user/update_input";
	}
}
