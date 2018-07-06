/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/7/3 16:29:09                            */
/*==============================================================*/


drop table if exists ORD_ADDR;

drop table if exists ORD_ORDER;

drop table if exists ORD_ORDER_DETAIL;

drop table if exists ORD_RETURN;

drop table if exists ORD_RETURN_PIC;

drop table if exists ORD_TASK;

/*==============================================================*/
/* Table: ORD_ADDR                                              */
/*==============================================================*/
create table ORD_ADDR
(
   ID                   bigint not null comment '收货地址ID',
   USER_ID              bigint not null comment '用户ID',
   RECEIVER_NAME        varchar(30) not null comment '收件人名称',
   RECEIVER_TEL         varchar(20) comment '收件人电话',
   RECEIVER_MOBILE      varchar(20) comment '收件人手机',
   RECEIVER_PROVINCE    varchar(20) not null comment '收件省',
   RECEIVER_CITY        varchar(20) not null comment '收件市',
   RECEIVER_EXP_AREA    varchar(50) not null comment '收件区/直辖镇',
   RECEIVER_ADDRESS     varchar(100) not null comment '收件人详细地址',
   RECEIVER_POST_CODE   char(6) comment '收件地邮编',
   IS_DEF               bool not null comment '是否为默认收货地址（0：否 1：默认）',
   OP_TIME              datetime not null comment '操作时间',
   primary key (ID)
);

alter table ORD_ADDR comment '用户收货地址';

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
   RETURN_TOTAL         decimal(50,4) comment '退货总额',
   RETURN_AMOUNT1       decimal(50,4) comment '可退货金额1（退到返现金额）',
   RETURN_AMOUNT2       decimal(50,4) comment '可退货总额2（退到余额）',
   ORDER_STATE          tinyint not null comment '订单状态（-1：作废  1：已下单（待支付）  2：已支付（待发货）  3：已发货（待签收）  4：已签收（待结算）  5：已结算  ））
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
   SHIPPER_ID           bigint comment '快递公司ID',
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
   ORDER_MESSAGES       varchar(500) comment '订单留言',
   MODIFY_REALVERY_MONEY_OP_ID bigint comment '修改实际金额操作人ID',
   CANCELING_ORDER_OP_ID bigint comment '取消订单操作人ID',
   CANCELDELI_REASON    varchar(500) comment '取消发货的原因',
   SEND_OP_ID           bigint comment '发货操作人',
   RECEIVED_OP_ID       bigint comment '签收人',
   CANCEL_REASON        varchar(300) comment '作废原因',
   primary key (ID),
   key AK_SHIPPER_LOGISTIC_CODE (SHIPPER_ID, LOGISTIC_CODE)
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
   PRODUCE_ID           bigint not null comment '产品ID',
   ONLINE_TITLE         varchar(200) not null comment '上线标题',
   SPEC_NAME            varchar(50) not null comment '规格名称',
   BUY_COUNT            int not null comment '购买数量',
   BUY_PRICE            numeric(50,4) not null comment '购买价格',
   CASHBACK_AMOUNT      numeric(50,4) not null comment '返现金额',
   RETURN_COUNT         int comment '退货数量',
   CASHBACK_TOTAL       decimal(50,4) comment '返现总额',
   BUY_UNIT             varchar(10) comment '购买单位',
   RETURN_STATE         tinyint not null comment '退货状态（0：未退货  1：退货中  2：已退货  3：部分已退）',
   USER_ID              bigint not null comment '用户ID',
   primary key (ID)
);

alter table ORD_ORDER_DETAIL comment '订单详情';

/*==============================================================*/
/* Table: ORD_RETURN                                            */
/*==============================================================*/
create table ORD_RETURN
(
   ID                   bigint not null comment '退货ID',
   RETURN_CODE          bigint not null comment '退货编号',
   ORDER_ID             bigint not null comment '订单ID',
   ORDER_DETAIL_ID      bigint not null comment '订单详情ID',
   RETURN_COUNT         int not null comment '退货数量',
   RETURN_RENTAL        decimal(18,4) not null comment '退货总额',
   RETURN_AMOUNT1       decimal(18,4) comment '退货金额（余额）',
   RETURN_AMOUNT2       decimal(18,4) comment '退货金额（返现金）',
   SUBTRACT_CASHBACK    decimal(18,4) comment '扣减返现金额',
   RETURN_TYPE          tinyint not null comment '退款类型（1：仅退款  2：退货并退款）',
   APPLICATION_STATE    tinyint not null comment '申请状态（-1：已取消  1：待审核  2：退货中  3：已退货   4：已拒绝）',
   REFUND_STATE         tinyint not null comment '退款状态（1：未退款  2、已退款）',
   RETURN_REASON        varchar(500) not null comment '退货原因',
   USER_ID              bigint not null comment '用户ID',
   APPLICATION_OP_ID    bigint not null comment '申请操作人',
   APPLICATION_TIME     datetime not null comment '申请时间',
   CANCEL_OP_ID         bigint comment '取消操作人',
   CANCEL_TIME          datetime comment '取消时间',
   REVIEW_OP_ID         bigint comment '审核操作人',
   REVIEW_TIME          datetime comment '审核时间',
   REFUND_OP_ID         bigint comment '退款操作人',
   REFUND_TIME          datetime comment '退款时间',
   REJECT_OP_ID         bigint comment '拒绝操作人',
   REJECT_REASON        varchar(200) comment '拒绝原因',
   REJECT_TIME          datetime comment '拒绝时间',
   FINISH_OP_ID         bigint comment '完成操作人',
   FINISH_TIME          datetime comment '完成时间',
   RECEIVE_OP_ID        bigint comment '确认收到货操作人',
   RECEIVE_TIME         datetime comment '确认收到货时间',
   primary key (ID)
);

alter table ORD_RETURN comment '用户退货信息';

/*==============================================================*/
/* Table: ORD_RETURN_PIC                                        */
/*==============================================================*/
create table ORD_RETURN_PIC
(
   ID                   bigint not null comment '退货图片ID',
   RETURN_ID            bigint not null comment '退货ID',
   PIC_PATH             varchar(500) not null comment '图片路径',
   primary key (ID)
);

alter table ORD_RETURN_PIC comment '退货图片';

/*==============================================================*/
/* Table: ORD_TASK                                              */
/*==============================================================*/
create table ORD_TASK
(
   ID                   bigint not null comment '任务ID',
   EXECUTE_STATE        tinyint not null default 0 comment '执行状态(-1:取消；0:未执行；1:已执行)',
   EXECUTE_PLAN_TIME    datetime not null comment '计划执行时间',
   EXECUTE_FACT_TIME    datetime comment '实际执行时间',
   TASK_TYPE            tinyint not null comment '任务类型（1：取消订单  2：订单签收）',
   ORDER_ID             varchar(150) not null comment '订单ID(销售订单ID)',
   primary key (ID)
);

alter table ORD_TASK comment '订单任务';

