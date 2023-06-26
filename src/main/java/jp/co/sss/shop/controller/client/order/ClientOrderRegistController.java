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

@Controller
public class ClientOrderRegistController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	BeanTools beanTools;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
	public String order(Model model) {
		
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			
			int userId = userBean.getId();
			User user = userRepository.findByIdAndDeleteFlag(userId, 0);
			
			OrderForm orderForm = new OrderForm();
			
			orderForm.setId(user.getId());
			orderForm.setPostalCode(user.getPostalCode());
			orderForm.setAddress(user.getAddress());
			orderForm.setName(user.getName());
			orderForm.setPhoneNumber(user.getPhoneNumber());
			orderForm.setPayMethod(1);
			
			session.setAttribute("orderForm", orderForm);
			
			return "redirect:/client/order/address/input";
			
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
	public String inputAddress(OrderForm form) {
		
		OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
//		model.addAttribute("orderForm", orderForm);
//		
//		if (result.hasErrors()) {
//			session.setAttribute("result", result);
//		}
		
		return "client/order/address_input";
	}
	
	
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.POST)
	public String payment(@Valid @ModelAttribute OrderForm orderForm, BindingResult result) {
		
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			
			if (result.hasErrors()) {
				
				session.setAttribute("result", result);
				
				return "redirect:/client/order/address/input";
			}
			
			OrderForm exOrderForm = (OrderForm) session.getAttribute("orderForm"); 
			
			exOrderForm.setAddress(orderForm.getAddress());
			exOrderForm.setPostalCode(orderForm.getPostalCode());
			exOrderForm.setName(orderForm.getName());
			exOrderForm.setPhoneNumber(orderForm.getPhoneNumber());
			
			session.setAttribute("orderForm", exOrderForm);
			
			return "redirect:/client/order/payment/input";
			
		} else {
			return "redirect:/login";
		}
	}
	
	
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.GET)
	public String paymentInput() {
		
		return "client/order/payment_input";
	}
	
	
	
	@RequestMapping(path = "/client/order/payment/back", method = RequestMethod.POST)
	public String backToAddressInput() {
		
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			
			return "redirect:/client/order/address/input";
			
		} else {
			return "redirect:/login";
		}
		
	}
	
	
	@RequestMapping(path = "/client/order/check", method = RequestMethod.POST)
	public String check(int payMethod) {
		
		OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
		orderForm.setPayMethod(payMethod);
		session.setAttribute("orderForm", orderForm);
		
		return "redirect:/client/order/check";
	}
	
	
	
	
	@RequestMapping(path = "/client/order/check", method = RequestMethod.GET)
	public String orderCheck(Model model) {
		
//		OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
		
		@SuppressWarnings("unchecked")
		List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
		
		BasketBean basketItem;
		
		int total = 0;
		
		for (int i = 0; i < basketList.size(); i++) {						//商品の削除フラグチェック
			
			basketItem = basketList.get(i);
			int itemId = basketItem.getId();
			
			Item item = itemRepository.getReferenceById(itemId);
			
			if(item.getDeleteFlag() == 1) {
				basketList.remove(i);
			}
		}
		
		for (int i = 0; i < basketList.size(); i++) {						//商品のチェック
			
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
		
		List<OrderItemBean> orderList = new ArrayList<>();
		
		OrderItemBean orderItemBean = new OrderItemBean();
		
		for (int i = 0; i < basketList.size(); i++) {
			
			basketItem = basketList.get(i);
			
			int id = basketItem.getId();
			Item item = itemRepository.getReferenceById(id);
			
			orderItemBean = beanTools.generateOrderItemBean(item, basketItem);
			Image image = (item.getImageList()).get(0);
			String imagePath = image.getImagePath();
			orderItemBean.setImage(imagePath);
			
			orderList.add(orderItemBean);
			
			total += basketItem.getOrderNum() * item.getPrice();
		}
		
		model.addAttribute("total", total);
		session.setAttribute("orderItems", orderList);
		
		return "client/order/check";
	}
	
	
	@RequestMapping(path = "/client/order/check/back", method = RequestMethod.POST)
	public String backToPaymentInput() {
		
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			
			return "redirect:/client/order/payment/input";
			
		} else {
			return "redirect:/login";
		}
		
	}
	
	
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.POST)
	public String complete(Model model) {
		
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			
			@SuppressWarnings("unchecked")
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
			BasketBean basketItem;
			
			for (int i = 0; i < basketList.size(); i++) {						//商品のチェック
				
				basketItem = basketList.get(i);
				
				if (basketItem.getStock() == 0) {								//在庫切れの場合
					
					basketList.remove(i);
					session.setAttribute("baskets", basketList);
					model.addAttribute("stockNone", basketItem.getName());
					
				}
			}
			
			Order order = new Order();
			
			OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");			//注文情報（住所や電話番号）
			
			@SuppressWarnings("unchecked")
			List<OrderItemBean> orderItemBeanList = (List<OrderItemBean>) session.getAttribute("orderItems");
			List<OrderItem> orderItemList = new ArrayList<>();
			
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
			
			for (int i = 0; i < orderItemList.size(); i++) {		//orderItemList内チェック
				OrderItem orderItem = orderItemList.get(i);
				Item item = orderItem.getItem();
				System.out.println(item.getName());
			}
			
			User user = userRepository.getReferenceById(userBean.getId());
			order.setUser(user);
			order.setOrderItemsList(orderItemList);
			BeanUtils.copyProperties(orderForm, order, "id");
			orderRepository.save(order);
			
			
			
			
			for (int i = 0; i < orderItemList.size(); i++) {
				
				OrderItem orderItem = new OrderItem();
				orderItem = orderItemList.get(i);
				Item item = orderItem.getItem();
				orderItem.setItem(item);
				orderItem.setOrder(order);
				orderItemRepository.save(orderItem);
			}
			
			
			
			session.removeAttribute("orderForm");
			session.removeAttribute("baskets");
			
			return "redirect:/client/order/complete";
			
		} else {
			return "redirect:/login";
		}
	}
	
	
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
	public String completed() {
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

