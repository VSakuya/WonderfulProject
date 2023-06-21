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
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
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
	 * カテゴリー検索
	 * @param categoryId カテゴリーidの値受け渡し
	 * @param model Viewとの値受け渡し
	 * @return "list" 商品一覧画面
	 */
	@RequestMapping(path = "/client/item/list/{sortType}", method = RequestMethod.GET)
	public String category(@PathVariable Integer categoryId, Model model) {
		Category category = new Category();
		category.setId(categoryId);
		List<Item> items = itemRepository.findByCategory(category);
		model.addAttribute("items", items);
		return"client/item/list";
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
		Item item = itemRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		ItemBean itemBean = beanTools.copyEntityToItemBean(item);
		if (item == null) {
		return "redirect:/syserror";
		}
//		session.setAttribute("image", item);
		session.setAttribute("id", id);
		model.addAttribute("items",itemBean);
		return"client/item/detail";
	}

	/**
	 *戻るボタン押して商品一覧画面に戻る
	 * @return "list" 商品一覧画面
	 * @author tanaka
	 */
	@RequestMapping(path = "/client/item/list/1", method = RequestMethod.POST)
	public String rollback() {
		return"redirect:/client/item/list";
	}

	/**
	 * 商品一覧表示 新着順
	 * @param model Viewとの値受け渡し
	 * @return "list/new/arrival"商品新着順画面
	 * @author Tomita
	 * @param pageable 
	 */


	@RequestMapping(path = "/client/item/list/1")
	public String idSortDesc(Model model, Pageable pageable) {
		Page<Object[]> itemsPage = itemRepository.findByDeleteFlagOrderByInsertDateDescIdDescPage(Constant.NOT_DELETED, pageable);
		List<Object[]> itemList = itemsPage.getContent();
		
		
		model.addAttribute("items", itemRepository.findAllByOrderByIdDesc());
		model.addAttribute("itemsPage", itemsPage);
		model.addAttribute("itemList", itemList);
		return "client/item/list";

	}




}
