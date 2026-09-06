import {dashboardRoutes} from '../routes';

const mixinRoutes = [...dashboardRoutes];

const dashboardMenus = mixinRoutes.map((el) => {
    const {name, path, meta, redirect, children} = el;
    return {
        name,
        path,
        meta,
        redirect,
        children,
    };
});

export default dashboardMenus;
