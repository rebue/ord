

-- 2018-11-28
alter table ORD_TASK						add					SUB_TASK_TYPE        										tinyint 				default -1 comment '子任务类型';
alter table ORD_TASK						add					unique key AK_TASK_TYPE_AND_SUB_TASK_TYPE_AND_ORDER (TASK_TYPE, SUB_TASK_TYPE, ORDER_ID);
alter table ORD_TASK						modify				TASK_TYPE            											tinyint 				not null 	comment '任务类型（1：订单自动取消的任务  2：订单自动签收的任务 3: 订单开始结算的任务 4: 订单结算的任务 5: 订单完成结算的任务）';
alter table ORD_ORDER					add					unique key AK_ORDER_CODE (ORDER_CODE);
alter table ORD_ORDER					drop					key AK_SHIPPER_LOGISTIC_CODE;


-- 2018-11-22
alter table ORD_RETURN           		change          	RETURN_RENTAL      REFUND_TOTAL         	decimal(18,4) not null comment '退款总额（退款总额=退款余额+退款返现金+退款补偿金）';
alter table ORD_RETURN           		drop column  	RETURN_AMOUNT1;
alter table ORD_RETURN           		drop column  	RETURN_AMOUNT2;
alter table ORD_ORDER_DETAIL		add                	IS_DELIVERED         											bool comment '是否已发货';
alter table ORD_ORDER_DETAIL		modify 			CASHBACK_TOTAL       									decimal(18,4) not null comment '返现总额(返现总额=返现金额 * (购买数量-退货数量)';
alter table ORD_ORDER_DETAIL		modify 			BUY_COUNT            											int not null comment '购买数量(实际数量=购买数量-退货数量)';
alter table ORD_RETURN					modify 			REFUND_TOTAL         										decimal(18,4) comment '退款总额（退款总额=退款余额+退款返现金+退款补偿金）';


-- 2018-11-20
alter table ORD_RETURN          		drop column  	REFUND_STATE;
alter table ORD_RETURN           		drop column  	SUBTRACT_CASHBACK;
alter table ORD_RETURN			 		add                	REFUND_COMPENSATION  							decimal(18,4) default 0 comment '退款补偿金额(退货退款产生的需补偿给卖家的金额，例如补偿运费)';


-- 2018-11-16
alter table ORD_ORDER_DETAIL						  add            IS_SETTLE_BUYER      bool comment '是否结算给买家';
alter table ORD_ORDER_DETAIL						  add            ACTUAL_AMOUNT        decimal(18,4) comment '实际成交金额';


-- 2018-11-15
alter table ORD_ORDER                                      add          DELIVER_ORG_ID       bigint                               comment '发货组织ID(默认填入上线组织ID，可变更为供应商的ID)';
alter table ORD_ORDER                                      add          ONLINE_ORG_ID         bigint                               comment '上线组织ID'; 
update ORD_ORDER set RETURN_TOTAL=0 where RETURN_TOTAL is null;
alter table ORD_ORDER                                      modify      RETURN_TOTAL         decimal(50,4) not null default 0 comment '退货总额';

-- 2018-11-14
alter table ORD_ORDER                                       add         PAY_ORDER_ID         bigint not null                     comment '支付订单ID
            提供给第三方支付记录的订单ID（因为有可能会多笔订单合并支付）
            确认订单时，默认填写为订单ID(ORDER_ID)
            拆分订单时，拆分后的订单的支付订单ID仍为旧订单的支付订单ID不变';
update ORD_ORDER set PAY_ORDER_ID=ID;
update ORD_ORDER_DETAIL set RETURN_COUNT=0 where RETURN_COUNT is null;
alter table ORD_ORDER_DETAIL                         modify    RETURN_COUNT         int not null default 0         comment '退货数量';


-- 2018-11-12
alter table ORD_ORDER_DETAIL                         add         PRODUCT_SPEC_ID      bigint             null          comment '产品规格ID';
-- alter table ORD_ORDER_DETAIL                         add         ONLINE_SPEC_ID          bigint            not null    comment '上线规格ID';
alter table ORD_ORDER                                       modify     USER_NAME                  varchar(50)  null          comment '作废-下单人姓名';


-- 2018-11-08
alter table ORD_ORDER_DETAIL                         add     DELIVER_ORG_ID          bigint                comment '发货组织ID(默认填入上线组织ID，可变更为供应商的ID)';
create table ORD_ORDER_DETAIL_DELIVER
(
   ID                   bigint not null comment '发货ID',
   ORDER_ID             bigint not null comment '订单ID',
   ORDER_DETAIL_ID      bigint not null comment '订单详情ID',
   LOGISTIC_ID          bigint not null comment '物流订单ID',
   primary key (ID),
   unique key AK_ORDER_AND_ORDER_DETAIL_AND_LOGISTIC_ID (ORDER_ID, ORDER_DETAIL_ID, LOGISTIC_ID)
);
alter table ORD_ORDER_DETAIL_DELIVER comment '订单详情发货信息';
alter table ORD_ORDER_DETAIL_DELIVER add constraint FK_Relationship_6 foreign key (ORDER_ID)
      references ORD_ORDER (ID) on delete restrict on update restrict;
alter table ORD_ORDER_DETAIL_DELIVER add constraint FK_Relationship_7 foreign key (ORDER_DETAIL_ID)
      references ORD_ORDER_DETAIL (ID) on delete restrict on update restrict;


