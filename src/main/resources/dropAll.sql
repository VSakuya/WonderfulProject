-- 注意！！！テスト用、使用前は必ず譚浩鳴に確認してください。
-- 全部のテーブルをドロップ
BEGIN
   FOR t IN (SELECT table_name FROM user_tables) LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS');
   END LOOP;
END;
/

-- 全部のシーケンサーをドロップ
BEGIN
   FOR s IN (SELECT sequence_name FROM user_sequences) LOOP
      EXECUTE IMMEDIATE ('DROP SEQUENCE ' || s.sequence_name);
   END LOOP;
END;
/

COMMIT;
