package jp.co.sss.shop.controller.client.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;

@RestController
public class ClientItemDataController {

    @Autowired
    ItemRepository itemRepository;

    // Demo for Ajax
    @PostMapping("/client/item/test")
    public ResponseEntity<List<ItemBean>> testMethod() {
        List<Item> items = itemRepository.findAll();
        List<ItemBean> itemBeans = new ArrayList<ItemBean>();
        for (Item item : items) {
            ItemBean itemBean = new ItemBean();
            BeanUtils.copyProperties(item, itemBean);
            itemBean.setCategoryId(item.getCategory().getId());
            itemBean.setCategoryName(item.getCategory().getName());
            itemBean.setImage(item.getImageList().get(0) == null ? "" : item.getImageList().get(0).getImagePath());
            itemBeans.add(itemBean);
        }
        BeanUtils.copyProperties(items, itemBeans);
        return new ResponseEntity<>(itemBeans, HttpStatus.OK);
    }
}
