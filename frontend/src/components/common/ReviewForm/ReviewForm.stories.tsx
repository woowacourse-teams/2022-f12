import ReviewForm from './ReviewForm';
import { ComponentStory } from '@storybook/react';

import sampleProfileImage from './sample_profile.jpg';

export default {
  component: ReviewForm,
  title: 'Components/ReviewForm',
};

const Template: ComponentStory<typeof ReviewForm> = () => <ReviewForm />;

export const Defaults: ComponentStory<typeof ReviewForm> = Template.bind({});
