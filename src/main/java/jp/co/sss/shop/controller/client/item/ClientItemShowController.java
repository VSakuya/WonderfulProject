package jp.co.sss.shop.controller.client.item;


import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.ListForm;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
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
	OrderItemRepository orderItemRepository;

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
	    UserBean userBean =(UserBean) session.getAttribute("user");
	    if (userBean != null) {
	    	Integer userId  = userBean.getId();
	    	User user = new User();
	    	user.setId(userId);
	    	List<Favorite> favoriteList = favoriteRepository.findByItemAndUser(item, user);
	    	Boolean isFavorite = favoriteList.size() > 0;
	    	model.addAttribute("isFavorite", isFavorite);
		}
		model.addAttribute("item", item);
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
	//@RequestMapping(path="/client/item/new_arrival", method =  RequestMethod.GET)
	//public String newArrival(Model model, Pageable pageable) {
		//<Item> itemsPage = itemRepository.findByDeleteFlagOrderByInsertDateDescIdDescPage(Constant.NOT_DELETED, pageable);
		//List<Item> itemList = itemsPage.getContent();


	@RequestMapping(path = "client/item/new_arrival", method = RequestMethod.GET)
	public String newArrival(Model model) {
		List<Item> itemList =  itemRepository.findByDeleteFlagOrderByInsertDateDescIdDescPage(Constant.NOT_DELETED);
		List<Item> sbList = new ArrayList<Item>();
		if (itemList.size() >= 10) {
			sbList = itemList.subList(0, 10);
		}else {
			sbList = itemList;
		}
		model.addAttribute("itemList", sbList);
		return"client/item/new_arrival";
	}

	/**
	 * 商品売れ筋表示
	 * @param model Viewとの値受け渡し
	 * @return "hot_seller"商品売れ筋画面
	 * @author Inaba
	 */
	@RequestMapping("/client/item/hot_seller")
	public String showFindDateForRanking(Model model) {
		List<Item> itemList = itemRepository.findDataForHotSell();
		List<Item> sbList = new ArrayList<Item>();
		if (itemList.size() >= 10) {
			sbList = itemList.subList(0, 10);
		}else {
			sbList = itemList;
		}
		model.addAttribute("items", sbList);
		return "client/item/hot_seller";

	}


	/**
	 * 商品一覧ページ
	 *
	 * @param model テンプレートのmodel
	 * @param listForm 検索用データ情報
	 * @return ページ"client/item/list"
	 * @author tan
	 */
	@RequestMapping(path = "/client/item/list", method = RequestMethod.GET)
	public String itemList(@Valid @ModelAttribute ListForm listForm, BindingResult result, Pageable pageable, Model model) {
		List<Item> rtItems 	= new ArrayList<>();
		Page<Item> pageItems = new PageImpl<>(rtItems);
		String searchStr 	= listForm.getSearchStr();
		Integer minPrice 	= listForm.getMinPrice();
		Integer maxPrice 	= listForm.getMaxPrice();
		Integer isInstock	= listForm.getIsInstock();
		Integer categoryId	= listForm.getCategoryId();

		if (categoryId != -1) {
			Category category = new Category();
			category.setId(categoryId);
			pageItems = itemRepository.findByCategoryAndDeleteFlag(category, 0, pageable);
		}
		else {
			pageItems = itemRepository.findByStrAndPriceRange(searchStr, minPrice, maxPrice, 0, pageable);
		}
		rtItems = pageItems.getContent();


		model.addAttribute("pageItems", pageItems);
		model.addAttribute("items", rtItems);
		return "client/item/list";
	}

}
