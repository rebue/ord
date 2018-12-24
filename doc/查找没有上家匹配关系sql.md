
select a.ONLINE_SPEC_ID, count(*) as count from ORD_ORDER_DETAIL a inner join ORD_ORDER b on a.ORDER_ID=b.ID where b.ORDER_STATE > 2 and a.RETURN_STATE != 2 and a.ID not in (select DOWNLINE_ORDER_DETAIL_ID from ORD_BUY_RELATION) group by a.ONLINE_SPEC_ID having count(a.ONLINE_SPEC_ID)>1;

select a.SPEC_NAME, count(*) as count from ORD_ORDER_DETAIL a inner join ORD_ORDER b on a.ORDER_ID=b.ID where b.ORDER_STATE > 2 and a.RETURN_STATE != 2 and a.ID not in (select DOWNLINE_ORDER_DETAIL_ID from ORD_BUY_RELATION) and ONLINE_SPEC_ID = 0 group by a.SPEC_NAME having count(a.SPEC_NAME)>1;
