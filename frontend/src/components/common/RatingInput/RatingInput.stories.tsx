import { useState } from 'react';

import RatingInput from '@/components/common/RatingInput/RatingInput';

export default {
  component: RatingInput,
  title: 'Components/Common/RatingInput',
};

const Template = () => {
  const [rating, setRating] = useState(0);
  return <RatingInput rating={rating} setRating={setRating} />;
};

export const Default = () => <Template />;
