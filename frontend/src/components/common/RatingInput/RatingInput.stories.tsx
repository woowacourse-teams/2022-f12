import RatingInput from '@/components/common/RatingInput/RatingInput';
import { useState } from 'react';

export default {
  component: RatingInput,
  title: 'Components/RatingInput',
};

const Template = () => {
  const [rating, setRating] = useState(0);
  return <RatingInput rating={rating} setRating={setRating} />;
};

export const Default = () => <Template />;
