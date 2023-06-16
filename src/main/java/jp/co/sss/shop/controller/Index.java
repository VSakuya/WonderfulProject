package jp.co.sss.shop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * トップページ画面のコントローラー
 *
 * @author Tan
 */
@Controller
public class Index {

	/**
	 * エラー画面表示処理
	 *
	 * @param session セッション情報
	 * @return "index" トップページー画面へ
	 */
	@RequestMapping(path = "/index")
	public String error(HttpSession session) {
		// セッション情報を無効にする
		session.invalidate();
		return "index_new";
	}
}
