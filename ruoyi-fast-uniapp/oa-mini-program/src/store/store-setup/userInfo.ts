import type { Users } from '@/service/types/user';

export default function () {
  const users = ref<Users>({
    users:{}
  });
  const setUserInfo2 = (userInfo: Users) => {
    users.value = userInfo;
  };

  return { users ,setUserInfo2 };
}
