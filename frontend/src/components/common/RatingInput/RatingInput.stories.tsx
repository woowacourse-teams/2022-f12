import RatingInput from './RatingInput';
import { ComponentStory } from '@storybook/react';
import { useState } from 'react';

export default {
  component: RatingInput,
  title: 'Components/RatingInput',
};

const Template: ComponentStory<typeof RatingInput> = (args) => {
  const [rating, setRating] = useState<number>(0);
  return <RatingInput rating={rating} setRating={setRating} {...args} />;
};

export const Defaults = Template.bind({}) as ComponentStory<typeof RatingInput>;
Defaults.args = {};
