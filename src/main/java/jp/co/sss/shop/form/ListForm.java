package jp.co.sss.shop.form;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 商品検索機能のフォーム
 *
 * @author Tan
 *
 *
 */
public class ListForm implements Serializable {

	/**
	 * 文字列検索
	 */
	private String searchStr;

	/**
	 * 最小価格
	 */
	@Min(0)
	private Integer minPrice = 0;

	/**
	 * 最大価格
	 * -1は制限なし
	 */
	@Max(9999999)
	private Integer maxPrice = 9999999;

	/**
	 * 在庫確認
	 * -1	:確認しない
	 *  0 	：在庫なし
	 *  1	:在庫あり
	 */
	private Integer isInstock = -1;

	/**
	 * カテゴリID
	 * デファクトは-1
	 */
	private Integer categoryId = -1;

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Integer getIsInstock() {
		return isInstock;
	}

	public void setIsInstock(Integer isInstock) {
		this.isInstock = isInstock;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}


}
