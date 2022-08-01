import PageLayout from '@/pages/common/PageLayout/PageLayout';
import Home from '@/pages/Home/Home';
import Product from '@/pages/Product/Product';
import Products from '@/pages/Products/Products';
import Login from '@/components/Login/Login';
import Register from '@/pages/Register/Register';
import ROUTES from '@/constants/routes';
import Profile from '@/pages/Profile/Profile';
import ProfileSearch from '@/pages/ProfileSearch/ProfileSearch';
import UserRoutes from '@/pages/common/UserRoutes/UserRoutes';
import NonUserRoutes from '@/pages/common/NonUserRoutes/NonUserRoutes';
import NotFound from '@/pages/NotFound/NotFound';

const USER_ROUTES = [
  {
    element: <UserRoutes />,
    children: [
      { path: ROUTES.REGISTER, element: <Register /> },
      {
        path: ROUTES.PROFILE,
        element: <Profile />,
      },
    ],
  },
];

const NON_USER_ROUTES = [
  {
    element: <NonUserRoutes />,
    children: [{ path: ROUTES.LOGIN, element: <Login /> }],
  },
];

export const PAGES = [
  {
    element: <PageLayout />,
    children: [
      { path: ROUTES.HOME, element: <Home /> },
      { path: ROUTES.PRODUCTS, element: <Products /> },
      { path: `${ROUTES.PRODUCT}/:productId`, element: <Product /> },
      { path: `${ROUTES.PROFILE_SEARCH}`, element: <ProfileSearch /> },
      ...NON_USER_ROUTES,
      ...USER_ROUTES,
      { path: ROUTES.NOT_FOUND, element: <NotFound /> },
    ],
  },
];
