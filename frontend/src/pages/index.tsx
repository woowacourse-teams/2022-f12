import PageLayout from './common/PageLayout/PageLayout';
import Home from './Home/Home';
import Product from './Product/Product';
import Products from './Products/Products';

export const PAGES = [
  {
    element: <PageLayout />,
    children: [
      { path: '/', element: <Home /> },
      { path: '/products', element: <Products /> },
      { path: '/product', element: <Product /> },
    ],
  },
];
