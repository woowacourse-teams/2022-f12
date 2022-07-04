import PageLayout from './common/PageLayout/PageLayout';
import Home from './Home/Home';
import Product from './Product/Product';
import Products from './Products/Products';
import ROUTES from '../constants/routes';

export const PAGES = [
  {
    element: <PageLayout />,
    children: [
      { path: ROUTES.HOME, element: <Home /> },
      { path: ROUTES.PRODUCTS, element: <Products /> },
      { path: ROUTES.PRODUCT, element: <Product /> },
    ],
  },
];
