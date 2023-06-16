package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;


/**
 *  買い物かご管理　買い物かご関連機能（一般会員用）コントローラクラス
 * 
 * @author Wada
 *
 */
@Controller
public class ClientBasketController {
	
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	
	/**
	 * 買い物かご一覧
	 * 在庫チェックもこのページで行う
	 * 
	 * @return list.htmlに遷移
	 */
	
	@RequestMapping(path = "/client/basket/list", method = RequestMethod.GET)
	public String showCart(HttpSession session, Model model) {
		
//		List<BasketBean> basketList = new ArrayList<BasketBean>();
//		BasketBean basketBean = new BasketBean(1, "ここから商品詳細 itemId = 1", 30, 20);
//		basketList.add(basketBean);
//		session.setAttribute("baskets", basketList);
//		
//		@SuppressWarnings("unchecked")
//		List<BasketBean> ba= (List<BasketBean>) session.getAttribute("baskets");
//		BasketBean bas = new BasketBean(2, "ここから商品詳細 itemId = 2", 302);
//		ba.add(bas);
//		session.setAttribute("baskets", ba);
//		
//		@SuppressWarnings("unchecked")
//		List<BasketBean> bask = (List<BasketBean>) session.getAttribute("baskets");
//		BasketBean baske = new BasketBean(3, "ここから商品詳細 itemId = 3", 30123);
//		bask.add(baske);
//		session.setAttribute("baskets", bask);												テスト用データ
		
		if(session.getAttribute("baskets") == null) {					//買い物かごに商品が存在しない場合
			
			return "client/basket/list";
			
		} else {														//買い物かごに商品が存在する場合
			
//			@SuppressWarnings("unchecked")
//			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
//			session.setAttribute("baskets", basketList);
			
			
			@SuppressWarnings("unchecked")
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
			
			BasketBean	basketItem;
			
			
			for (int i = 0; i < basketList.size(); i++) {
				
				basketItem = basketList.get(i);
				
				if (basketItem.getOrderNum() > basketItem.getStock()) {
					model.addAttribute("stockNone","在庫切れ");
				} else if (basketItem.getOrderNum() == basketItem.getStock()) {
					model.addAttribute("stockShort", "在庫不足");
				}
				
			}
			
		}
		
		return "client/basket/list";
	}
	
	
	
	/**
	 * 買い物かごへ追加ボタン
	 * 
	 * @return "/client/basket/list"へリダイレクト
	 */
	
	
	@RequestMapping(path = "/client/basket/add", method = RequestMethod.POST)
	public String addToCart(HttpSession session,Model model, int id) {
		
		if (session.getAttribute("baskets") == null) {			//買い物かごに商品がない場合　リストの生成
			
			List<BasketBean> basketList = new ArrayList<BasketBean>();
			
			Item item = itemRepository.getReferenceById(id);
			BasketBean basketBean = new BasketBean(item.getId(), item.getName(), item.getStock());
			basketList.add(basketBean);
			
			session.setAttribute("baskets", basketList);
			
		} else {												//買い物かごに商品がある場合
			
			@SuppressWarnings("unchecked")
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
			
			Item		item = itemRepository.getReferenceById(id);
			BasketBean	basketItem = null;
			
			for (int i = 0; i < basketList.size(); i++) {		//買い物かご内の重複チェック
				
				basketItem = basketList.get(i);
				
				if (basketItem.getId() == id) {					//買い物かごに追加したい商品が存在する場合
						
						basketItem = basketList.get(i);
						int orderNum = basketItem.getOrderNum();
						basketItem.setOrderNum(orderNum += 1);
						basketList.add(i, basketItem);
						session.setAttribute("baskets", basketList);
						
						return "redirect:/client/basket/list";
						
				}
			}
			
														//買い物かごに追加したい商品が存在しない場合
			
			BasketBean basketBean = new BasketBean(item.getId(), item.getName(), item.getStock());
			basketList.add(basketBean);
			session.setAttribute("baskets", basketList);
			
		}
		
		return "redirect:/client/basket/list";
	}
	
	
	/**
	 * 削除ボタン
	 * 
	 * @return "/client/basket/list"へリダイレクト
	 */
	@RequestMapping(path = "/client/basket/delete/{id}", method = RequestMethod.POST)
	public String deleteItem(HttpSession session,int count) {
		
		int listNum = count - 1;
		
		@SuppressWarnings("unchecked")
		List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
		
		BasketBean basketItem = basketList.get(listNum);
		
		int orderNum = basketItem.getOrderNum();
		
		if (orderNum == 1) {
			
			basketList.remove(listNum);
			
		} else {
			
			basketItem.setOrderNum(orderNum -= 1);
			basketList.add(listNum, basketItem);
			session.setAttribute("baskets", basketList);
			
		}
		
		return "redirect:/client/basket/list";
	}
	
	/**
	 * 買い物かごを空にするボタン
	 * basketsの破棄
	 * 
	 * @return　"/client/basket/list"へリダイレクト
	 */
	@RequestMapping(path = "/client/basket/deleteAll", method = RequestMethod.POST)
	public String deleteAll(HttpSession session) {
		
		session.removeAttribute("baskets");
		
		return "redirect:/client/basket/list";
	}
}
