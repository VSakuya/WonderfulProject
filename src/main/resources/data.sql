-- レコード登録(カテゴリ)
INSERT INTO categories VALUES(seq_categories.NEXTVAL, '食料品', '野菜類、肉類、海産物、加工食品などを扱います。', DEFAULT, DEFAULT);
INSERT INTO categories VALUES(seq_categories.NEXTVAL, '書籍', '和書、洋書、専門書、漫画、雑誌などを扱います。', DEFAULT, DEFAULT);

-- レコード登録(商品)
INSERT INTO items VALUES(seq_items.NEXTVAL, 'りんご', 100, '青森県産のりんごです。とってもみずみずしい！', 30, 0, DEFAULT, 1);
INSERT INTO items VALUES(seq_items.NEXTVAL, '辞書', 2000, 'これ一冊があれば大丈夫！', 5, 1, DEFAULT, 2);
INSERT INTO items VALUES(seq_items.NEXTVAL, 'オレンジ', 150, 'オーストラリア産のオレンジです。', 30, 0, DEFAULT, 1);

-- レコード登録(会員) すべてのパスワードは'testtest'です、sha256('testtestWonderfulProject')の結果です
INSERT INTO users VALUES(seq_users.NEXTVAL, 'tanaka_taro@test.co.jp', '732955d4bbbc1e09e9e633b7946a402d30da3804eea99480ae402e1a358a40fe', 'システム管理太郎', '1111111', '東京都台東区1-2-3 ABCビル10階', '0123456789', 0, DEFAULT, DEFAULT);
INSERT INTO users VALUES(seq_users.NEXTVAL, 'unyo_jiro@test.co.jp', '732955d4bbbc1e09e9e633b7946a402d30da3804eea99480ae402e1a358a40fe', '運用管理二郎', '1111111', '東京都台東区1-2-3 ABCビル10階', '0123456789', 1, DEFAULT, DEFAULT);
INSERT INTO users VALUES(seq_users.NEXTVAL, 'ippan_saburo@test.co.jp', '732955d4bbbc1e09e9e633b7946a402d30da3804eea99480ae402e1a358a40fe', '一般三郎', '1111111', '東京都台東区4-5-6 ABCマンション5階', '0123456789', 2, DEFAULT, DEFAULT);

-- レコード登録(注文)
INSERT INTO orders VALUES(seq_orders.NEXTVAL, '1111111', '東京都台東区4-5-6 ABCマンション5階', '一般三郎', '0123456789', 2, DEFAULT, 3);
INSERT INTO orders VALUES(seq_orders.NEXTVAL, '1111111', '東京都台東区4-5-6 ABCマンション5階', '一般三郎', '0123456789', 2, DEFAULT, 3);
INSERT INTO orders VALUES(seq_orders.NEXTVAL, '1111111', '東京都台東区4-5-6 ABCマンション5階', '一般三郎', '0123456789', 2, DEFAULT, 3);

-- レコード登録(商品注文)
INSERT INTO order_items VALUES(seq_order_items.NEXTVAL, 4, 120, 1, 1);
INSERT INTO order_items VALUES(seq_order_items.NEXTVAL, 4, 112, 2, 1);
INSERT INTO order_items VALUES(seq_order_items.NEXTVAL, 4, 300, 3, 1);

-- レコード登録(画像)
INSERT INTO images VALUES(seq_images.NEXTVAL, 'test1.jpg', DEFAULT, 1);
INSERT INTO images VALUES(seq_images.NEXTVAL, 'test2.jpg', DEFAULT, 2);
INSERT INTO images VALUES(seq_images.NEXTVAL, 'test3.jpg', DEFAULT, 2);
INSERT INTO images VALUES(seq_images.NEXTVAL, 'test2.jpg', DEFAULT, 1);
INSERT INTO images VALUES(seq_images.NEXTVAL, 'test2.jpg', DEFAULT, 3);
INSERT INTO images VALUES(seq_images.NEXTVAL, 'test1.jpg', DEFAULT, 3);

-- レコード登録(いいね)
INSERT INTO favorite VALUES(seq_favorite.NEXTVAL, DEFAULT, 1, 2);
INSERT INTO favorite VALUES(seq_favorite.NEXTVAL, DEFAULT, 2, 2);
INSERT INTO favorite VALUES(seq_favorite.NEXTVAL, DEFAULT, 3, 1);
INSERT INTO favorite VALUES(seq_favorite.NEXTVAL, DEFAULT, 1, 3);
INSERT INTO favorite VALUES(seq_favorite.NEXTVAL, DEFAULT, 2, 1);

-- コミット
COMMIT;

