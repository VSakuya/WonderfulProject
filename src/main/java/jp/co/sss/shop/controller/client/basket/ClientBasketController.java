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
import jp.co.sss.shop.bean.UserBean;
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
		//ログインチェック　ログイン情報がない場合ログイン画面に遷移
		UserBean userBean = (UserBean) session.getAttribute("user");

		if (userBean != null) {
			//買い物かごに商品が存在しない場合　一覧画面に遷移
			if(session.getAttribute("baskets") == null) {

				return "client/basket/list";

			} else {
				//買い物かごに商品が存在する場合
				@SuppressWarnings("unchecked")
				List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
				BasketBean	basketItem;

				//買い物かご内商品の削除フラグチェック
				int max = basketList.size();
				List<String> deletedItem = new ArrayList<>();
				for (int i = 0; i < max; i++) {
					//買い物かご内商品情報の取得
					basketItem = basketList.get(i-deletedItem.size());
					int itemId = basketItem.getId();
					Item item = itemRepository.getReferenceById(itemId);
					//削除フラグが立っている場合
					if(item.getDeleteFlag() == 1) {
						basketList.remove(i-deletedItem.size());
						deletedItem.add(item.getName());
					}
				}
				//削除フラグチェック後買い物かご内に商品がない場合
				if (basketList.size() == 0) {
					session.removeAttribute("baskets");
					return "client/basket/list";
				}

				List<String> stockNoneList = new ArrayList<>();
				List<String> stockShortList = new ArrayList<>();
				//在庫チェック
				max = basketList.size();
				for (int i = 0; i < max; i++) {

					basketItem = basketList.get(i-stockNoneList.size());
					int itemId = basketItem.getId();
					Item item = itemRepository.getReferenceById(itemId);
					//商品が在庫切れの場合
					if (item.getStock() == 0) {
						//買い物かごリストから対象商品の削除
						basketList.remove(i-stockNoneList.size());
						session.setAttribute("baskets", basketList);
						//在庫切れ商品の商品名をリストに保存
						stockNoneList.add(basketItem.getName());

					} else if (basketItem.getOrderNum() > item.getStock()) {
						//商品の注文数が在庫を超えている場合　注文数＝在庫数に変更
						basketItem.setOrderNum(item.getStock());
						basketList.set(i-stockNoneList.size(), basketItem);
						session.setAttribute("baskets", basketList);
						//在庫を超えた商品の商品名をリストに保存
						stockShortList.add(basketItem.getName());

					}
				}
				if (basketList.size() == 0) {
					session.removeAttribute("baskets");
				}
				//リクエストスコープに買い物かご内商品の在庫情報を保存
				model.addAttribute("stockNone", stockNoneList);
				model.addAttribute("stockShort", stockShortList);

				return "client/basket/list";
			}
		} else {
			return "redirect:/login";
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

		UserBean userBean = (UserBean) session.getAttribute("user");

		if (userBean != null) {
			//買い物かごに商品が入っていない場合
			if (session.getAttribute("baskets") == null) {
				//BasketBean型の買い物かごリストの生成
				List<BasketBean> basketList = new ArrayList<BasketBean>();
				//商品情報の取得
				Item item = itemRepository.getReferenceById(id);
				BasketBean basketBean = new BasketBean(item.getId(), item.getName(), item.getStock());
				basketList.add(basketBean);
				//セッションスコープに買い物かごリスト情報を保存
				session.setAttribute("baskets", basketList);

			} else {
				//買い物かごに商品が存在する場合
				@SuppressWarnings("unchecked")
				List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("baskets");
				//商品情報の取得
				Item item = itemRepository.getReferenceById(id);
				//買い物かご内の重複チェック
				for (int i = 0; i < basketList.size(); i++) {

					BasketBean basketItem = basketList.get(i);
					//追加したい商品が買い物かごに存在する場合
					if (basketItem.getId() == id) {
						//買い物かごリストから商品情報の取得
						basketItem = basketList.get(i);
						int orderNum = basketItem.getOrderNum();
						//注文数を１増やす
						basketItem.setOrderNum(orderNum += 1);
						basketList.set(i, basketItem);
						//買い物かご情報をセッションスコープに保存
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

		} else {

			return "redirect:/login";

		}
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

		UserBean userBean = (UserBean) session.getAttribute("user");

		if (userBean != null) {

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
				//注文数が２つ以上の場合
				basketItem.setOrderNum(orderNum -= 1);
				basketList.set(listNum, basketItem);
				session.setAttribute("baskets", basketList);

			}

			return "redirect:/client/basket/list";

		} else {

			return "redirect:/login";

		}
	}

	/**
	 * 買い物かごを空にするボタン
	 * ・スコープ変数basketsの破棄
	 *
	 * @return　買い物かご一覧(showCart())へリダイレクト
	 */
	@RequestMapping(path = "/client/basket/deleteAll", method = RequestMethod.POST)
	public String deleteAll() {
		//ログインチェック　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");

		if (userBean != null) {
			//買い物かご情報を破棄
			session.removeAttribute("baskets");

		return "redirect:/client/basket/list";

		} else {

			return "redirect:/login";

		}
	}
}
