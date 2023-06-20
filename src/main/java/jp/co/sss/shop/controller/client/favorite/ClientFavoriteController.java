package jp.co.sss.shop.controller.client.favorite;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Image;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;





/**
 * お気に入り機能　コントローラクラス
 * 
 * @author Wada
 *
 */

@Controller
public class ClientFavoriteController {
	
	@Autowired
	FavoriteRepository favoriteRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	UserRepository userRepository;
	
	Item item;
	
	Image image;
	
	Favorite favorite;
	
	/**
	 * お気に入り一覧
	 * 削除フラグのチェックも行う
	 * 
	 * @return　client/favorite/listに遷移
	 */
	@RequestMapping(path = "/client/favorite/list", method = RequestMethod.GET)
	public String showFavorite(Model model) {
		
		List<Favorite> list = favoriteRepository.findAll();
		
		for (int i = 0; i < list.size(); i++) {														//削除フラグチェック
			
			Item favoritedItem = (list.get(i)).getItem();
			
			if (itemRepository.findByIdAndDeleteFlag(favoritedItem.getId(), 1) == favoritedItem) {
				list.remove(i);
			}
		}
		
		model.addAttribute("items", list);
		
		return "client/favorite/list";
	}
	
	
	/**
	 * お気に入りのオンオフ
	 * @param session　セッションスコープ
	 * @param id　商品ID
	 * @return　商品詳細画面にリダイレクト
	 */
	
	@RequestMapping(path = "/client/favorite/switch", method = RequestMethod.GET)
	public String switchFavorite(HttpSession session, int id) {
		
		item = itemRepository.getReferenceById(id);
		
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		int userId = userForm.getId();
		User user = userRepository.getReferenceById(userId);
		
		if (favoriteRepository.findByItemAndUser(item, user) != null) {
			
			Favorite favoritedItem = (Favorite) favoriteRepository.findByItemAndUser(item, user);
			favoriteRepository.deleteById(favoritedItem.getId());
			
			return "redirect:/client/item/detail/{id}";
			
			
		} else {
			
			Favorite favorite = new Favorite();
			BeanUtils.copyProperties(user, favorite);
			BeanUtils.copyProperties(item, favorite);
			
			favorite = favoriteRepository.save(favorite);
			
			return "redirect:/client/item/detail/{id}";
		}
		
	}
	
	
	
}