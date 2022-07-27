import ReviewListSection from '@/components/ReviewListSection/ReviewListSection';
import { reviewsWithProduct } from '@/mocks/data';

export default {
  component: ReviewListSection,
  title: 'Section/ReviewListSection',
};

const Template = () => (
  <ReviewListSection
    columns={1}
    data={reviewsWithProduct}
    getNextPage={() => undefined}
  />
);

export const Default = () => <Template />;
