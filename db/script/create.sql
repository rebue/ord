/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/4/7 16:57:59                            */
/*==============================================================*/


drop table if exists ORD_ORDER;

drop table if exists ORD_ORDER_DETAIL;

/*==============================================================*/
/* Table: ORD_ORDER                                             */
/*==============================================================*/
create table ORD_ORDER
(
   ID                   bigint not null comment '订单ID',
   ORDER_CODE           varchar(50) not null comment '订单编号',
   ORDER_TITLE          varchar(200) not null comment '订单标题',
   ORDER_MONEY          decimal(50,4) not null comment '下单金额',
   REAL_MONEY           decimal(50,4) not null comment '实际金额',
   ORDER_STATE          tinyint not null comment '订单状态
            -1：作废
            1：已下单（待支付）
            2：已支付（待发货）
            3：已发货（待签收）
            4：已签收（待结算）
            5：已结算',
   USER_ID              bigint not null comment '用户编号',
   USER_NAME            varchar(50) not null comment '用户姓名',
   ORDER_TIME           datetime not null comment '下单时间',
   PAY_TIME             datetime comment '支付时间',
   SEND_TIME            datetime comment '发货时间',
   RECEIVED_TIME        datetime comment '签收时间',
   CLOSE_TIME           datetime comment '结算时间',
   CANCEL_TIME          datetime comment '作废时间',
   LOGISTIC_ID          bigint comment '物流订单ID',
   SHIPPER_CODE         varchar(10) comment '快递公司编号',
   SHIPPER_NAME         varchar(100) comment '快递公司名称',
   LOGISTIC_CODE        varchar(30) comment '快递单号',
   RECEIVER_NAME        varchar(30) comment '收件人名称',
   RECEIVER_TEL         varchar(20) comment '收件人电话',
   RECEIVER_MOBILE      varchar(20) comment '收件人手机',
   RECEIVER_PROVINCE    varchar(20) comment '收件省',
   RECEIVER_CITY        varchar(20) comment '收件市',
   RECEIVER_EXP_AREA    varchar(20) comment '收件区',
   RECEIVER_ADDRESS     varchar(100) comment '收件人详细地址',
   RECEIVER_POST_CODE   char(6) comment '收件地邮编',
   primary key (ID),
   unique key AK_SHIPPER_LOGISTIC_CODE (SHIPPER_CODE, LOGISTIC_CODE)
);

alter table ORD_ORDER comment '订单信息';

/*==============================================================*/
/* Table: ORD_ORDER_DETAIL                                      */
/*==============================================================*/
create table ORD_ORDER_DETAIL
(
   ID                   bigint not null comment '订单详情ID',
   ORDER_ID             bigint not null comment '订单ID',
   ONLINE_ID            bigint not null comment '上线ID',
   ONLINE_TITLE         varchar(200) not null comment '上线标题',
   SPEC_NAME            varchar(50) not null comment '规格名称',
   BUY_COUNT            int not null comment '购买数量',
   BUY_PRICE            numeric(18,4) not null comment '购买价格',
   CASHBACK_AMOUNT      numeric(18,4) not null comment '返现金额',
   BUY_UNIT             varchar(10) comment '购买单位',
   primary key (ID)
);

alter table ORD_ORDER_DETAIL comment '订单详情';

