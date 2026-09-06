import Api from "@/annotation/Api";
import Schema from "@/annotation/Schema";

@Api("/pay/order")
export class PayOrder {
  /** 订单ID */
  @Schema('订单ID')
  orderId!: number;

  /** 订单号 */
  @Schema('商户订单号')
  orderNumber!: string;

  /** 第三方订单号 */
  @Schema('第三方订单号')
  thirdNumber!: string;

  /** 订单状态 */
  @Schema('订单状态')
  orderStatus!: string;

  /** 订单总金额 */
  @Schema('订单总金额(分)')
  totalAmount!: string;

  /** 实际支付金额 */
  @Schema('实际支付金额(分)')
  actualAmount!: string;

  /** 订单内容 */
  @Schema('订单内容')
  orderContent!: string;

  /** 负载信息 */
  @Schema('负载信息')
  orderMessage!: string;

  /** 支付方式 */
  @Schema('支付方式')
  payType!: string;

  /** 支付时间 */
  @Schema('支付时间')
  payTime!: Date;

  /** 支付人 */
  @Schema('支付人')
  payBy!: string;

  /** 创建人 */
  @Schema('创建人')
  createBy!: string;

  /** 创建时间 */
  @Schema('创建时间')
  createTime!: string;

  /** 更新时间 */
  @Schema('更新时间')
  updateTime!: string;

  /** 备注 */
  @Schema('备注')
  remark!: string;
}