package jp.co.sss.shop.controller.client.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 一般会員登録コントローラクラス
 * 
 * @author Kaine
 */
@Controller
public class ClientUserRegistController {
	/**
	 * ユーザー情報
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * 会員登録 入力画面表示処理
	 * @return "/client/user/regist_input" 会員登録入力画面
	 */
	@RequestMapping(path = "/client/user/regist/input")
	public String createUser() {
		return "client/user/regist_input";
	}

	/**
	 * 会員登録 入力チェック処理
	 * @param userForm 会員フォーム
	 * @param result 入力チェック結果
	 * @return 
	 		誤りがあった場合:"client/user/regist_input" 入力画面再表示
			誤りがない場合:"redirect:/client/user/regist_check" 確認画面表示
	 */
	@RequestMapping(path = "/client/user/regist/input", method = RequestMethod.POST)
	public String doCreateUser(@Valid @ModelAttribute UserForm userForm, BindingResult result) {
		String returnStr = "client/user/regist_input";
		if (result.hasErrors()) {
			//入力値に誤りがあった場合、再度入力画面を表示
			returnStr = "client/user/regist_input";
		}
		return returnStr;
	}

	/**
	 * 会員登録 確認画面表示処理
	 * @return "client/user/regist_check" 会員登録確認画面
	 */
	@RequestMapping(path = "/client/user/regist/check", method = RequestMethod.POST)
	public String checkCreateUser() {
		return "client/user/regist_check";
	}

	/**
	 * 会員登録 完了画面表示処理
	 * @return "client/user/complete" 会員登録完了画面
	 */
	@RequestMapping(path = "/client/user/regist/complete", method = RequestMethod.POST)
	public String completeCreateUser() {
		return "client/user/complete_user";
	}

}
