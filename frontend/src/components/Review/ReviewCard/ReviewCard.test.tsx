import { render } from '@/__test__/test-utils';
import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';

import ReviewCard from '@/components/Review/ReviewCard/ReviewCard';

import { reviewsWithOutProduct } from '@/mocks/data';

test('삭제 버튼이 존재한다.', async () => {
  const review = reviewsWithOutProduct[0];
  render(<ReviewCard reviewData={review} reviewId={review.id} />);

  await screen.findByRole('article');

  expect(screen.getByRole('button', { name: '삭제' })).toBeInTheDocument();
});
