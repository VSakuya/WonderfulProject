package jp.co.sss.shop.controller.client.order;

import java.util.ArrayList;
import java.util.List;

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

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Image;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.service.BeanTools;

/**
 * 
 * @author Wada
 * 
 * 商品の注文機能　コントローラクラス
 */
@Controller
public class ClientOrderRegistController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	BeanTools beanTools;
	
	/**
	 * 会員情報リポジトリ
	 */
	@Autowired
	UserRepository userRepository;
	
	/**
	 * 商品情報リポジトリ
	 */
	@Autowired
	ItemRepository itemRepository;
	
	/**
	 * 注文情報リポジトリ
	 */
	@Autowired
	OrderRepository orderRepository;
	
	/**
	 * 注文商品リポジトリ
	 */
	@Autowired
	OrderItemRepository orderItemRepository;
	
	/**
	 * 買い物かご画面のご注文のお手続きボタン押下後
	 * ユーザ情報をもとに注文情報の作成
	 * 
	 * @return　"/client/order/address/input"へリダイレクト
	 */
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
	public String order() {
		//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			//ユーザ情報の取得
			int userId = userBean.getId();
			User user = userRepository.findByIdAndDeleteFlag(userId, 0);
			//注文フォームの生成
			OrderForm orderForm = new OrderForm();
			//ユーザ情報をもとに注文情報を生成
			orderForm.setId(user.getId());
			orderForm.setPostalCode(user.getPostalCode());
			orderForm.setAddress(user.getAddress());
			orderForm.setName(user.getName());
			orderForm.setPhoneNumber(user.getPhoneNumber());
			orderForm.setPayMethod(1);
			//セッションスコープへ注文情報を保存
			session.setAttribute("orderForm", orderForm);

			return "redirect:/client/order/address/input";

		} else {
			return "redirect:/login";
		}
	}

	
	
	
	/**
	//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
	UserBean userBean = (UserBean) session.getAttribute("user");
	if (userBean != null) {

		

	} else {
		return "redirect:/login";
	}
	 */
	
	
	
	
	
	
	
	/**
	 * 届け先入力画面へ遷移
	 * 
	 * @return　届け先入力画面へ遷移
	 */
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
	public String inputAddress(OrderForm form) {
		//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			
			OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
			//届け先入力画面へ遷移
			return "client/order/address_input";
			
		} else {
			return "redirect:/login";
		}
	}

	/**
	 * 届け先入力画面で次へボタン押下後処理
	 * 
	 * @return　"/client/order/payment/input"へリダイレクト
	 */
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.POST)
	public String payment(@Valid @ModelAttribute OrderForm orderForm, BindingResult result) {
		//ログインチェック　ログイン画面がなければログイン画面へ遷移する
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {

			if (result.hasErrors()) {

				session.setAttribute("result", result);

				return "redirect:/client/order/address/input";
			}
			//以前の注文フォーム情報を取得
			OrderForm exOrderForm = (OrderForm) session.getAttribute("orderForm"); 
			//入力情報をもとに注文情報の更新
			exOrderForm.setAddress(orderForm.getAddress());
			exOrderForm.setPostalCode(orderForm.getPostalCode());
			exOrderForm.setName(orderForm.getName());
			exOrderForm.setPhoneNumber(orderForm.getPhoneNumber());
			//セッションスコープに更新後の注文情報を保存
			session.setAttribute("orderForm", exOrderForm);

			return "redirect:/client/order/payment/input";

		} else {
			return "redirect:/login";
		}
	}

	/**
	 * 支払い方法選択画面へ遷移
	 * 
	 * @return　支払い情報画面へ遷移
	 */
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.GET)
	public String paymentInput() {

		//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {

			//支払い方法選択画面へ遷移
			return "client/order/payment_input";

		} else {
			return "redirect:/login";
		}
	}

	/**
	 * 支払い方法選択画面で戻るボタン押下時
	 * 
	 * @return　"/client/order/address/input"へリダイレクト
	 */
	@RequestMapping(path = "/client/order/payment/back", method = RequestMethod.POST)
	public String backToAddressInput() {
		//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {

			return "redirect:/client/order/address/input";

		} else {
			return "redirect:/login";
		}
	}

	/**
	 * 選択された支払い方法をもとに注文情報を更新
	 * 
	 * @param payMethod 選択された支払い方法
	 * 
	 * @return　"/client/order/check"へリダイレクト
	 */
	@RequestMapping(path = "/client/order/check", method = RequestMethod.POST)
	public String check(int payMethod) {
		
		//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {

			//セッションスコープから注文情報を取得
			OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
			//支払い方法を更新
			orderForm.setPayMethod(payMethod);
			//セッションスコープに注文情報を保存
			session.setAttribute("orderForm", orderForm);

			return "redirect:/client/order/check";	

		} else {
			return "redirect:/login";
		}
	}

	/**
	 * 注文内容確認画面
	 * 在庫状況をもとに注文商品情報の更新を行う
	 * 
	 * @param model　
	 * 
	 * @return　注文内容確認画面へ遷移
	 */
	@RequestMapping(path = "/client/order/check", method = RequestMethod.GET)
	public String orderCheck(Model model) {

		//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {

			//セッションスコープより買い物かご情報の取得
			@SuppressWarnings("unchecked")
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");

			//BasketBean型の変数を宣言
			BasketBean basketItem;
			//合計金額変数の宣言
			int total = 0;
			//買い物かご商品の削除フラグチェック
			for (int i = 0; i < basketList.size(); i++) {

				basketItem = basketList.get(i);
				int itemId = basketItem.getId();
				//itemIdの取得　IdをもとにItem型の変数を作成
				Item item = itemRepository.getReferenceById(itemId);
				//削除フラグチェック　削除フラグが立っている場合、該当商品を削除
				if(item.getDeleteFlag() == 1) {
					basketList.remove(i);
				}
			}
			//在庫切れ商品の商品名格納用リストの宣言
			List<String> stockNoneList = new ArrayList<>();
			//在庫不足商品の商品名格納用リストの宣言
			List<String> stockShortList = new ArrayList<>();
			//商品の在庫チェック
			for (int i = 0; i < basketList.size(); i++) {

				basketItem = basketList.get(i);
				int itemId = basketItem.getId();
				//itemIdの取得　IdをもとにItem型の変数を作成
				Item item = itemRepository.getReferenceById(itemId);
				//該当商品が在庫切れの場合
				if (item.getStock() == 0) {
					//商品を削除
					basketList.remove(i);
					//削除情報をセッションスコープに保存
					session.setAttribute("baskets", basketList);
					//在庫切れ商品の商品名をリストに追加
					stockNoneList.add(item.getName());
					//注文数が在庫数を超えている場合
				} else if (basketItem.getOrderNum() > item.getStock()) {
					//注文数=在庫数に変更
					basketItem.setOrderNum(item.getStock());
					basketList.set(i, basketItem);
					//更新した情報をセッションスコープに保存
					session.setAttribute("baskets", basketList);
					//在庫不足商品の商品名をリストに追加
					stockShortList.add(item.getName());
				}
			}
			//リクエストスコープに在庫切れ、在庫不足商品の情報を保存
			model.addAttribute("stockNone", stockNoneList);
			model.addAttribute("stockShort", stockShortList);

			if (basketList.size() == 0) {
				session.removeAttribute("baskets");
				session.removeAttribute("orderItems");
				return "client/order/check";
			}

			List<OrderItemBean> orderList = new ArrayList<>();
			OrderItemBean orderItemBean = new OrderItemBean();

			for (int i = 0; i < basketList.size(); i++) {
				//買い物かごの商品情報取得
				basketItem = basketList.get(i);
				//商品IDをもとにItem型の変数を作成
				int id = basketItem.getId();
				Item item = itemRepository.getReferenceById(id);
				//orderItemBeanに情報を保存
				orderItemBean = beanTools.generateOrderItemBean(item, basketItem);
				Image image = (item.getImageList()).get(0);
				String imagePath = image.getImagePath();
				orderItemBean.setImage(imagePath);
				//orderItemBeanをorderListに追加
				orderList.add(orderItemBean);
				//合計金額の計算
				total += basketItem.getOrderNum() * item.getPrice();
			}
			//合計金額をリクエストスコープに保存
			model.addAttribute("total", total);
			//注文商品リストをセッションスコープに保存
			session.setAttribute("orderItems", orderList);
			//注文内容確認画面へ遷移
			return "client/order/check";

		} else {
			return "redirect:/login";
		}
	}

	/**
	 * 注文内容確認画面で戻るボタンを押下後の処理
	 * ログインチェックを行う
	 * 
	 * @return　"/client/order/payment/input"へリダイレクト
	 */
	@RequestMapping(path = "/client/order/check/back", method = RequestMethod.POST)
	public String backToPaymentInput() {
		//ログインチェック　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			
			return "redirect:/client/order/payment/input";
			
		} else {
			return "redirect:/login";
		}
		
	}
	
	/**
	 * ご注文の確定ボタン押下後の処理
	 * 
	 * 在庫チェックを行い在庫切れ商品があれば確認画面へリダイレクト
	 * 在庫切れ商品がなければ完了処理を行う
	 * 
	 * @return　"/client/order/complete"へリダイレクト
	 */
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.POST)
	public String complete(Model model) {
		//ログインチェック　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			//商品情報の取得
			@SuppressWarnings("unchecked")
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
			BasketBean basketItem;
			//在庫チェック
			for (int i = 0; i < basketList.size(); i++) {

				basketItem = basketList.get(i);
				//商品IDをもとにItem型の変数を作成
				int id = basketItem.getId();
				Item item = itemRepository.getReferenceById(id);
				//商品が在庫切れの場合
				if (item.getStock() == 0 || basketItem.getOrderNum() > item.getStock()) {
					//注文内容確認画面へリダイレクト
					return "redirect:/client/order/check";

				}
			}

			Order order = new Order();
			//注文情報の取得
			OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
			//注文商品情報の取得
			@SuppressWarnings("unchecked")
			List<OrderItemBean> orderItemBeanList = (List<OrderItemBean>) session.getAttribute("orderItems");
			List<OrderItem> orderItemList = new ArrayList<>();
			//注文商品情報の登録
			for (int i = 0; i < orderItemBeanList.size(); i++) {

				OrderItem orderItem = new OrderItem();
				Item item = new Item();

				OrderItemBean orderItemBean = orderItemBeanList.get(i);
				int id  = orderItemBean.getId();
				item = itemRepository.getReferenceById(id);
				System.out.println(item.getName());
				BeanUtils.copyProperties(orderItemBean, orderItem, "id");

				orderItem.setPrice(orderItemBean.getPrice());
				orderItem.setQuantity(orderItemBean.getOrderNum());
				orderItem.setItem(item);

				orderItemList.add(orderItem);

			}
			//注文情報をＤＢに登録
			User user = userRepository.getReferenceById(userBean.getId());
			order.setUser(user);
			order.setOrderItemsList(orderItemList);
			BeanUtils.copyProperties(orderForm, order, "id");
			orderRepository.save(order);

			for (int i = 0; i < orderItemList.size(); i++) {
				//注文商品情報をＤＢに登録
				OrderItem orderItem = new OrderItem();
				orderItem = orderItemList.get(i);
				Item item = orderItem.getItem();
				orderItem.setItem(item);
				orderItem.setOrder(order);
				orderItemRepository.save(orderItem);
				//在庫状況の更新
				int stock = item.getStock();
				stock -= orderItem.getQuantity();
				item.setStock(stock);
				itemRepository.save(item);
			}
			//セッションスコープから注文情報と買い物かご情報の削除
			session.removeAttribute("orderForm");
			session.removeAttribute("baskets");

			return "redirect:/client/order/complete";

		} else {
			return "redirect:/login";
		}
	}

	/**
	 * 注文完了画面
	 * 
	 * @return　注文完了画面へ遷移
	 */
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
	public String completed() {

		//ログインチェックを行う　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {

			return "client/order/complete";

		} else {
			return "redirect:/login";
		}
	}
}