-- 2018年11月6日
	-- alter table ORD_ORDER_DETAIL add DELIVER_ORG_TYPE     tinyint comment '发货组织类型（1：本组织发货 2：供应商发货）';
    -- alter table ORD_ORDER_DETAIL add IS_DELIVER                     bool not null default false comment '是否已发货';
    -- alter table ORD_ORDER_DETAIL add ONLINE_ORG_ID             bigint comment '上线组织ID';


-- 2018年11月5日17:20:08
	-- ORD_ORDER_DETAIL 添加成本价格（COST_PRICE）
alter table ORD_ORDER_DETAIL add COST_PRICE           decimal(18,4) comment '成本价格（单个）';
-- 2018年11月5日17:05:47
	-- ORD_ORDER_DETAIL 去除供应商结算类型、添加押货类型（PLEDGE_TYPE）
	-- alter table ORD_ORDER_DETAIL add PLEDGE_TYPE          tinyint comment '押货类型（1：押货 2：供应商发货';


-- 2018年11月3日16:27:51
	-- ORD_ORDER_DETAIL 添加供应商id和供应商结算类型
alter table ORD_ORDER_DETAIL add SUPPLIER_ID          bigint comment '供应商ID';


-- 2018年10月8日15:17:14
	-- 添加订单购买关系（ORD_BUY_RELATION）表字段：关系来源（RELATION_SOURCE）
	alter table ORD_BUY_RELATION add RELATION_SOURCE      tinyint not null comment '关系来源（1：自己匹配自己  2：购买关系  3：注册关系  4：差一人且已有购买关系  5：差两人  6：差一人但没有购买关系）';
-- 2018年10月8日11:13:07
	-- 添加：商品购买关系表（ORD_GOODS_BUY_RELATION）
	/*==============================================================*/
	/* Table: ORD_GOODS_BUY_RELATION                                */
	/*==============================================================*/
	create table ORD_GOODS_BUY_RELATION
	(
	   ID                   bigint not null comment '商品购买关系ID',
	   UPLINE_USER_ID       bigint not null comment '上家用户ID',
	   DOWNLINE_USER_ID     bigint not null comment '下家用户ID',
	   ONLINE_ID            bigint not null comment '上线ID',
	   CREATE_TIME          datetime not null comment '创建时间',
	   primary key (ID),
	   unique key AK_UPLINE_USER_AND_DOWNLINE_USER_AND_ONLINE_AND_SALE_PRICE (UPLINE_USER_ID, DOWNLINE_USER_ID, ONLINE_ID)
	);
	
	alter table ORD_GOODS_BUY_RELATION comment '用户商品购买关系';
	
	-- 修改订单详情（ORD_ORDER_DETAIL）表字段类型
	alter table ORD_ORDER_DETAIL modify column BUY_PRICE            decimal(18,4) not null comment '购买价格（单价）';
	alter table ORD_ORDER_DETAIL modify column CASHBACK_AMOUNT      decimal(18,4) not null comment '返现金额';
	alter table ORD_ORDER_DETAIL modify column CASHBACK_TOTAL       decimal(18,4) comment '返现总额';
	
-- 2018年9月7日17:49:54  
	-- 添加：表下级购买信息（ORD_SUBLEVEL_BUY）
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
	
	-- 在订单详情表（ORD_ORDER_DETAIL）新增表字段：返现佣金名额（CASHBACK_COMMISSION_SLOT）、返现佣金状态（CASHBACK_COMMISSION_STATE）
	alter table ORD_ORDER_DETAIL add CASHBACK_COMMISSION_SLOT tinyint comment '返现佣金名额';
	alter table ORD_ORDER_DETAIL add CASHBACK_COMMISSION_STATE tinyint comment '返现佣金状态（0：匹配中，1：待返，2：已返）';
    
    
-- 2018-12-01
alter table ORD_ORDER_DETAIL add ONLINE_SPEC_ID       bigint not null comment '上线规格ID';
alter table ORD_ORDER_DETAIL modify ONLINE_SPEC_ID       bigint not null comment '上线规格ID(限购时获取用户购买数量时使用)';





-- --------------------------------------------------------------------------------------------------------------------

alter table ORD_ORDER_DETAIL  		add 						ORDER_TIMESTAMP      bigint not null default 0 comment '下单时间戳(用于排序识别详情的先后顺序)';

-- --------------------------------------------------------------------------------------------------------------------





alter table ORD_ORDER_DETAIL add BUY_POINT            decimal(18,4) comment '购买积分';
alter table ORD_ORDER_DETAIL add BUY_POINT_TOTAL      decimal(18,4) comment '购买总积分';
alter table ORD_ORDER_DETAIL add PAY_SEQ             tinyint comment '支付顺序';
alter table ORD_ORDER_DETAIL add unique key AK_PAY_SEQU (PAY_SEQ);


-- ------------------------------------------------------- 上面的已更新到线上 2018.12.23 ----------------------------------------------------





alter table ORD_BUY_RELATION add unique key AK_DOWNLINE_ORDER_DETAIL (DOWNLINE_ORDER_DETAIL_ID);

-- ------------------------------------------------------- 上面的已更新到线上 2018.12.24 ----------------------------------------------------



