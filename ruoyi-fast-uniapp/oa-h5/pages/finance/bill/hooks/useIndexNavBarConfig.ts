interface RightIconConfig {
  type: number,
  icon: string
}

type NavBarEventHandler = (e: Event) => void;

type IndexNavBarConfigHookTuple = [
  RightIconConfig[],
  NavBarEventHandler
];

export const useIndexNavBarConfig = (): IndexNavBarConfigHookTuple => {
  const rightIconList = [
    {
      type: 1,
      icon: "icon-a-gengduo2"
    }
  ];

  const handleNavbarRightClick = () => {
    uni.navigateTo({
      url: "/pages/finance/bill/add"
    });
  }

  return [
    rightIconList,
    handleNavbarRightClick
  ];
}