package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

/**
 * 一般会員 登録機能のコントローラクラス
 *
 * @author Kaine
 */
@Controller
public class ClientUserRegistController {
	/**
	 * 会員情報　リポジトリ
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * 会員登録 入力画面表示処理
	 *
	 * @return "/client/user/regist_input" 会員登録入力画面
	 */
	@RequestMapping(path = "/client/user/regist/input", method = RequestMethod.GET)
	public String registUser(@ModelAttribute UserForm userForm) {
		return "client/user/regist_input";
	}

	/**
	 * 会員登録 入力チェック処理
	 * @param userForm 会員フォーム
	 * @param result 入力チェック結果
	 * @return
	 		エラーがあった場合:"client/user/regist_input" 入力画面表示
			エラーがない場合:"redirect:/client/user/regist/check" 確認画面へリダイレクト
	 */
	@RequestMapping(path = "/client/user/regist/input", method = RequestMethod.POST)
	public String doRegistUser(@Valid @ModelAttribute UserForm userForm, BindingResult result) {
		String returnStr = "client/user/regist_input";
		userForm.setAuthority(Constant.AUTH_CLIENT);
		if (result.hasErrors()) {

			// エラーがあった場合、セッションにエラー情報を保存
			session.setAttribute("result", result);

			// 入力画面を再表示
			returnStr = "client/user/regist_input";
		} else {

			// セッションに入力情報を登録
			session.setAttribute("userForm", userForm);

			// 確認画面へリダイレクト
			returnStr = "redirect:/client/user/regist/check";
		}

		return returnStr;
	}

	/**
	 * 会員登録 確認画面表示処理
	 *
	 * @return "client/user/regist_check" 会員登録確認画面
	 */
	@RequestMapping(path = "/client/user/regist/check")
	public String checkRegistUser() {
		return "client/user/regist_check";
	}

	/**
	 * 会員登録 完了画面表示処理
	 *
	 * @return "client/user/complete" 会員登録完了画面
	 */
	@RequestMapping(path = "/client/user/regist/complete")
	public String completeRegistUser() {

		// セッション保持情報から入力値再取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");

		// 会員情報を生成
		User user = new User();

		// 入力フォーム情報をエンティティに設定
		BeanUtils.copyProperties(userForm, user);

		// DB登録
		userRepository.save(user);

		// セッションから入力情報削除
		session.removeAttribute("userForm");

		// 登録完了画面を表示
		return "client/user/regist_complete";
	}

}
