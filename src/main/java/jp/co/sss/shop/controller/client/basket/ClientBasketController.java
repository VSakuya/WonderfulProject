package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	 * 
	 * @return
	 */
	
	@RequestMapping(path = "/client/basket/list", method = RequestMethod.GET)
	public String showCart( HttpSession session) {
		
		
		
		BasketBean basketBean = new BasketBean(1, "ここから商品詳細 itemId = 1", 30);
		
		List<BasketBean> basketList = new ArrayList<BasketBean>();
		
		basketList.add(basketBean);
		
		
		session.setAttribute("baskets", basketList);
		
		

		if(session.getAttribute("baskets") == null) {
			return "index";
		}
		
		
		return "client/basket/list";
	}
	
	/**
	 * 買い物かごへ追加ボタン
	 * 
	 * @return "/client/basket/list"へリダイレクト
	 */
	
	
	@RequestMapping(path = "/client/basket/add", method = RequestMethod.POST)
	public String addToCart(HttpSession session, int id) {
		
		Item item = itemRepository.getReferenceById(id);
		
		session.getAttribute("baskets");
		
		
		
		
		BasketBean basketBean = new BasketBean(item.getId(), item.getName(), item.getStock());
		List<BasketBean> basketList = new ArrayList<BasketBean>();
		basketList.add(basketBean);
		
		session.setAttribute("baskets", basketList);
		
		return "redirect:/client/basket/list";
	}
	
	/**
	 * 削除ボタン
	 * 
	 * @return
	 */
	@RequestMapping(path = "/client/basket/delete/{id}", method = RequestMethod.POST)
	public String deleteItem() {
		
		return "redirect:/client/basket/list";
	}
	
	/**
	 * 買い物かごを空にするボタン
	 * @return　一覧へリダイレクト
	 */
	@RequestMapping(path = "/client/basket/deleteAll", method = RequestMethod.POST)
	public String deleteAll() {
		
//		session.removeAttribute("baskets");
		
		return "redirect:/client/basket/list";
	}
}
