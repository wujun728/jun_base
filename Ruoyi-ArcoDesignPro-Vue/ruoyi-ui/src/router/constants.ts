export const WHITE_LIST = [
    {name: 'notFound', children: []},
    {name: 'login', children: []},
    {name: 'Redirect', children: []},
    {
        name: 'dashboard', children: [
            {name: 'Workplace',},
            {name: 'DashboardMonitor',},
        ]
    },
    {
        name: 'user', children: [
            {name: 'Info',},
            {name: 'Setting',}
        ]
    },
];

export const NOT_FOUND = {
    name: 'notFound',
};

export const REDIRECT_ROUTE_NAME = 'Redirect';

export const DEFAULT_ROUTE_NAME = 'Workplace';

export const DEFAULT_ROUTE = {
    locale: 'menu.dashboard.workplace',
    title: '工作台',
    name: DEFAULT_ROUTE_NAME,
    fullPath: '/dashboard/workplace',
};
