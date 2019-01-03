SELECT 
    *
FROM
    ord.ORD_TASK e
WHERE
    (e.TASK_TYPE = 4
        AND (e.SUB_TASK_TYPE = 1
        OR e.SUB_TASK_TYPE = 4))
        AND e.ORDER_ID IN (SELECT 
            c.ORDER_ID
        FROM
            (SELECT 
                a.ID, a.ORDER_ID
            FROM
                ord.ORD_ORDER_DETAIL a  
            INNER JOIN ord.ORD_ORDER b ON a.ORDER_ID = b.ID
            WHERE
                b.ORDER_STATE = 5
                    AND (a.SUPPLIER_ID IS NOT NULL
                    || a.SUPPLIER_ID != '') and a.BUY_COUNT-a.RETURN_COUNT  >=1 )   c
        WHERE
            CAST(c.ID AS CHAR) NOT IN (SELECT 
                    d.ORDER_DETAIL_ID
                FROM
                    afc.AFC_TRADE d
                WHERE
                    d.TRADE_TYPE = 50))  ;