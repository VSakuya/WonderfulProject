package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserUpdateForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 会員情報変更機能のコントローラクラス
 *
 * @author Kaine
 */
@Controller
public class ClientUserUpdateController {

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
	 * 入力画面　初期表示処理
	 *
	 * @return "client/user/update_input" 入力画面 表示処理
	 */
	@RequestMapping(path = "/client/user/update/input/init", method = RequestMethod.POST)
	public String updateInput() {

		// セッションスコープより入力情報を取り出す
		UserUpdateForm userForm = (UserUpdateForm) session.getAttribute("userForm");
		if (userForm == null) {

			// セッション情報がない場合、詳細画面からの遷移と判断し初期値を準備
			// 変更対象の情報取得
			UserBean userBean = (UserBean) session.getAttribute("user");
			if(userBean == null) {
				return "redirect:/login";
			}
			
			// Beanに保存した値からユーザー名を取得
			String name = userBean.getName();

			// ユーザー名に該当する会員情報を取得
			User user = userRepository.findByName(name);

			if (user == null) {
				// 対象が無い場合、エラー
				return "redirect:/syserror";
			}

			// フォーム初期値の生成
			UserUpdateForm userUpdateForm = new UserUpdateForm();
			BeanUtils.copyProperties(user, userUpdateForm);

			// 変更入力フォームをセッションに保持
			session.setAttribute("userForm", userUpdateForm);
		}

		// 変更入力画面表示
		return "redirect:/client/user/update/input";
	}

	@RequestMapping(path = "/client/user/update/input", method = RequestMethod.GET)
	public String updateInput(Model model) {

		//セッションから入力フォーム取得
		UserUpdateForm userForm = (UserUpdateForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッション情報がない場合、
			return "redirect:/login";
		}
			// 入力フォーム情報を画面表示設定
			model.addAttribute("userForm", userForm);
		
		
		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報を画面表示設定
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			session.removeAttribute("result");
		}

		//変更入力画面　表示
		return "client/user/update_input";

	}

	@RequestMapping(path = "/client/user/update/check", method = RequestMethod.POST)
	public String updateInputCheck(@Valid @ModelAttribute UserUpdateForm form, BindingResult result) {

		//直前のセッション情報を取得
		UserUpdateForm lastUserForm = (UserUpdateForm) session.getAttribute("userForm");
		if (lastUserForm == null) {
			// セッション情報が無い場合
			return "redirect:/login";
		}
		if (form.getAuthority() == null) {
			//権限情報がない場合、セッション情報から値をセット
			form.setAuthority(lastUserForm.getAuthority());
		}

		// 入力フォーム情報をセッションに保持
		session.setAttribute("userForm", form);

		// 入力値にエラーがあった場合、入力画面に戻る
		if (result.hasErrors()) {

			session.setAttribute("result", result);

			//変更入力画面　表示処理
			return "redirect:/client/user/update/input";

		}

		//変更確認画面　表示処理
		return "redirect:/client/user/update/check";
	}

	@RequestMapping(path = "/client/user/update/check", method = RequestMethod.GET)
	public String updateCheck(Model model) {

		//セッションから入力フォーム情報取得
		UserUpdateForm userForm = (UserUpdateForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		//入力フォーム情報をスコープへ設定
		model.addAttribute("userForm", userForm);

		// 変更確認画面　表示
		return "client/user/update_check";

	}

	@RequestMapping(path = "/client/user/update/complete", method = RequestMethod.POST)
	public String updateComplete() {

		//セッション保持情報から入力値再取得
		UserUpdateForm userForm = (UserUpdateForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		// セッション情報がない場合、詳細画面からの遷移と判断し初期値を準備
		// 変更対象の情報取得
		UserBean userBean = (UserBean) session.getAttribute("user");

		// Beanに保存した値からユーザー名を取得
		String name = (String) userBean.getName();

		// ユーザー名に該当する会員情報を取得
		User user = new User();
		user = userRepository.findByName(name);
		if (user == null) {
			// 対象が無い場合、エラー
			return "redirect:/syserror";
		}

		// 各項目の保存
		user.setEmail(userForm.getEmail());

		user.setPassword(userForm.getPassword());

		user.setName(userForm.getName());

		user.setPostalCode(userForm.getPostalCode());

		user.setAddress(userForm.getAddress());

		user.setPhoneNumber(userForm.getPhoneNumber());

		// 情報をDBに登録
		userRepository.save(user);

		// ログインユーザ情報変更の場合、セッション保存ユーザ情報を更新
		UserBean loginUser = (UserBean) session.getAttribute("user");
		loginUser.setName(userForm.getName());

		// セッションから情報を削除
		session.removeAttribute("user");
		session.removeAttribute("userForm");

		// ユーザー情報をセッションに登録
		session.setAttribute("user", loginUser);

		// 変更完了画面　表示処理
		return "redirect:/client/user/update/complete";
	}

	@RequestMapping(path = "/client/user/update/complete", method = RequestMethod.GET)
	public String updateCompleteFinish() {

		return "client/user/update_complete";
	}

}
