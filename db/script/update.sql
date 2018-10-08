-- 2018年10月8日11:13:07添加：
	-- 商品购买关系表（ORD_GOODS_BUY_RELATION）
	/*==============================================================*/
	/* Table: ORD_GOODS_BUY_RELATION                                */
	/*==============================================================*/
	create table ORD_GOODS_BUY_RELATION
	(
	   ID                   bigint not null comment '商品购买关系ID',
	   UPLINE_USER_ID       bigint not null comment '上家用户ID',
	   DOWNLINE_USER_ID     bigint not null comment '下家用户ID',
	   ONLINE_ID            bigint not null comment '上线ID',
	   SALE_PRICE           decimal(18,4) not null comment '销售价格',
	   CREATE_TIME          datetime not null comment '创建时间',
	   primary key (ID),
	   unique key AK_UPLINE_USER_AND_DOWNLINE_USER_AND_ONLINE_AND_SALE_PRICE (UPLINE_USER_ID, DOWNLINE_USER_ID, ONLINE_ID, SALE_PRICE)
	);
	
	alter table ORD_GOODS_BUY_RELATION comment '用户商品购买关系';
	
	-- 修改订单详情（ORD_ORDER_DETAIL）表字段类型
	alter table ORD_ORDER_DETAIL modify column BUY_PRICE            decimal(18,4) not null comment '购买价格（单价）';
	alter table ORD_ORDER_DETAIL modify column CASHBACK_AMOUNT      decimal(18,4) not null comment '返现金额';
	alter table ORD_ORDER_DETAIL modify column CASHBACK_TOTAL       decimal(18,4) comment '返现总额';
	
--2018年9月7日17:49:54  
	--添加：表下级购买信息（ORD_SUBLEVEL_BUY）
	/*==============================================================*/
	/* Table: ORD_SUBLEVEL_BUY                                      */
	/*==============================================================*/
	create table ORD_SUBLEVEL_BUY
	(
	   ID                   bigint not null comment '下级购买信息ID',
	   ORDER_DETAIL_ID      bigint not null comment '订单详情ID',
	   SUBLEVEL_USER_ID     bigint not null comment '下级买家ID',
	   primary key (ID)
	);
	
	alter table ORD_SUBLEVEL_BUY comment '下级购买信息';
	
	--在订单详情表（ORD_ORDER_DETAIL）新增表字段：返现佣金名额（CASHBACK_COMMISSION_SLOT）、返现佣金状态（CASHBACK_COMMISSION_STATE）
	alter table ORD_ORDER_DETAIL add CASHBACK_COMMISSION_SLOT tinyint comment '返现佣金名额';
	alter table ORD_ORDER_DETAIL add CASHBACK_COMMISSION_STATE tinyint comment '返现佣金状态（0：匹配中，1：待返，2：已返）';