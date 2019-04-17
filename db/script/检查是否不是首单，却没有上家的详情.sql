SELECT 
    *
FROM
    ORD_ORDER_DETAIL
WHERE
    ORDER_ID IN (SELECT 
            id
        FROM
            ORD_ORDER
        WHERE
            ORDER_STATE > 2)
        AND id NOT IN (SELECT 
            DOWNLINE_ORDER_DETAIL_ID
        FROM
            ORD_BUY_RELATION)
        AND RETURN_STATE = 0
        AND ONLINE_TITLE NOT LIKE '%测试%'
        AND PAY_SEQ IS NULL and SUBJECT_TYPE=1;