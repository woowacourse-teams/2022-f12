import Home from '@/pages/Home/Home';
import NotFound from '@/pages/NotFound/NotFound';
import Product from '@/pages/Product/Product';
import Products from '@/pages/Products/Products';
import Profile from '@/pages/Profile/Profile';
import ProfileSearch from '@/pages/ProfileSearch/ProfileSearch';
import Register from '@/pages/Register/Register';
import NonUserRoutes from '@/pages/common/NonUserRoutes/NonUserRoutes';
import PageLayout from '@/pages/common/PageLayout/PageLayout';
import RegisterCompleteRoute from '@/pages/common/RegisterIncompleteRoute/RegisterCompleteRoute';
import UserRoutes from '@/pages/common/UserRoutes/UserRoutes';

import Login from '@/components/Login/Login';

import ROUTES from '@/constants/routes';

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
          { path: ROUTES.HOME, element: <Home /> },
          { path: ROUTES.PRODUCTS, element: <Products /> },
          { path: `${ROUTES.PRODUCT}/:productId`, element: <Product /> },
          { path: ROUTES.PROFILE_SEARCH, element: <ProfileSearch /> },
          { path: ROUTES.FOLLOWING, element: <ProfileSearch type="following" /> },
          { path: `${ROUTES.PROFILE}/:memberId`, element: <Profile /> },

          USER_ROUTES([{ path: ROUTES.MY_PROFILE, element: <Profile /> }]),
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
