import ReviewListSection from './ReviewListSection';
import { reviews } from '../../mocks/data';

export default {
  component: ReviewListSection,
  title: 'Section/ReviewListSection',
};

const Template = () => (
  <ReviewListSection data={reviews} getNextPage={() => undefined} />
);

export const Default = () => <Template />;
