package jp.co.sss.shop.controller.client.item;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.service.PriceCalc;


/**
 * 注文管理　一覧表示機能(一般会員)
 *
 * @author Wandormen
 * TIPS: 一般会員向けの注文一覧と注文一覧詳細の処理です。
 *
 */

@Controller
public class ClientOrderShowController {
	/**
	 * 注文情報
	 */
	@Autowired
	OrderRepository orderRepository;
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * 合計金額計算サービス
	 */
	@Autowired
	BeanTools beanTools;

	@Autowired
	PriceCalc priceCalc;

	/**
	 * 一覧取得、一覧画面表示　処理
	 *
	 * @param model
	 * @param pageable
	 * @param orderBeanList
	 * @return "client/oeder/list" 注文情報 一覧画面へ
	 */

	@RequestMapping(path = "/client/order/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String showOrderList(Model model, Pageable pageable, Object orderBeanList) {

		//全ての注文情報を取得（注文日降順）
		Page<Order> orderList = orderRepository.findAllOrderByInsertdateDescIdDesc(pageable);

		//注文情報リストを生成
		List<OrderBean> orderItemList = new ArrayList<OrderBean>();
		for(Order order : orderList) {
			//表示する注文を生成
			OrderBean orderBean = beanTools.copyEntityToOrderBean(order);

			//orderレコードから紐づくOrderItemのListを取り出す
			List<OrderItem> orderItemList1 = order.getOrderItemsList();
			//合計金額の産出
			int total = priceCalc.orderItemPriceTotal( orderItemList1);

			//合計金額のセット
			orderBean.setTotal(total);

//			orderBeanList.add(orderBean);

		}
		//注文情報リストをViewへ渡す
		model.addAttribute("pages", orderList);
		model.addAttribute("orders", orderBeanList);

//		//全ての注文情報を取得（注文日降順）
//		Page<Order> orderList = orderRepository.findAllOrderByInsertdateDescIdDesc(pageable);
//
//		//注文情報リストを生成
//		List<OrderBean> orderItemList = new ArrayList<OrderBean>();
//		for(Order order : orderList) {
//			//表示する注文を生成
//			OrderBean orderBean = beanTools.copyEntityToOrderBean(order);
//
//			//orderレコードから紐づくOrderItemのListを取り出す
//			List<OrderItem> orderItemList = order.getOrderItemsList();
//			//合計金額の産出
//			int total = priceCalc.orderItemPriceTotal( orderItemList);
//
//			//合計金額のセット
//			orderBean.setTotal(total);
//
//			orderBeanList.add(orderBean);
//
//		}
//		//注文情報リストをViewへ渡す
//		model.addAttribute("pages", orderList);
//		model.addAttribute("orders", orderBeanList);

		return "client/order/list";
	}
}
