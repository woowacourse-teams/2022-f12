import PageLayout from '@/pages/common/PageLayout/PageLayout';
import Home from '@/pages/Home/Home';
import Product from '@/pages/Product/Product';
import Products from '@/pages/Products/Products';
import Login from '@/components/Login/Login';
import Register from '@/pages/Register/Register';
import ROUTES from '@/constants/routes';
import InventoryContextProvider from '@/contexts/InventoryContextProvider';
import Profile from '@/pages/Profile/Profile';
import UserRoutes from '@/pages/common/UserRoutes/UserRoutes';
import NonUserRoutes from '@/pages/common/NonUserRoutes/NonUserRoutes';

const USER_ROUTES = [
  {
    element: <UserRoutes />,
    children: [
      { path: ROUTES.REGISTER, element: <Register /> },
      {
        path: ROUTES.PROFILE,
        element: (
          <InventoryContextProvider>
            <Profile />
          </InventoryContextProvider>
        ),
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
      ...NON_USER_ROUTES,
      ...USER_ROUTES,
    ],
  },
];
