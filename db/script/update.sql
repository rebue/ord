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