//package jp.co.sss.shop.controller.client.order.payment.input;
//
//import javax.servlet.http.HttpSession;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import jp.co.sss.shop.bean.OrderBean;
//import jp.co.sss.shop.form.OrderForm;
//import jp.co.sss.shop.form.UserForm;
//import jp.co.sss.shop.repository.UserRepository;
//
///**
// * 支払方法ページ（一般会員）
// * 
// * 
// * @author Ohashi
// *
// */
//@Controller
//public class ClientOrderRegistController {
//
//	/**
//	 * 会員情報　リポジトリ
//	 */
//	@Autowired
//	UserRepository userRepository;
//	
//	OrderBean orderBean;
//
//	/**
//	 * セッション
//	 */
//	@Autowired
//	HttpSession session;
//	
//	/**
//	 * 会員登録 入力画面表示処理
//	 *
//	 * @return "/client/order/address_input" 会員登録入力画面
//	 */
//	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
//	public String registUser(@ModelAttribute OrderForm orderForm) {
//		return "client/order/address_input";
//	}
//	/**
//	 * 会員登録 入力チェック処理
//	 * @param userForm 会員フォーム
//	 * @param result 入力チェック結果
//	 * @return
//	 		エラーがあった場合:"client/user/regist_input" 入力画面表示
//			エラーがない場合:"redirect:/client/user/regist/check" 確認画面へリダイレクト
//	 */
//	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
//	public String doRegistUser(@Valid @ModelAttribute OrderForm orderForm, BindingResult result) {
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
//			session.setAttribute("userForm", orderForm);
//			
//			// 確認画面へリダイレクト
//			returnStr = "redirect:/client/order/payment/input";
//		}
//		
//		return returnStr;
//	}
//	
//	/**
//	 * 会員登録 確認画面表示処理
//	 *
//	 * @return "client/user/regist_check" 注文確認画面
//	 */
//	@RequestMapping(path = "/client/order/check")
//	public String checkRegistUser() {
//		return "client/order/check";
//	}
//	
//	
//	/**
//	 * 購入完了画面　表示処理
//	 *
//	 * @return "client/order/complete" 注文完了画面　表示
//	 */
//	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
//	public String registCompleteFinish() {
//		return "client/order/complete";
//	}
//
//	
////	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
////	public String registInput() {
////		
////		return "redirect:/client/order/address_input";
////	}
////	
////	@RequestMapping(path = "/client/order/address/back", method = RequestMethod.POST)
////	public String registback() {
////		
////		return "redirect:/client/order/address/input";
////	}
////
////	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
////	public String showRegist() {
////		
////		return "client/order/address_input";
//////	}
//
//	
//	//フォーム画面の表示
//	@RequestMapping(path ="/client/order/payment/input", method = {RequestMethod.GET})
//	public String paymentInputOrder() {
//		return "client/order/payment_input";
//	}
//	@RequestMapping(path ="/client/order/payment/input", method = {RequestMethod.POST})
//	public String doPaymentInputOrder(@ModelAttribute UserForm userForm) {
//		return "client/order/payment_input";
//		
//	}
//}

