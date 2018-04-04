/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/4/4 15:42:33                            */
/*==============================================================*/


drop table if exists ORD_ORDER;

drop table if exists ORD_ORDER_DETAILS;

/*==============================================================*/
/* Table: ORD_ORDER                                             */
/*==============================================================*/
create table ORD_ORDER
(
   ID                   bigint not null comment '订单ID',
   ORDER_ID             varchar(50) not null comment '订单编号',
   USER_ID              bigint not null comment '用户编号',
   USER_NAME            varchar(50) not null comment '用户姓名',
   CONSIGNEE_NAME       varchar(20) not null comment '收货人姓名',
   CONSIGNEE_CONTACT    varchar(30) not null comment '收货人联系方式',
   ENTRY_AREA           varchar(500) not null comment '用户收货地址',
   ORDER_TITLE          varchar(200) not null comment '订单标题',
   ORDER_MONEY          decimal(50,4) not null comment '下单金额',
   REAL_MONEY           decimal(50,4) not null comment '实际金额',
   EXPRESS_COMPANY_ID   bigint comment '快递公司编号',
   EXPRESS_COMPANY_NAME varchar(100) comment '快递公司名称',
   EXPRESS_ORDER_ID     varchar(500) comment '快递单号',
   OP_TIME              datetime not null comment '购买时间',
   ORDER_STATE          tinyint not null comment '订单状态（ -1：作废，0：草稿，1：待支付，2：待发货，3：已发货 ，4：代签收，5：已签收，6：已结算）',
   EXPRESS_STATE        tinyint not null comment '快递状态（-1：作废，0：无，1：待揽件，2：运输中，3：派件中，4：已签收，5：拒收，6：问题件）',
   primary key (ID)
);

alter table ORD_ORDER comment '订单信息';

/*==============================================================*/
/* Table: ORD_ORDER_DETAILS                                     */
/*==============================================================*/
create table ORD_ORDER_DETAILS
(
   ID                   bigint not null comment '订单详情ID',
   ORDER_ID             bigint not null comment '订单ID',
   ONLINE_ID            bigint not null comment '上线ID',
   ONLINE_TITLE         varchar(200) not null comment '上线标题',
   SPEC_ID              bigint not null comment '规格ID',
   SPEC_NAME            varchar(100) not null comment '规格名称',
   BUY_COUNT            int not null comment '购买数量',
   BUY_PRICE            decimal(4) not null comment '购买价格',
   CASHBACK_AMOUNT      decimal(4) not null comment '返现金额',
   primary key (ID)
);

alter table ORD_ORDER_DETAILS comment '订单详情';

