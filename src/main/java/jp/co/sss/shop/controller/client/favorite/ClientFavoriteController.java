package jp.co.sss.shop.controller.client.favorite;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * お気に入り機能　コントローラクラス
 *
 * @author Wada
 */
@Controller
public class ClientFavoriteController {
	/**
	 * お気に入り情報リポジトリ
	 */
	@Autowired
	FavoriteRepository favoriteRepository;

	/**
	 * 商品情報リポジトリ
	 */
	@Autowired
	ItemRepository itemRepository;

	/**
	 * 会員情報リポジトリ
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * ユーザ情報取得用
	 */
	@Autowired
	HttpSession session;

	/**
	 * 商品情報エンティティ
	 */
	Item item;

	/**
	 * お気に入り一覧機能
	 *
	 * @param model　お気に入りリストを保存するリクエストスコープ
	 *
	 * @return　お気に入り一覧画面に遷移
	 */
	@RequestMapping(path = "/client/favorite/list", method = RequestMethod.GET)
	public String showFavorite(Model model) {
		//ログインチェック　ログイン情報がなければログイン画面へ遷移
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			//現在閲覧しているユーザの検索
			int userId = userBean.getId();
			User user = userRepository.findByIdAndDeleteFlag(userId, 0);
			//ユーザのお気に入りリストの取得
			List<Favorite> list = favoriteRepository.findByUser(user);
			//お気に入り内の削除フラグチェック
			for (int i = 0; i < list.size(); i++) {
	
				Item favoritedItem = (list.get(i)).getItem();
				//削除フラグがある場合、お気に入りリストから削除
				if (itemRepository.findByIdAndDeleteFlag(favoritedItem.getId(), 1) == favoritedItem) {
					list.remove(i);
				}
			}
			//お気に入りリストに商品がある場合
			if (list.size() != 0) {
				model.addAttribute("items", list);
			}

			return "client/favorite/list";

		} else {
			return "redirect:/login";
		}
	}

	/**
	 * お気に入り登録/解除機能
	 *
	 * @param id　商品ID
	 *
	 * @return　商品詳細画面に遷移
	 */
	@RequestMapping(path = "/client/favorite/switch", method = RequestMethod.POST)
	public String switchFavorite(int id) {
		//idから商品情報を取得
		item = itemRepository.findByIdAndDeleteFlag(id, 0);
		//セッションからユーザ情報を取得
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean != null) {
			int userId = userBean.getId();
			User user = userRepository.findByIdAndDeleteFlag(userId, 0);
			List<Favorite> favoritedItems = favoriteRepository.findByItemAndUser(item, user);
			//商品情報とユーザ情報からfavoriteテーブル内を検索（お気に入り状態の確認）
			if (favoritedItems.size() > 0) {
				//お気に入りに登録されている場合
				for (Favorite favorite : favoritedItems) {
					favoriteRepository.deleteById(favorite.getId());
				}

				return "redirect:/client/item/detail/" + id;

			} else {
				//お気に入りに登録されていない場合
				Favorite favorite = new Favorite();

				favorite.setUser(user);
				favorite.setItem(item);
				favorite = favoriteRepository.save(favorite);

				return "redirect:/client/item/detail/" + id;
			}
		}else {
			return "redirect:/login";
		}
	}
}