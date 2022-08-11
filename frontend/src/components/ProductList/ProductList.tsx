import { Link } from 'react-router-dom';

import Masonry from '@/components/common/Masonry/Masonry';
import NoDataPlaceholder from '@/components/common/NoDataPlaceholder/NoDataPlaceholder';
import ProductCard from '@/components/common/ProductCard/ProductCard';

import ROUTES from '@/constants/routes';

function ProductList({ data }: { data: Product[] }) {
  return (
    <Masonry columnCount={4}>
      {data.map(({ id, imageUrl, name, rating, reviewCount }) => (
        <Link to={`${ROUTES.PRODUCT}/${id}`} key={id}>
          <ProductCard
            imageUrl={imageUrl}
            name={name}
            rating={rating}
            reviewCount={reviewCount}
          />
        </Link>
      ))}
      {data.length === 0 && <NoDataPlaceholder />}
    </Masonry>
  );
}

export default ProductList;
