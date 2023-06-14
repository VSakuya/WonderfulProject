package jp.co.sss.shop.controller.client.basket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *  買い物かご管理　買い物かご関連機能（一般会員用）コントローラクラス
 * 
 * @author Wada
 *
 */
@Controller
public class ClientBasketController {
	
	/**
	 * 買い物かご一覧
	 * @return
	 */
	
	@RequestMapping(path = "/client/basket/list", method = RequestMethod.GET)
	public String showCart() {
		
		return "client/basket/list";
	}
	
	/**
	 * 買い物かごへ追加ボタン
	 * @return
	 */
	
	@RequestMapping(path = "/client/basket/add", method = RequestMethod.POST)
	public String addToCart() {
		
		
			return "redirect:/client/basket/list";
	}
	
	/**
	 * 削除ボタン
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
	@RequestMapping(path = "/basket/deleteAll", method = RequestMethod.POST)
	public String deleteAll() {
		
		return "redirect:/client/basket/list";
	}
}
