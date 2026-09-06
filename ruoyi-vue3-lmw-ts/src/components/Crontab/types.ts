export type Emits = {
  update: [data1: keyof CronType, data2: string, data3: string]
}
export type CronType = {
  second: string
  min: string
  hour: string
  day: string
  month: string
  week: string
  year: string
}
export type Props = {
  cron: CronType
  check: (data1: number, data2: number, data3: number) => number
}
