import { lazy } from 'react';

import NotFound from '@/pages/NotFound/NotFound';
import NonUserRoutes from '@/pages/common/NonUserRoutes/NonUserRoutes';
import PageLayout from '@/pages/common/PageLayout/PageLayout';
import RegisterCompleteRoute from '@/pages/common/RegisterIncompleteRoute/RegisterCompleteRoute';
import UserRoutes from '@/pages/common/UserRoutes/UserRoutes';

import Login from '@/components/Login/Login';

import ROUTES from '@/constants/routes';

const Home = lazy(() => import(/* webpackChunkName: "Home" */ '@/pages/Home/Home'));
const Profile = lazy(
  () => import(/* webpackChunkName: "Profile" */ '@/pages/Profile/Profile')
);
const ProfileSearch = lazy(
  () =>
    import(/* webpackChunkName: "ProfileSearch" */ '@/pages/ProfileSearch/ProfileSearch')
);
const Register = lazy(
  () => import(/* webpackChunkName: "Register" */ '@/pages/Register/Register')
);
const Product = lazy(
  () => import(/* webpackChunkName: "Product" */ '@/pages/Product/Product')
);
const Products = lazy(
  () => import(/* webpackChunkName: "Products" */ '@/pages/Products/Products')
);

type Route = {
  element: JSX.Element;
  path?: string;
  children?: Route[];
};

const USER_ROUTES = (routes: Route[]): Route => ({
  element: <UserRoutes />,
  children: [...routes],
});

const NON_USER_ROUTES = (routes: Route[]): Route => ({
  element: <NonUserRoutes />,
  children: [...routes],
});

export const PAGES: Route[] = [
  {
    element: <PageLayout />,
    children: [
      {
        element: <RegisterCompleteRoute />,
        children: [
          {
            path: ROUTES.HOME,
            element: <Home />,
          },
          { path: ROUTES.PRODUCTS, element: <Products /> },
          { path: `${ROUTES.PRODUCT}/:productId`, element: <Product /> },
          { path: ROUTES.PROFILE_SEARCH, element: <ProfileSearch /> },
          { path: `${ROUTES.PROFILE}/:memberId`, element: <Profile /> },

          USER_ROUTES([
            { path: ROUTES.MY_PROFILE, element: <Profile /> },
            { path: ROUTES.FOLLOWING, element: <ProfileSearch type="following" /> },
          ]),
          NON_USER_ROUTES([
            {
              element: <NonUserRoutes />,
              children: [{ path: ROUTES.LOGIN, element: <Login /> }],
            },
          ]),
        ],
      },

      // 추가 정보 미 입력시 갈 수 있는 유일한 경로
      USER_ROUTES([{ path: ROUTES.REGISTER, element: <Register /> }]),

      { path: ROUTES.NOT_FOUND, element: <NotFound /> },
    ],
  },
];
