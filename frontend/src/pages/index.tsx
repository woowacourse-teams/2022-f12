import PageLayout from '@/pages/common/PageLayout/PageLayout';
import Home from '@/pages/Home/Home';
import Product from '@/pages/Product/Product';
import Products from '@/pages/Products/Products';
import ROUTES from '@/constants/routes';

export const PAGES = [
  {
    element: <PageLayout />,
    children: [
      { path: ROUTES.HOME, element: <Home /> },
      { path: ROUTES.PRODUCTS, element: <Products /> },
      { path: `${ROUTES.PRODUCT}/:productId`, element: <Product /> },
    ],
  },
];
