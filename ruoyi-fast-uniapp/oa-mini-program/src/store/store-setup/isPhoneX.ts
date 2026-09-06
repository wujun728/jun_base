import type { IsPhoneX } from '@/service/types/user';

export default function () {
  const isIphoneX = ref<IsPhoneX>({
    isIphoneX: false,
  });

  const setIsIphoneX = (phone: IsPhoneX) => {
    isIphoneX.value = phone;
  };

  return { isIphoneX, setIsIphoneX };
}
