package jp.co.sss.shop.controller.client.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientOrderRegistController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
	public String order() {
		
		UserBean userBean = (UserBean) session.getAttribute("user");
		int userId = userBean.getId();
		User user = userRepository.findByIdAndDeleteFlag(userId, 0);
		
		OrderForm orderForm = new OrderForm();
		
		orderForm.setId(user.getId());
		orderForm.setPostalCode(user.getPostalCode());
		orderForm.setAddress(user.getAddress());
		orderForm.setName(user.getName());
		orderForm.setPhoneNumber(user.getPhoneNumber());
		orderForm.setPayMethod(1);
		
		session.setAttribute("ex", orderForm);
		
		return "redirect:/client/order/address/input";
	}
	
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
	public String inputAddress(Model model) {
		
		OrderForm orderForm = (OrderForm) session.getAttribute("ex");
		model.addAttribute("ex", orderForm);
		
//		if (result.hasErrors()) {
//			session.setAttribute("result", result);
//		}
		
		return "client/order/address_input";
	}
	
	
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.POST)
	public String b(Model model, OrderForm orderForm) {
		
		model.addAttribute(orderForm);
		
		return "redirect:/client/order/payment/input";
	}
	
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.GET)
	public String c() {
		
		return "client/order/payment_input";
	}
	
	
	@RequestMapping(path = "/client/order/check", method = RequestMethod.POST)
	public String d(int payMethod) {
		
		OrderForm orderForm = (OrderForm) session.getAttribute("ex");
		orderForm.setPayMethod(payMethod);
		session.setAttribute("ex", orderForm);
		
		return "redirect:/client/order/check";
	}
	
	
	
	
	
	
	@RequestMapping(path = "/client/order/check", method = RequestMethod.GET)
	public String e(Model model) {
		
		OrderForm orderForm = (OrderForm) session.getAttribute("ex");
		
		@SuppressWarnings("unchecked")
		List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
		
		BasketBean basketItem;
		
		int total = 0;
		
		for (int i = 0; i < basketList.size(); i++) {						//買い物かご内商品の削除フラグチェック
			
			basketItem = basketList.get(i);
			int itemId = basketItem.getId();
			
			Item item = itemRepository.getReferenceById(itemId);
			
			if(item.getDeleteFlag() == 1) {
				basketList.remove(i);
			}
		}
		
		for (int i = 0; i < basketList.size(); i++) {						//買い物かご内商品のチェック
			
			basketItem = basketList.get(i);
			
			if (basketItem.getStock() == 0) {								//在庫切れの場合
				
				basketList.remove(i);
				session.setAttribute("baskets", basketList);
				model.addAttribute("stockNone", basketItem.getName());
				
			} else if (basketItem.getOrderNum() > basketItem.getStock()) {	//注文数が在庫を超えている場合
				
				basketItem.setOrderNum(basketItem.getStock());
				basketList.set(i, basketItem);
				session.setAttribute("baskets", basketList);
				model.addAttribute("stockShort", basketItem.getName());
				
			}
		}
		
		List<OrderItem> orderList = new ArrayList<>();
		OrderItem orderItem = new OrderItem();
		
		for (int i = 0; i < basketList.size(); i++) {
			
			basketItem = basketList.get(i);
			
			int g = basketItem.getId();
			
			Item item = itemRepository.getReferenceById(g);
			
//			int price = basketItem.getOrderNum() * item.getPrice();
			
			orderItem.setItem(item);
			orderItem.setQuantity(basketItem.getOrderNum());
			orderItem.setPrice(item.getPrice());
			orderList.add(i, orderItem);
			
			total += basketItem.getOrderNum() * item.getPrice();
		}
		model.addAttribute("total", total);
		session.setAttribute("ex", orderForm);
		session.setAttribute("orderItems", orderList);
		
		return "client/order/check";
	}
	
	
	
	
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.POST)
	public String f(Model model) {
		
		OrderForm orderForm = (OrderForm) session.getAttribute("ex");
		
		@SuppressWarnings("unchecked")
		List<OrderItem> orderList = (List<OrderItem>) session.getAttribute("orderItems");
		
		@SuppressWarnings("unchecked")
		List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
		
//		BasketBean basketItem;
		
		OrderItem orderItem;
		
		for (int i = 0; i < orderList.size(); i++) {						//買い物かご内商品の削除フラグチェック
			
			orderItem = orderList.get(i);
			int itemId = orderItem.getId();
			
			Item item = itemRepository.getReferenceById(itemId);
			
			if(item.getDeleteFlag() == 1) {
				orderList.remove(i);
			}
		}
		
//		for (int i = 0; i < orderList.size(); i++) {						//買い物かご内商品のチェック
//			
//			orderItem = orderList.get(i);
//			
//			if ((orderItem.getItem()).getStock() == 0) {								//在庫切れの場合
//				
//				orderList.remove(i);
//				session.setAttribute("baskets", orderList);
//				model.addAttribute("stockNone", (orderItem.getItem()).getName());
//				
//			} else if (basketItem.getOrderNum() > (orderItem.getItem()).getStock()) {	//注文数が在庫を超えている場合
//				
//				orderItem.setOrderNum((orderItem.getItem()).getStock());
//				basketList.set(i, basketItem);
//				session.setAttribute("baskets", basketList);
//				model.addAttribute("stockShort", basketItem.getName());
//				
//			}
//		}
//		
//		Order order = new Order();
//		
//		UserBean userBean = (UserBean) session.getAttribute("user");
//		int userId = userBean.getId();
//		User user = userRepository.findByIdAndDeleteFlag(userId, 0);
//		
//		BeanUtils.copyProperties(orderForm, order);
//		BeanUtils.copyProperties(orderList, order);
//		BeanUtils.copyProperties(user, order);
//		
//		orderRepository.save(order);
//		
//		OrderItem orderItems = new OrderItem();
//		
//		for (int i = 0; i < orderList.size(); i++) {						//買い物かご内商品のチェック
//			
//			orderItem = orderList.get(i);
//			
//			
//		}
//		
//		BeanUtils.copyProperties(orderList, orderItems);
		
		return "redirect:/client/order/complete";
	}
	
	
	
	
	
	
	
	
	
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
	public String g() {
		
		return "client/order/complete";
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

