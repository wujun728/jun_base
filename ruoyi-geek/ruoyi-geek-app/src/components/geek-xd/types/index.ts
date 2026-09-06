export interface Menu {
    icon: string,
    label: string
}

export interface Commodity {
    img: string,
    title: string,
    subTitle?: string,
    price: number
}

export interface CommodityOrder extends Commodity {
    shop: string,
    status: string,
    num: number,
    label?:string
}