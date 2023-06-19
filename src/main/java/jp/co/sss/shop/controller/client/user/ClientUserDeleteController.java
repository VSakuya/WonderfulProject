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
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientUserDeleteController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	@RequestMapping(path = "/client/user/delete/check", method = RequestMethod.POST)
	public String deleteCheck() {
		// セッションスコープより入力情報を取り出す
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {

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

		return "client/user/delete_check";
	}

	@RequestMapping(path = "/client/user/delete/complete")
	public String deleteComplete() {
		
		// セッション情報から削除対象フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");

		// 削除対象の会員情報を取得
		User user = userRepository.findByIdAndDeleteFlag(userForm.getId(), Constant.NOT_DELETED);

		// 削除フラグを立てる
		user.setDeleteFlag(Constant.DELETED);
		
		// 会員情報を保存
		userRepository.save(user);
		
		// セッション情報を削除
		session.invalidate();
		
		// 削除完了画面表示
		return "client/user/delete_complete";
	}
}
