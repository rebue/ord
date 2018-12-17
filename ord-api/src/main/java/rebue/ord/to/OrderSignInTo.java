package rebue.ord.to;

import lombok.Data;

/**
 * 创建时间：2018年5月16日 上午11:59:23 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrderSignInTo.java 类说明： 订单签收请求参数
 */
@Data
public class OrderSignInTo {

    /** 订单编号 **/
    private Long   orderId;

    /** 用户编号 **/
    private Long   userId;

    /** 用户ip地址 **/
    private String ip;

    /** 用户mac地址 **/
    private String mac;

}
