package jp.co.sss.shop.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ListForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.util.Constant;

/**
 * トップページ画面のコントローラー
 *
 * @author Tan
 */
@Controller
public class Index {

	@Autowired
	HttpSession session;

	@Autowired
	ItemRepository itemRepository;

	/**
	 * スライダーフォルダの中のファイルの配列
	 */
	@Value("classpath:static" + Constant.SLIDER_IMAGE_FOLDER + "*")
	private Resource[] sliderImgs;

    /**
     * Thymeleafに定数を読めるようにメソッド
     * @return 定数の実体
     */
    @ModelAttribute("constants")
    public Constant appConstants() {
        return new Constant();
    }

	/**
	 * エラー画面表示処理
	 *
	 * @param session セッション情報
	 * @return "index" トップページー画面へ
	 * @throws IOException
	 */
	@RequestMapping(path = "/")
	public String indexPage(Model model) {
		List<String> imgList = new ArrayList<>();
		for (Resource res : sliderImgs) {
			String str = "/static" + Constant.SLIDER_IMAGE_FOLDER + res.getFilename();
			imgList.add(str);
		}
		model.addAttribute("sliderImgs", imgList);
		return "index_new";
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
