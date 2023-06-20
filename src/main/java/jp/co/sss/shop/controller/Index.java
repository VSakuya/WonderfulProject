package jp.co.sss.shop.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.util.Constant;

/**
 * トップページ画面のコントローラー
 *
 * @author Tan
 */
@Controller
public class Index {


	/**
	 * スライダーフォルダの中のファイルの配列
	 */
	@Value("classpath:static" + Constant.SLIDER_IMAGE_FOLDER + "*")
	private Resource[] sliderImgs;

	/**
	 * エラー画面表示処理
	 *
	 * @param session セッション情報
	 * @return "index" トップページー画面へ
	 * @throws IOException
	 */
	@RequestMapping(path = "/")
	public String indexPage(HttpSession session, Model model) {
		List<String> imgList = new ArrayList<>();
		for (Resource res : sliderImgs) {
			String str = "/static" + Constant.SLIDER_IMAGE_FOLDER + res.getFilename();
			imgList.add(str);
		}
		model.addAttribute("sliderImgs", imgList);
		return "index_new";
	}
}
