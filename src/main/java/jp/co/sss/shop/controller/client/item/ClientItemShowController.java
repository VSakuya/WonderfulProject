package jp.co.sss.shop.controller.client.item;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ClientItemShowController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	FavoriteRepository favoriteRepository;
	
	@Autowired
	UserRepository userRepository;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;
	
	

	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/index" , method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
		return "index";
	}


	/**
	 * 商品詳細表示
	 * @param id 商品id
	 * @param model Viewとの値受け渡し
	 * @return "detail" 商品詳細画面
	 * @author tanaka
	 */
	@RequestMapping(path = "/client/item/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Integer id, Model model) {
		Item item = itemRepository.getReferenceById(id);
	    ItemBean itemBean = beanTools.copyEntityToItemBean(item);
	    itemBean.setImage(item.getImageList().get(0).getImagePath());
	    UserBean userBean =(UserBean) session.getAttribute("user");
	    Integer userId  = userBean.getId();
	    User user = userRepository.findByIdAndDeleteFlag(userId, 0);
	    List<Favorite> favorite = favoriteRepository.findByUser(user);
	    
	    
	    model.addAttribute("favorite", favorite);
		model.addAttribute("items", itemBean);
		return"client/item/detail";
	}

	/**
	 *戻るボタン押して商品一覧画面に戻る
	 * @return "list" 商品一覧画面
	 * @author tanaka
	 */
	@RequestMapping(path = "/client/item/new_arrival/back", method = RequestMethod.POST)
	public String rollback() {
		return"redirect:/client/item/new_arrival";
	}

	/**
	 * 商品一覧表示 新着順
	 * @param model Viewとの値受け渡し
	 * @return "client/item/new_arrival"商品新着順画面
	 * @author Tomita
	 */





	@RequestMapping(path="/client/item/new_arrival", method =  RequestMethod.GET)
	public String newArrival(Model model, Pageable pageable) {
		Page<Object[]> itemsPage = itemRepository.findByDeleteFlagOrderByInsertDateDescIdDescPage(Constant.NOT_DELETED, pageable);
		List<Object[]> itemList = itemsPage.getContent();

		model.addAttribute("itemsPage", itemsPage);
		model.addAttribute("itemList", itemList );

		return"client/item/new_arrival";
	}

	/**
	 * 商品売れ筋表示
	 * @param model Viewとの値受け渡し
	 * @return "hotsell"商品売れ筋画面
	 * @author Inaba
	 */
	@RequestMapping("/client/item/list/2")
	public String showFindDateForRanking(Model model, Pageable pageable) {
		Page<Item> pageList = itemRepository.findDataForHotSell(pageable);
		List<Item> itemList = pageList.getContent();
		model.addAttribute("pageList", pageList);
		model.addAttribute("itemList", itemList);
		model.addAttribute("ranking", itemRepository.findDataForHotSell(pageable));
		return "client/item/hotsell";
	}

}
