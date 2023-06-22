package jp.co.sss.shop.controller.client.order.payment.input;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 支払方法ページ（一般会員）
 * 
 * 
 * @author Ohashi
 *
 */
@Controller
public class ClientOrderRegistController {

	/**
	 * 会員情報　リポジトリ
	 */
	@Autowired
	UserRepository userRepository;
	
	OrderBean orderBean;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;
	
	/**
	 * 会員登録 入力画面表示処理
	 *
	 * @return "/client/order/address_input" 会員登録入力画面
	 */
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
	public String registUser(@ModelAttribute OrderForm orderForm) {
		return "client/order/address_input";
	}
	/**
	 * 会員登録 入力チェック処理
	 * @param userForm 会員フォーム
	 * @param result 入力チェック結果
	 * @return
	 		エラーがあった場合:"client/user/regist_input" 入力画面表示
			エラーがない場合:"redirect:/client/user/regist/check" 確認画面へリダイレクト
	 */
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
	public String doRegistUser(@Valid @ModelAttribute OrderForm orderForm, BindingResult result) {
		String returnStr = "client/order/address_input";
		if (result.hasErrors()) {
			
			// エラーがあった場合、セッションにエラー情報を保存
			session.setAttribute("result", result);
			
			// 入力画面を再表示
			returnStr = "client/order/address_input";
		} else {
			
			// セッションに入力情報を登録
			session.setAttribute("userForm", orderForm);
			
			// 確認画面へリダイレクト
			returnStr = "redirect:/client/order/payment/input";
		}
		
		return returnStr;
	}
	
	/**
	 * 会員登録 確認画面表示処理
	 *
	 * @return "client/user/regist_check" 注文確認画面
	 */
	@RequestMapping(path = "/client/order/check")
	public String checkRegistUser() {
		return "client/order/check";
	}
	
	
	/**
	 * 購入完了画面　表示処理
	 *
	 * @return "client/order/complete" 注文完了画面　表示
	 */
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
	public String registCompleteFinish() {
		return "client/order/complete";
	}

	
//	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
//	public String registInput() {
//		
//		return "redirect:/client/order/address_input";
//	}
//	
//	@RequestMapping(path = "/client/order/address/back", method = RequestMethod.POST)
//	public String registback() {
//		
//		return "redirect:/client/order/address/input";
//	}
//
//	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
//	public String showRegist() {
//		
//		return "client/order/address_input";
////	}

	
	//フォーム画面の表示
	@RequestMapping(path ="/client/order/payment/input", method = {RequestMethod.GET})
	public String paymentInputOrder() {
		return "client/order/payment_input";
	}
	@RequestMapping(path ="/client/order/payment/input", method = {RequestMethod.POST})
	public String doPaymentInputOrder(@ModelAttribute UserForm userForm) {
		return "client/order/payment_input";
		
	}
}
