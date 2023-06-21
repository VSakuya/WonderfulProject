package jp.co.sss.shop.controller.client.order.payment.input;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.OrderBean;
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
	//フォーム画面の表示
	@RequestMapping(path ="/client/order/payment/input", method = {RequestMethod.POST})
	public String paymentInputOrder(Model model) {
		//セッションスコープより入力情報を取り出す
				UserForm userForm = (UserForm) session.getAttribute("userForm");
//				if (userForm == null) {
//					userForm = new UserForm();
//					userForm.setAuthority(((UserBean) session.getAttribute("user")).getAuthority());

					//空の入力フォーム情報をセッションに保持 登録ボタンからの遷移
					session.setAttribute("userForm", userForm);
//				}
					

				//登録入力画面　表示処理
				return "order/payment_input";

			}
//
//			/**
//			 * 入力画面　表示処理(GET)
//			 * 
//			 * @param model Viewとの値受渡し
//			 * @return "admin/user/regist_input" 入力画面　表示
//			 */
//			@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.GET)
//			public String registInput(Model model) {
//
//				UserForm userForm = (UserForm) session.getAttribute("userForm");
//				if (userForm == null) {
//					// セッション情報がない場合、エラー
//					return "redirect:/syserror";
//				}
//
//				BindingResult result = (BindingResult) session.getAttribute("result");
//				if (result != null) {
//					//セッションにエラー情報がある場合、エラー情報をスコープに設定
//					model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
//					// セッションにエラー情報を削除
//					session.removeAttribute("result");
//				}
//
//				// 入力フォーム情報をスコープに設定
//				model.addAttribute("userForm", userForm);
//
//				// 入力画面　表示
//				return "client/order/payment_input";
//
//			}

//		//選択肢
//		List<String> selectLists = new ArrayList<>();
//		selectLists.add(1);
//		selectLists.add();
//		selectLists.add();
//		selectLists.add("電子マネー");
//		selectLists.add("コンビニ決済");
//		model.addAttribute("selectLists", selectLists);
//		return "client/order/payment_input";

//	//送信ボタンクリック
//	@RequestMapping(path ="client/order/payment_input", method = {RequestMethod.POST})
//	public String paymentResult(@ModelAttribute FormContents formContents,Model model) {
//		
//	}
	
	
	
	
	
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
	public String registUser() {
		return "client/order/address_input";
	}
	
	
	
//	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
//	public String doRegistUser(@Valid @ModelAttribute OrderForm orderForm, BindingResult result) {
		
//		UserBean userBean = (UserBean) session.getAttribute("user");
//		int userId = userBean.getId();
//		User user = userRepository.findByIdAndDeleteFlag(userId, 0);
//		
//		
//		session.setAttribute("orderForm", user);
		
//		String returnStr = "client/order/address_input";
//		if (result.hasErrors()) {
//			
//			// エラーがあった場合、セッションにエラー情報を保存
//			session.setAttribute("result", result);
//			
//			// 入力画面を再表示
//			returnStr = "client/order/address_input";
//		} else {
//			
//			// セッションに入力情報を登録
//			session.setAttribute("orderForm", orderForm);
//			
//			// 確認画面へリダイレクト
//			returnStr = "redirect:/client/order/check";
//		}
//		
//		return returnStr;
//	}
}
