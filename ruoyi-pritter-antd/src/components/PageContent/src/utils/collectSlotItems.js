const DEFAULT_EXCEPT = ['todo']

export function collectSlotItems(data, exceptSlot = DEFAULT_EXCEPT, collected = []) {
  if (Array.isArray(data)) {
    data.forEach((item) => collectSlotItems(item, exceptSlot, collected))
    return collected
  }

  if (typeof data !== 'object' || data === null) {
    return collected
  }

  if (data.slotName && !exceptSlot.includes(data.slotName)) {
    collected.push(data)
  }

  Object.values(data).forEach((value) => collectSlotItems(value, exceptSlot, collected))
  return collected
}
