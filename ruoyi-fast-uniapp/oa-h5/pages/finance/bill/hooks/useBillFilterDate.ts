type DateRangeEvent = [string, string];

type BillFilterDateHookReturnType = [
  Ref<string>,
  EventHandler<DateRangeEvent>
];

export const useBillFilterDate = (defaultDateRange: string = ""): BillFilterDateHookReturnType => {

  const dateRange = ref<string>(defaultDateRange);

  const handleSelectDateRange: EventHandler<DateRangeEvent> = (e) => {
    if (!e) {
      dateRange.value = "";
      return;
    }
    dateRange.value = [
      e[0],
      e[1]
    ]
      .map(i => i.replace(/-/g, "/"))
      .join("-");
  }

  return [
    dateRange,
    handleSelectDateRange
  ];
}