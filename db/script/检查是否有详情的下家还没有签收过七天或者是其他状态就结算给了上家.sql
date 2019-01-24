
-----------查询订单状态小于4的订单
SELECT 
    b.*
FROM
    ORD_ORDER_DETAIL a,
    ORD_ORDER b
WHERE
    a.ORDER_ID = b.id AND b.ORDER_STATE < 4
        AND a.id IN (SELECT 
            DOWNLINE_ORDER_DETAIL_ID
        FROM
            ORD_ORDER_DETAIL a,
            ORD_BUY_RELATION b
        WHERE
            a.id = b.UPLINE_ORDER_DETAIL_ID
                AND a.COMMISSION_STATE = 2)
GROUP BY b.id;


-----------查询订单状态等于4（已签收状态）且签收时间小于7天的订单
SELECT 
b.*
FROM
    ORD_ORDER_DETAIL a,
    ORD_ORDER b
WHERE
    a.ORDER_ID = b.id AND b.ORDER_STATE = 4
        AND DATEDIFF( NOW(),b.RECEIVED_TIME) > 7
        AND a.id IN (SELECT 
            DOWNLINE_ORDER_DETAIL_ID
        FROM
            ORD_ORDER_DETAIL a,
            ORD_BUY_RELATION b
        WHERE
            a.id = b.UPLINE_ORDER_DETAIL_ID
                AND a.COMMISSION_STATE = 2)
GROUP BY b.id


-----------暂时想不到怎么合成一条语句。。。。。。。。