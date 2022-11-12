import ReviewListSection from '@/components/Review/ReviewListSection/ReviewListSection';

import { reviewsWithProduct } from '@/mocks/data/reviews';

export default {
  component: ReviewListSection,
  title: 'Section/ReviewListSection',
};

const Template = () => (
  <ReviewListSection
    columns={1}
    data={reviewsWithProduct}
    getNextPage={() => undefined}
    isLoading={false}
    isError={false}
  />
);

export const Default = () => <Template />;
