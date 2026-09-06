import type { User } from '@/service/types/user';

export default function () {
  const user = ref<User>({
    token: '',
    id: 0,
  });

  const setUserInfo = (userInfo: User) => {
    user.value = userInfo;
  };

  return { user, setUserInfo };
}
