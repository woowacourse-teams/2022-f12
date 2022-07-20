import PageLayout from '@/pages/common/PageLayout/PageLayout';
import Home from '@/pages/Home/Home';
import Product from '@/pages/Product/Product';
import Products from '@/pages/Products/Products';
import Login from '@/components/Login/Login';
import Register from './Register/Register';
import ROUTES from '@/constants/routes';
import Profile from '@/pages/Profile/Profile';

export const PAGES = [
  {
    element: <PageLayout />,
    children: [
      { path: ROUTES.HOME, element: <Home /> },
      { path: ROUTES.PRODUCTS, element: <Products /> },
      { path: `${ROUTES.PRODUCT}/:productId`, element: <Product /> },
      { path: ROUTES.LOGIN, element: <Login /> },
      { path: ROUTES.REGISTER, element: <Register /> },
      { path: ROUTES.PROFILE, element: <Profile /> },
    ],
  },
];
