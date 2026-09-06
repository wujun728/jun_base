import Api from "@/annotation/Api";
import Schema from "@/annotation/Schema";
import SchemaComponent from "@/annotation/SchemaComponent";
import { ElInput } from "element-plus";
import { h } from "vue";
import { parseTime } from "@/utils/ruoyi";

@Api("/pay/invoice")
export class PayInvoice {
  /** 发票id */
  @SchemaComponent("table", {
    render() {
      return h('span', {}, this.$slots);
    }
  })
  @SchemaComponent("form", h(ElInput, { placeholder: "请输入发票id" }))
  @Schema("发票id")
  invoiceId!: number;

  /** 订单号 */
  @SchemaComponent("table", h('span'))
  @SchemaComponent("query", h(ElInput, { placeholder: "请输入订单号" }))
  @SchemaComponent("form", h(ElInput, { placeholder: "请输入订单号" }))
  @Schema("订单号")
  orderNumber!: string;

  /** 发票类型 */
  @SchemaComponent("table", h('span'))
  @SchemaComponent("form", h(ElInput, { placeholder: "请选择发票类型" }))
  @Schema("发票类型")
  invoiceType!: string;

  /** 发票抬头 */
  @SchemaComponent("table", h('span'))
  @SchemaComponent("form", h(ElInput, { placeholder: "请输入发票抬头" }))
  @Schema("发票抬头")
  invoiceHeader!: string;

  /** 纳税人识别号 */
  @SchemaComponent("table", h('span'))
  @SchemaComponent("form", h(ElInput, { placeholder: "请输入纳税人识别号" }))
  @Schema("纳税人识别号")
  invoiceNumber!: string;

  /** 收票人手机号 */
  @SchemaComponent("table", h('span'))
  @SchemaComponent("form", h(ElInput, { placeholder: "请输入收票人手机号" }))
  @Schema("收票人手机号")
  invoicePhone!: string;

  /** 收票人邮箱 */
  @SchemaComponent("table", h('span'))
  @SchemaComponent("form", h(ElInput, { placeholder: "请输入收票人邮箱" }))
  @Schema("收票人邮箱")
  invoiceEmail!: string;

  /** 发票备注 */
  @SchemaComponent("table", h('span'))
  @SchemaComponent("form", h(ElInput, { placeholder: "请输入发票备注" }))
  @Schema("发票备注")
  invoiceRemark!: string;

  /** 创建时间 */
  @SchemaComponent("table", {
    props: ['value'],
    render() {
      return h('span', {}, parseTime(this.value, '{y}-{m}-{d}'));
    }
  })
  @Schema('创建时间')
  createTime!: string;

  /** 更新时间 */
  @SchemaComponent("table", {
    props: ['value'],
    render() {
      return h('span', {}, parseTime(this.value, '{y}-{m}-{d}'));
    }
  })
  @Schema('更新时间')
  updateTime!: string;

}
