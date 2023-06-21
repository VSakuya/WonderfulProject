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
 * 　showCart() 買い物かご一覧機能
 * 　addToCart() 買い物かごに商品を追加
 * 　deleteItem() 買い物かご内の商品を減らす
 * 　deleteAll() 買い物かご内の商品をすべて削除する
 * 
 * @author Wada
 */
@Controller
public class ClientBasketController {
	
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	/**
	 * 買い物かご用インターフェース
	 */
	@Autowired
	HttpSession session;

	/**
	 * 買い物かご一覧
	 * 削除フラグチェックと在庫チェックも行う
	 * 
	 * @param session　買い物かご
	 * @param model 在庫不足や在庫切れの場合
	 * 
	 * @return 買い物かご一覧画面に遷移
	 */
	@RequestMapping(path = "/client/basket/list", method = RequestMethod.GET)
	public String showCart(Model model) {
		
		if(session.getAttribute("baskets") == null) {							//買い物かごに商品が存在しない場合
			
			return "client/basket/list";
			
		} else {																//買い物かごに商品が存在する場合
			
			@SuppressWarnings("unchecked")
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
			
			BasketBean	basketItem;
			
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
			return "client/basket/list";	
		}
	}
	
	
	/**
	 * 商品を買い物かごに追加
	 * 
	 * @param session　買い物かご
	 * @param id 商品詳細ページから受け取った商品のID
	 * 
	 * @return 買い物かご一覧(showCart())へリダイレクト
	 */
	@RequestMapping(path = "/client/basket/add", method = RequestMethod.POST)
	public String addToCart(int id) {
		
		if (session.getAttribute("baskets") == null) {			//買い物かごに商品がない場合　リストの生成
			
			List<BasketBean> basketList = new ArrayList<BasketBean>();
			
			Item item = itemRepository.getReferenceById(id);
			BasketBean basketBean = new BasketBean(item.getId(), item.getName(), item.getStock());
			basketList.add(basketBean);
			
			session.setAttribute("baskets", basketList);
			
		} else {												//買い物かごに商品がある場合
			
			@SuppressWarnings("unchecked")
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
			
			Item item = itemRepository.getReferenceById(id);
			
			for (int i = 0; i < basketList.size(); i++) {		//買い物かご内の重複チェック
				
				BasketBean basketItem = basketList.get(i);
				
				if (basketItem.getId() == id) {					//買い物かごに追加したい商品が存在する場合
						
						basketItem = basketList.get(i);
						int orderNum = basketItem.getOrderNum();
						basketItem.setOrderNum(orderNum += 1);
						basketList.set(i, basketItem);
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
	 * 注文数を減らす
	 * 
	 * @param session　	買い物かご用
	 * @param index		添字用の数値
	 * 
	 * @return			買い物かご一覧(showCart())へリダイレクト
	 */
	@RequestMapping(path = "/client/basket/delete", method = RequestMethod.POST)
	public String deleteItem(Integer index) {
		
		int listNum = index;
		
		@SuppressWarnings("unchecked")
		List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
		
		BasketBean basketItem = basketList.get(listNum);
		
		int orderNum = basketItem.getOrderNum();
		
		
		if (orderNum == 1) {								//注文数が１つの場合　買い物かごから削除
			
			if (basketList.size() == 1) {
				session.removeAttribute("baskets");
				return "redirect:/client/basket/list";
			}
			
			basketList.remove(listNum);
			session.setAttribute("baskets", basketList);
			
		} else {
			
			basketItem.setOrderNum(orderNum -= 1);			//注文数が２つ以上の場合
			basketList.set(listNum, basketItem);
			session.setAttribute("baskets", basketList);
			
		}
		return "redirect:/client/basket/list";
	}
	
	/**
	 * 買い物かごを空にするボタン
	 * ・スコープ変数basketsの破棄
	 * 
	 * @return　買い物かご一覧(showCart())へリダイレクト
	 */
	@RequestMapping(path = "/client/basket/deleteAll", method = RequestMethod.POST)
	public String deleteAll() {
		
		session.removeAttribute("baskets");
		
		return "redirect:/client/basket/list";
	}
}
